package it.gabry147.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.gabry147.entities.ActivityType;
import it.gabry147.entities.ActivityTypes;


//class for get activitytypes
@Path("/activity_types")
public class ActivitytypesResources {
	
	//request#6
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAllTypes() {
		ActivityTypes result = new ActivityTypes();
		List<ActivityType> list = ActivityType.getAllActivityTypes();
		result.setTypeList(list);
		return Response.status(Response.Status.OK).entity(result).build();
	}

}
