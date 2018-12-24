package com.revolut.moneytransfer.model.manager;

import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserManager {
	public UserManager() {
	}

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static final Logger log = Logger.getLogger(UserManager.class);

	public long create(User userEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		long userId = 0L;
		if (findByEmail(userEntity.getEmail()) != null)
			return -1L;
		try {
			transaction = session.beginTransaction();
			userId = ((Long) session.save(userEntity)).longValue();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error adding new user, Transaction rollback", e);
		} finally {
			session.close();
		}
		return userId;
	}

	public List<User> findAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		List<User> loUsers = new ArrayList();
		try {
			transaction = session.beginTransaction();
			loUsers = session.createQuery("From User").list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error afinding all users, Transaction rollback", e);
		} finally {
			session.close();
		}
		return loUsers;
	}

	public boolean update(User userEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean isUpdated = true;
		try {
			transaction = session.beginTransaction();
			session.update(userEntity);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			isUpdated = false;
			log.error("error updating user, Transaction rollback", e);
		} finally {
			session.close();
		}
		return isUpdated;
	}

	public User findByID(long userID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		User userEntity = null;
		try {
			transaction = session.beginTransaction();
			userEntity = (User) session.get(User.class, Long.valueOf(userID));
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error finding user with id :" + userID + ",Transaction rollback", e);
		} finally {
			session.close();
		}
		return userEntity;
	}

	public boolean delete(long userID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean isDeleted = false;
		try {
			transaction = session.beginTransaction();
			User user = (User) session.get(User.class, Long.valueOf(userID));
			session.delete(user);
			transaction.commit();
			isDeleted = true;
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error deleting user with id :" + userID + ",Transaction rollback", e);
		} finally {
			session.close();
		}
		return isDeleted;
	}

	public User findByEmail(String email) {
		Session session = sessionFactory.openSession();
		User user = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "from User where email=:email";
			Query query = session.createQuery(sql);
			query.setParameter("email", email);
			user = (User) query.uniqueResult();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error while retrieving User with mail :" + email + ", Transaction rollback", e);
		} finally {
			session.close();
		}
		return user;
	}

	public void deleteAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "delete User";
			Query query = session.createQuery(sql);
			query.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error deleting Users, Transaction rollback", e);
		} finally {
			session.close();
		}
	}
}
