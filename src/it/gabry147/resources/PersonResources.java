package it.gabry147.resources;

import it.gabry147.entities.Activities;
import it.gabry147.entities.Activity;
import it.gabry147.entities.ActivityType;
import it.gabry147.entities.People;
import it.gabry147.entities.Person;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//class which serve all request with person path
@Stateless
@LocalBean
@Path("/person")
public class PersonResources {
	
	//Request #1
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAll() {
		System.out.println("Get all person...");
		People result = new People();
		List<Person> people = Person.getAllPeople();
		result.setPersonList(people);
		//return always OK, the list can be empty
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	//request#2
	@GET
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getPerson(@PathParam("personId") int id) {	
		System.out.println("Get person...");
		Person person = Person.getPersonById(id);
		if(person == null) return Response.status(Response.Status.NOT_FOUND).build();
		System.out.println("GET: "+person.toString());
		return Response.status(Response.Status.OK).entity(person).build();
	}
	
	//request#3
	@PUT
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updatePerson(@PathParam("personId") int id, Person newPerson) {
		System.out.println("Updating person...");
		Person oldPerson = Person.getPersonById(id);
		//if person to modify doesn't exisit -> bad request
		if(oldPerson == null) return Response.status(Response.Status.BAD_REQUEST).build();
		//for every not null field, update the field
		if(newPerson.getFirstname() != null) oldPerson.setFirstname(newPerson.getFirstname());
		if(newPerson.getLastname() != null) oldPerson.setLastname(newPerson.getLastname());
		if(newPerson.getBirthdate() != null) oldPerson.setBirthdate(newPerson.getBirthdate());
		Person result = Person.updatePerson(oldPerson);
		System.out.println("UPDATE: "+result.toString());
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	//request#4
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response newPerson(Person person) throws IOException {
		System.out.println("Creating new person...");	
		person = Person.savePerson(person);
		//id null -> set to 0
		if (person.getId() == 0) return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		System.out.println("POST: "+person.toString());
		return Response.status(Response.Status.CREATED).entity(person).build();
	}
	
	//request#5
	@DELETE
	@Path("{personId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response removePerson(@PathParam("personId") int id) {
		System.out.println("Deleting person...");
		Person person = Person.getPersonById(id);
		if(person == null) return Response.status(Response.Status.BAD_REQUEST).build();
		Person.removePerson(person);
		System.out.println("DELETE: "+person.toString());
		return Response.status(Response.Status.OK).entity(person).build();
	}
	
	//request#6 in ActivitytypesResources.java
	
	//request#7
	//request#11
	@GET 
	@Path("{id}/{activity_type}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getTypedActivities(@PathParam("id") int personID, @PathParam("activity_type") String type,
			@QueryParam("before") String before, @QueryParam("after") String after) {
		System.out.println("Get activity list...");
		Person person = Person.getPersonById(personID);
		//check person exist
		if(person == null) return Response.status(Response.Status.BAD_REQUEST).build();
		List<Activity> activities = person.getActivitypreference();
		//person exists but no activities
		if(activities == null) return Response.status(Response.Status.NOT_FOUND).build();
		ActivityType activitytype = ActivityType.getActivityByType(type);
		//check type exist
		if(activitytype == null) return Response.status(Response.Status.BAD_REQUEST).build();
		//return value
		Activities filteredActivities = new Activities();
		filteredActivities.setActivityList(new ArrayList<Activity>());
		for(Activity activity : activities) {
			//filter for type
			if(activity.getType().getType().equals(type)) {
				//correct type, default to insert
				Boolean toInsert = true;
				//check if there are string params
				if(before != null && after != null) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		        	Date beforeThis = null;
		        	Date afterThis = null;
					try {
						//create the Dates from the string params
						beforeThis = df.parse(before);
						afterThis = df.parse(after);
					} catch (ParseException e) {
						return Response.status(Response.Status.BAD_REQUEST).build();
					}
					Date activitydate = activity.getStartdate();
					if( ! activitydate.before(beforeThis) ) toInsert = false;
					if( ! activitydate.after(afterThis) ) toInsert = false;
				}
				if(toInsert) {
					filteredActivities.getActivityList().add(activity);
				}
				
			}
		}
		return Response.status(Response.Status.OK).entity(filteredActivities).build();
	}
	
	//request#8 OK
	@GET 
	@Path("{person_id}/{activity_type}/{activity_id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getActivityByTypeAndID(@PathParam("person_id") int personID, @PathParam("activity_type") String type, @PathParam("activity_id") int activityID) {		
		System.out.println("Get activity...");
		Person person = Person.getPersonById(personID);
		//check person
		if(person == null) return Response.status(Response.Status.BAD_REQUEST).build();
		List<Activity> activities = person.getActivitypreference();
		//check list of activities
		if(activities == null) return Response.status(Response.Status.BAD_REQUEST).build();
		ActivityType activitytype = ActivityType.getActivityByType(type);
		//check type existance
		if(activitytype == null) return Response.status(Response.Status.BAD_REQUEST).build();
		for(Activity activity : activities) {
			//find activity with exact ID
			if(activity.getId() == activityID) {
				return Response.status(Response.Status.OK).entity(activity).build();
			}
		}
		//person and type exist, person has activities but activityID not found
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	//request#9 OK
	@POST
	@Path("{id}/{activity_type}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response newActivityByID(
			@PathParam("id") int personID, @PathParam("activity_type") String type,
			Activity activity) {	
		System.out.println("Save activity...");
		Person person = Person.getPersonById(personID);
		//check person
		if(person == null) return Response.status(Response.Status.BAD_REQUEST).build();
		List<Activity> activities = person.getActivitypreference();
		//if first activity, need to create an empty list
		if(activities == null) {
			person.setActivitypreference(new ArrayList<Activity>());
			person.getActivitypreference();
		}
		if(activity.getType().getType().equals(type)) {
			Activity savedActivity = Activity.saveActivity(activity);
			person.getActivitypreference().add(savedActivity);
			Person.updatePerson(person);
			return Response.status(Response.Status.OK).entity(savedActivity).build();			
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	//request#10 OK
	@PUT 
	@Path("{person_id}/{activity_type}/{activity_id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateActivityType(@PathParam("person_id") int personID, @PathParam("activity_type") String type, @PathParam("activity_id") int activityID) 
	{		
		System.out.println("Update type of an activity...");
		Person person = Person.getPersonById(personID);
		//check person
		if(person == null) return Response.status(Response.Status.BAD_REQUEST).build();
		List<Activity> activities = person.getActivitypreference();
		//check activities
		if(activities == null) return Response.status(Response.Status.BAD_REQUEST).build();
		ActivityType activitytype = ActivityType.getActivityByType(type);
		//check type
		if(activitytype == null) return Response.status(Response.Status.BAD_REQUEST).build();
		for(Activity activity : activities) {
			if(activity.getId() == activityID) {
				activity.setType(activitytype);
				Activity modifiedActivity = Activity.updateActivity(activity);
				return Response.status(Response.Status.OK).entity(modifiedActivity).build();
			}
		}
		//person and type exist, person has activities but activityID not found
		return Response.status(Response.Status.NOT_MODIFIED).build();
	}
}
