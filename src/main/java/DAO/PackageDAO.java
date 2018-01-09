package DAO;

import java.util.ArrayList;
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
	
	public Package edit(Package paquete, int id) {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		Package paquete1 = session.get(Package.class, id);
		paquete1 = paquete;
		if(paquete1 != null) {
			session.update(paquete1);
		}
		session.getTransaction().commit();
		session.close();
		return paquete;
	}
	
	// For generating , executing hibernate select query and returns packages as a
	// list.
	@SuppressWarnings("unchecked")
	public ArrayList<Package> packageListInBBDD() {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		ArrayList<Package> packages = null;
		try {
			packages = (ArrayList<Package>) session.createQuery("from Package").list();

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		session.getTransaction().commit();
		session.close();
		return packages;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Package> toSendPackageListInBBDD() {
		Session session = HibernateUtil.createSessionFactory();
		session.beginTransaction();
		ArrayList<Package> packages = null;
		try {
			packages = (ArrayList<Package>) session.createQuery("from Package where packageState = 0").list();

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		session.getTransaction().commit();
		session.close();
		return packages;
	}
}
