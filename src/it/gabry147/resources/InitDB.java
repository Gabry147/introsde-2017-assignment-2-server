package it.gabry147.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import it.gabry147.dao.DBinitializer;
import it.gabry147.entities.People;

@Path("/init")
public class InitDB {
	
	//this function is useless using the ServletContextListener, person will be created there	
	@GET
	public Response populateDB() {
		People peo = DBinitializer.populatePerson();
		return Response.status(Response.Status.OK).entity(peo).build();
	}

}
