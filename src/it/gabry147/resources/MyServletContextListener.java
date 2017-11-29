package it.gabry147.resources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import it.gabry147.dao.DBinitializer;

public class MyServletContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Ending ServletContext");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//When the app is deployed and restarted, this function is called -> db never empty
		System.out.println("Starting ServletContextListener");
		DBinitializer.populatePerson();
	}

}
