package it.gabry147.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class StartPoint {

	//used to check app status and avoid 404 on startpage
	public Response wakeUp() {
		return Response.status(Response.Status.OK).entity(new String("Started")).build();
	}
}
