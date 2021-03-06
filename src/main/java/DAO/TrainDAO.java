package DAO;

/**
 * @file TrainDAO.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief TrainDAO
 */


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import Modelo.Train;
import hibernate.HibernateUtil;

public class TrainDAO {
	SessionFactory sessionFactory;

	/**
	 * Edit the train in the database.
	 * @param train
	 * take one train for update
	 */
	public void edit(Train train) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.update(train);
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * Gets the train list from the database.
	 * @return trains
	 */
	@SuppressWarnings("unchecked")
	public List<Train> list() {
		//Session session = HibernateUtil.createSessionFactory();
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Train> trains = null;
		try {
			trains = (List<Train>) session.createQuery("from Train").list();

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		session.getTransaction().commit();
		session.close();
		return trains;
	}
}
