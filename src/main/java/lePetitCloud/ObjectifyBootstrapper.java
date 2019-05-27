package lePetitCloud;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import lePetitCloud.entities.Petition;

public class ObjectifyBootstrapper implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        ObjectifyService.init();
        ObjectifyService.register(Petition.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}