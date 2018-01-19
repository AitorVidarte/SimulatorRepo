package DAO;

/**
 * @file PackageDao.java
 * @author Aitor,Xanti and Alex
 * @date 3/12/2017
 * @brief PackageDAO
 */


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.sonarsource.scanner.api.internal.cache.Logger;

import Modelo.Package;
import hibernate.HibernateUtil;

public class PackageDAO {
	
	/**
	 * Add the package in database.
	 * @param paquete
	 * The package
	 * @return package
	 */
	
	public Package add(Package paquete) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		session.save(paquete);
		session.getTransaction().commit();
		session.close();
		return paquete;
	}

	/**
	 * edit the package in database.
	 * @param paquete
	 * The package
	*/
	public void edit(Package paquete) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();	
		session.update(paquete);
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * take the packages from database.
	 * @return  packages
	 */
	@SuppressWarnings("unchecked")
	public List<Package> packageListInBBDD() {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		List<Package> packages = null;
		try {
			packages = session.createQuery("from Package").list();

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		session.getTransaction().commit();
		session.close();
		return packages;
	}
	
	/**
	 * take the send packages from database.
	 * @return package
	 */
	@SuppressWarnings("unchecked")
	public List<Package> toSendPackageListInBBDD() {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		List<Package> packages = null;
		try {
			packages =  session.createQuery("from Package where packageState = 3").list();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		session.getTransaction().commit();
		session.close();
		return packages;
	}
}
