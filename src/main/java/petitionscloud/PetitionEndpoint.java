package petitionscloud;


import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.*;


@Api(name = "petitions",
version = "v1",
namespace = @ApiNamespace(ownerDomain = "helloworld.example.com",
    ownerName = "helloworld.example.com",
    packagePath = ""))

public class PetitionEndpoint {
	

	@ApiMethod(name = "listAllPetition")
	public List<Entity> listAllPetition() {
		Query q = new Query("Petition");

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}
	
	@ApiMethod(name = "listAllContributorForAPetition")
	public List<Entity> listAllContributorForAPetition(@Named("name") String name) {
		Query q = new Query("Contributor")
		        .setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}

	@ApiMethod(name = "listPetition")
	public Entity listPetition(@Named("name") String name) {
		Query q = new Query("Petition")
		        .setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		return result;
	}
	
	@ApiMethod(name = "addPetition")
	public Entity addPetition(@Named("name") String name) {
		
		Entity e = new Entity("Petition", ""+name);
		e.setProperty("name", name);
		e.setProperty("votecount", 0);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(e);
		
		return  e;
	}
	
	@ApiMethod(name = "addAVote")
	public Entity addAVote(@Named("name") String name) {
		Query q = new Query("Petition")
		        .setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		result.setProperty("name", name);
		
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
	public Entity voteForPetition(@Named("name") String name,
		@Named("email") String email,
		@Named("firstName") String firstname,
		@Named("lastName") String lastname,
		@Named("zipcode") int zipcode,
		@Named("city") String city) {
		
		Entity e = new Entity("Contributor", ""+email);
		e.setProperty("name", name);
		e.setProperty("firstname", firstname);
		e.setProperty("lastname", lastname);
		e.setProperty("email",email);
		e.setProperty("zipcode", zipcode);
		e.setProperty("city", city);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(e);
		
		
		
		return  e;
	}
	

	
	@ApiMethod(name = "votedForContributor")
	public List<Entity> votedForContributor(@Named("email") String email) {
		Query q = new Query("Contributor")
		        .setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}
}