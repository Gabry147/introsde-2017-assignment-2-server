package it.gabry147.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.gabry147.dao.PersonActivitiesDao;

@Entity
@Table(name="Person")
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Person implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    @XmlAttribute(name = "id")
    protected int id;

    protected String firstname;

    protected String lastname;
   
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date birthdate;
   
    @XmlElementWrapper(name = "activitypreference")
    @XmlElement(name = "activity")
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    protected List<Activity> activitypreference;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public List<Activity> getActivitypreference() {
		return activitypreference;
	}

	public void setActivitypreference(List<Activity> activitypreference) {
		this.activitypreference = activitypreference;
	}
	
	public Person() {
		//needed for XML
	}
	
	public static List<Person> getAllPeople() {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
	    List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
	    PersonActivitiesDao.instance.closeConnections(em);
	    return list;
	}

	public static Person savePerson(Person p) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return p;
	}
	
	public static Person updatePerson(Person p) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
		PersonActivitiesDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removePerson(Person p) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    PersonActivitiesDao.instance.closeConnections(em);
	}
	
	public static Person getPersonById(int personId) {
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		PersonActivitiesDao.instance.closeConnections(em);
		return p;
	}
	
	public Person saveActivity(Activity a) {
		if(this.activitypreference==null) {
			this.activitypreference = new ArrayList<Activity>();
		}
		this.activitypreference.add(a);
		EntityManager em = PersonActivitiesDao.instance.createEntityManager();
		Person p = Person.updatePerson(this);
		PersonActivitiesDao.instance.closeConnections(em);
		return p;
	}
}
