package DAO;

/**
 * @file RaiDAO.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief RailDAO
 */


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import Modelo.Rail;
import hibernate.HibernateUtil;

public class RailDAO {

	/**
	 * Add the rail in the database.
	 * @param rail
	 * The rail
	 * @return rail
	 */
    public Rail add(Rail rail) {
	Session session = HibernateUtil.createSessionFactory();
	session.beginTransaction();
	session.save(rail);
	session.getTransaction().commit();
	session.close();
	return rail;
    }
    /**
	 * edit the rail in the database.
	 * @param rail
	 * The rail
	 */
    public void edit(Rail rail) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.update(rail);
		session.getTransaction().commit();
		session.close();
    }
}
