package lePetitCloud;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

import lePetitCloud.entities.Petition;

@Api(name = "petitions", version = "v1", namespace = @ApiNamespace(ownerDomain = "lepetitcloud.appspot.com", ownerName = "lepetitcloud.appspot.com", packagePath = ""))
public class EndPoint {
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)

    public void add() throws UnauthorizedException {

    }
    
    @ApiMethod(name = "createPetition", httpMethod = ApiMethod.HttpMethod.POST)
	public Entity creerPetition(String newPetition) {
    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Entity petition = new Entity("Petition", newPetition);
    	petition.setProperty("name", newPetition);
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	petition.setProperty("date", dateFormat.format(date));
    	petition.setProperty("nombreVotant", 0);
    	
    	datastore.put(petition);
    	
		return petition;
    }

    
    @ApiMethod(name = "getPetition", httpMethod = ApiMethod.HttpMethod.POST)
	public Entity getPetition(Petition searchPetition) throws EntityNotFoundException {
    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Key keyPetition = KeyFactory.createKey("Petition", searchPetition.getId());
    	Entity getPetition = datastore.get(keyPetition);
		return getPetition;
    }
    
}