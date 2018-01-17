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
		session.save(paquete);
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
