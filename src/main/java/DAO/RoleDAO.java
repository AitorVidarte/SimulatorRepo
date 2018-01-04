package DAO;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import Modelo.Role;
import hibernate.HibernateUtil;

public class RoleDAO {

	// For adding items in the Package table.
		public Role add(Role role) {
			Session session = HibernateUtil.createSessionFactory();
			session.beginTransaction();
			session.save(role);
			session.getTransaction().commit();
			session.close();
			return role;
		}

		// For deleting item from Package table.
		public Role delete(int id) {
			Session session = HibernateUtil.createSessionFactory();
			session.beginTransaction();
			Role role = session.get(Role.class, id);
			if (role != null) {
				session.delete(role);
			}
			session.getTransaction().commit();
			session.close();
			return role;
		}
		
		public Role edit(Role role, int id) {
			Session session = HibernateUtil.createSessionFactory();
			session.beginTransaction();
			Role role1 = session.get(Role.class, id);
			role1 = role;
			if(role1 != null) {
				session.update(role1);
			}
			session.getTransaction().commit();
			session.close();
			return role;
		}
		
		// For generating , executing hibernate select query and returns packages as a
		// list.
		@SuppressWarnings("unchecked")
		public List<Role> list() {
			Session session = HibernateUtil.createSessionFactory();
			session.beginTransaction();
			List<Role> roles = null;
			try {
				roles = (List<Role>) session.createQuery("from Role").list();

			} catch (HibernateException e) {
				e.printStackTrace();
				session.getTransaction().rollback();
			}
			session.getTransaction().commit();
			session.close();
			return roles;
		}
}
