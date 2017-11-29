package it.gabry147.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.gabry147.dao.PersonActivitiesDao;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Activity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    @XmlAttribute(name = "id")
    protected int id;

    protected String name;
    
    protected String description;

    protected String place;

    @XmlElement(name = "type")
    @ManyToOne(fetch=FetchType.EAGER)
    protected ActivityType type;
       
    //annotation for display a date in json
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date startdate;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	
	public Activity() {
		//needed for XML
	}

	//db access functions
	public static Activity saveActivity(Activity a) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(a);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return a;
	}
	
	public static Activity updateActivity(Activity a) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		a=em.merge(a);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return a;
	}
	
	public static void removeActivity(Activity a) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    a=em.merge(a);
	    em.remove(a);
	    tx.commit();
	    PersonActivitiesDao.instance.closeConnections(em);
	}
	
	public static Activity getActivityById(int activityId) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		Activity a = em.find(Activity.class, activityId);
		PersonActivitiesDao.instance.closeConnections(em);
		return a;
	}

}
