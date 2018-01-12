package DAO;


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import Modelo.Package;

import hibernate.HibernateUtil;

public class PackageDAO {
	
	// For adding items in the Package table.
	public Package add(Package paquete) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		System.out.println(paquete.getDestination().getStationID());
		session.save(paquete);
		session.getTransaction().commit();
		session.close();
		return paquete;
	}

	// For deleting item from Package table.
	public Package delete(int id) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		Package paquete = session.get(Package.class, id);
		if (paquete != null) {
			session.delete(paquete);
		}
		session.getTransaction().commit();
		session.close();
		return paquete;
	}
	
	public void edit(Package paquete) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();	
		session.update(paquete);
		session.getTransaction().commit();
		session.close();
	}
	
	// For generating , executing hibernate select query and returns packages as a
	// list.
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
