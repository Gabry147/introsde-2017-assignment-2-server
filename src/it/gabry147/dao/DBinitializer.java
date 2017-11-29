package it.gabry147.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.gabry147.entities.Activity;
import it.gabry147.entities.ActivityType;
import it.gabry147.entities.ActivityTypes;
import it.gabry147.entities.People;
import it.gabry147.entities.Person;
/*
Class for populate the database. First, it checks the existance of ActivityTypes, than check the db in order to have at least minimumPersonNumber (currently 5)
*/
public class DBinitializer {
	//minimum number of person in the db
	private static int minimumPersonNumber = 5;
	//list of activitytypes accepted
	private static String[] tags = {"Sport", "Social", "Multimedia", "Work", "Fun"};
	//list of activitytypes entity, used in most functions
	private static List<ActivityType> types = ActivityType.getAllActivityTypes();
	
	//check and eventually create the activitytypes
	public static ActivityTypes checkAndCreateActivityType() {
		ActivityTypes typesWrapper = new ActivityTypes();
		//if the function is called statically, types can be null
		if(types != null) {
			//if exist and size is not zero, all types are inserted
			if(types.size() != 0) {
				typesWrapper.setTypeList(types);
				return typesWrapper;
			}			
		}
		//no types found, create them from string list
		typesWrapper.setTypeList(new ArrayList<>());
		for(String tag : tags) {
			ActivityType at = new ActivityType();
			at.setType(tag);
			ActivityType.saveActivityType(at);
			typesWrapper.getTypeList().add(at);
		}
		types = typesWrapper.getTypeList();
		return typesWrapper;
	}
	
	//count person in the db, create person to reach a minimum of minimumPersonNumber, than return them
	public static People populatePerson() {
		//check for types
		DBinitializer.checkAndCreateActivityType();
		//get person from db
		List<Person> db = Person.getAllPeople();
		People listForClient = new People();
		listForClient.setPersonList(new ArrayList<Person>());
		//default = create all person needed
		int personToCreate = minimumPersonNumber;
		if(db != null) {
			//db has person, update the number of person to create
			personToCreate = minimumPersonNumber - db.size();
			for(Person old : db) {
				//add first old person to the return object
				listForClient.getPersonList().add(old);
			}
		}
		//if size() is equal or bigger, no iteration
		for(int i=0; i<personToCreate; i++) {
			//create and save random person
			Person p = DBinitializer.createRandomPerson(i);
			//create and save random activity
			Activity a1 = DBinitializer.createRandomActivity(i);
			//second activity have different number but same type if i is odd, useful to test the type filter
			Activity a2 = DBinitializer.createRandomActivity(i + types.size() + i%2);
			//save activity in person	
			p = p.saveActivity(a1);
			p = p.saveActivity(a2);
			listForClient.getPersonList().add(p);
		}
		return listForClient;
	}
	
	//create random person with fake data based on iteration number
	private static Person createRandomPerson(int i) {
		Person p = new Person();
		p.setFirstname("Jhon"+i);
		p.setLastname("Doe"+i);
		Calendar cal = Calendar.getInstance();
		//assuming minimumPersonNumber=5, no need to round value
		cal.set(1990+i, Calendar.JANUARY+i, 1+i);
		Date birth = cal.getTime();
		p.setBirthdate(birth);
		return Person.savePerson(p);
	}

	//create random activity with fake data based on iteration number and types size
	private static Activity createRandomActivity(int i) {
		Activity a = new Activity();
		//activity need to round i to avoid overflow
		a.setType(types.get( i % types.size()));
		a.setName("Activity"+i);
		a.setPlace("Trento");
		a.setDescription("Description "+i);
		
		Calendar cal = Calendar.getInstance();
		cal.set(2016, //year
				(Calendar.JANUARY+i)%12, //month
				((1+i) % 28) + 1, //day
				(8+i) % 24, //hour
				(30+i) % 60 //minute
				);
		Date start = cal.getTime();
		a.setStartdate(start);
		return Activity.saveActivity(a);
	}
}
