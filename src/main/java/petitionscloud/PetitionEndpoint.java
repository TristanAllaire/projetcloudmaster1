package petitionscloud;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.datastore.QueryResultList;

@Api(name = "petitions",
version = "v1",
namespace = @ApiNamespace(ownerDomain = "helloworld.example.com",
    ownerName = "helloworld.example.com",
    packagePath = ""))

public class PetitionEndpoint {
		
	@ApiMethod(name = "listAllPetition")
	public QueryResultList<Entity> listAllPetition() {
		FetchOptions option = FetchOptions.Builder.withLimit(10);
	    
		Query q = new Query("Petition");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);

		QueryResultList<Entity> result = pq.asQueryResultList(option);
				
		return result;
	}
	
	@ApiMethod(name = "listAllContributorForAPetition")
	public List<Entity> listAllContributorForAPetition(@Named("namep") String namep) {
		Query q = new Query("PetitionThenContributor")
		        .setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.GREATER_THAN, KeyFactory.createKey("PetitionThenContributor",namep)));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}

	@ApiMethod(name = "listPetition")
	public Entity listPetition(@Named("namep") String namep) {
		Query q = new Query("Petition")
		        .setFilter(new FilterPredicate("namep", FilterOperator.EQUAL, namep));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		return result;
	}
	
	@ApiMethod(name = "listContributor")
	public Entity listContributor(@Named("email") String email, @Named("namep") String namep) {
		
		String research = email+"_"+namep;
		
		Filter researchFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, KeyFactory.createKey("ContributorThenPetition",research));
		
		Query q = new Query("ContributorThenPetition");
		q.setFilter(researchFilter);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		return result;
	}
	
	@ApiMethod(name = "addPetition")
	public Entity addPetition(@Named("namep") String namep, @Named("description") String description, @Named("email") String email) {
		
		Entity e = new Entity("Petition", ""+namep);
		e.setProperty("namep", namep);
		e.setProperty("votecount", 0);
		e.setProperty("description", description);
		e.setProperty("email", email);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(e);
		
		return  e;
	}
	
	@ApiMethod(name = "addAVote")
	public Entity addAVote(@Named("namep") String namep) {
		Query q = new Query("Petition")
		        .setFilter(new FilterPredicate("namep", FilterOperator.EQUAL, namep));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		result.setProperty("namep", namep);
		
		int voteAmount = ((Number) result.getProperty("votecount")).intValue();
		voteAmount++;
		result.setProperty("votecount", voteAmount);
		datastore.put(result);
		
		return  result;
	}
	
	@ApiMethod(name = "getHundredBestPetitions")
	public List<Entity> getHundredBestPetitions() {
		Query q = new Query("Petition").addSort("votecount", SortDirection.DESCENDING);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}
	
	@ApiMethod(name = "voteForPetition")
	public List<Entity> voteForPetition(@Named("namep") String namep,
		@Named("email") String email,
		@Named("firstName") String firstname,
		@Named("lastName") String lastname,
		@Named("zipcode") int zipcode,
		@Named("city") String city) {
		
		String voteName = email + "_" + namep;
		
		Entity e = new Entity("ContributorThenPetition", ""+voteName);
		e.setProperty("namep", namep);
		e.setProperty("firstname", firstname);
		e.setProperty("lastname", lastname);
		e.setProperty("email",email);
		e.setProperty("zipcode", zipcode);
		e.setProperty("city", city);
		
		voteName = namep + "_" + email;
		
		Entity f = new Entity("PetitionThenContributor", ""+voteName);
		f.setProperty("namep", namep);
		f.setProperty("firstname", firstname);
		f.setProperty("lastname", lastname);
		f.setProperty("email",email);
		f.setProperty("zipcode", zipcode);
		f.setProperty("city", city);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(e);
		datastore.put(f);
		
		List<Entity> listReturn = new ArrayList<Entity>();
		listReturn.add(e);
		listReturn.add(f);
		
		return  listReturn;
	}

	
	@ApiMethod(name = "votedForContributor")
	public List<Entity> votedForContributor(@Named("email") String email) {
		/* Renvoi étrangement trop de résultat avec le >
		Query q = new Query("ContributorThenPetition")
		        .setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.GREATER_THAN, KeyFactory.createKey("ContributorThenPetition",email)));
		*/
		
		//Ancienne requête
		Query q = new Query("ContributorThenPetition").setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}
	
	@ApiMethod(name = "createdByContributor")
	public List<Entity> createdByContributor(@Named("email") String email) {		
		Query q = new Query("Petition").setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}
}