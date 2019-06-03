package lepetitcloud;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Key;

@Api(name = "petitions", version = "v1", namespace = @ApiNamespace(ownerDomain = "lepetitcloud.appspot.com", ownerName = "lepetitcloud.appspot.com", packagePath = ""))
public class PetitionEndpoint {

	@ApiMethod(name = "createPetition")
	public Entity creerPetition(@Named("name") String name) {

		Entity nouvellePetition = new Entity("Petition", "" + name);
		nouvellePetition.setProperty("name", name);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(nouvellePetition);

		return nouvellePetition;
	}

	@ApiMethod(name = "getPetition")
	public List<Entity> getPetition(@Named("name") String name) {

		Query q = new Query("Petition").setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return result;
	}

}