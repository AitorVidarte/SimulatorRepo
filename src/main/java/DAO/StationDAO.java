package DAO;

/**
 * @file StationDAO.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief StationDAO
 */


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import Modelo.Station;
import hibernate.HibernateUtil;

public class StationDAO {

	/**
	 * Add the station in the database.
	 * @param station
	 * The station
	 * @return station
	 */
	public Station add(Station station) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.save(station);
		session.getTransaction().commit();
		session.close();
		return station;
	}

	/**
	 * edit the station in the database.
	 * @param station
	 * The station
	 * @return station
	 */
	public Station edit(Station station) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.update(station);
		session.getTransaction().commit();
		session.close();
		return station;
	}

	/**
	 * Gets the station list from the database.
	 * @return stations
	 */
	@SuppressWarnings("unchecked")
	public List<Station> list() {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		List<Station> stations = null;
		try {
			stations = (List<Station>) session.createQuery("from Station").list();

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		session.getTransaction().commit();
		session.close();
		return stations;
	}
}
