package DAO;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import Modelo.Rail;
import hibernate.HibernateUtil;

public class RailDAO {

    // For adding items in the Package table.
    public Rail add(Rail rail) {
	Session session = HibernateUtil.createSessionFactory();
	session.beginTransaction();
	session.save(rail);
	session.getTransaction().commit();
	session.close();
	return rail;
    }

    // For deleting item from Package table.
    public Rail delete(int id) {
	Session session = HibernateUtil.createSessionFactory();
	session.beginTransaction();
	Rail rail = session.get(Rail.class, id);
	if (rail != null) {
	    session.delete(rail);
	}
	session.getTransaction().commit();
	session.close();
	return rail;
    }

    public Rail edit(Rail rail, int id) {
	Session session = HibernateUtil.createSessionFactory();
	session.beginTransaction();
	Rail rail1 = session.get(Rail.class, id);
	rail1 = rail;
	if (rail1 != null) {
	    session.update(rail1);
	}
	session.getTransaction().commit();
	session.close();
	return rail;
    }

    // For generating , executing hibernate select query and returns packages as a
    // list.
    @SuppressWarnings("unchecked")
    public List<Rail> list() {
	Session session = HibernateUtil.createSessionFactory();
	session.beginTransaction();
	List<Rail> rails = null;
	try {
	    rails = (List<Rail>) session.createQuery("from Rail").list();

	} catch (HibernateException e) {
	    e.printStackTrace();
	    session.getTransaction().rollback();
	}
	session.getTransaction().commit();
	session.close();
	return rails;
    }
}
