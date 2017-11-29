package it.gabry147.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonValue;

import it.gabry147.dao.PersonActivitiesDao;

@Entity
@Table(name="ActivityType")
@NamedQuery(name="ActivityType.findAll", query="SELECT at FROM ActivityType at")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ActivityType implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@XmlValue
    protected String type;

	@JsonValue
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ActivityType() {
		//needed for XML
	}
	
	//need this function to read json entity
	public ActivityType(String s) {
		this.setType(s);
	}
	
	public static List<ActivityType> getAllActivityTypes() {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
	    List<ActivityType> list = em.createNamedQuery("ActivityType.findAll", ActivityType.class).getResultList();
	    PersonActivitiesDao.instance.closeConnections(em);
	    return list;
	}
	
	public static ActivityType saveActivityType(ActivityType t) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(t);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return t;
	}
	
	public static ActivityType updateActivityType(ActivityType t) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		t=em.merge(t);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return t;
	}
	
	public static void removeActivityType(ActivityType t) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    t=em.merge(t);
	    em.remove(t);
	    tx.commit();
	    PersonActivitiesDao.instance.closeConnections(em);
	}
	
	public static ActivityType getActivityByType(String type) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		ActivityType at = em.find(ActivityType.class, type);
		PersonActivitiesDao.instance.closeConnections(em);
		return at;
	}
}