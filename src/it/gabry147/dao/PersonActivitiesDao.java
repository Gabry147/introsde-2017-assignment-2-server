package it.gabry147.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

//class providing basic function to access data from db
public enum PersonActivitiesDao {

	instance;
	private EntityManagerFactory emf;
	
	private PersonActivitiesDao() {
		if (emf!=null) {
			emf.close();
		}
		emf = Persistence.createEntityManagerFactory("sde2_server");
	}

	public EntityManager createEntityManager() {
		try {
			return emf.createEntityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;    
	}

	public void closeConnections(EntityManager em) {
		em.close();
	}

	public EntityTransaction getTransaction(EntityManager em) {
		return em.getTransaction();
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

}
