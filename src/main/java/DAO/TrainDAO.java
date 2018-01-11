package DAO;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import Modelo.Train;
import hibernate.HibernateUtil;

public class TrainDAO {
	SessionFactory sessionFactory;
	// For adding items in the Train table.
	public Train add(Train train) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.save(train);
		session.getTransaction().commit();
		session.close();
		return train;
	}

	// For deleting item from Train table.
	public Train delete(int id) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		Train train = (Train) session.load(Train.class, id);
		if (null != train) {
			session.delete(train);
		}
		session.getTransaction().commit();
		session.close();
		return train;
	}

	public void edit(Train train) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.update(train);
		session.getTransaction().commit();
		session.close();
	}
	
	// For generating , executing hibernate select query and returns trains as a
	// list.
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
