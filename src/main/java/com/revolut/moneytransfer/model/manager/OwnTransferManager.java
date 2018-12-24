package com.revolut.moneytransfer.model.manager;

import com.revolut.moneytransfer.model.OwnTransfer;
import com.revolut.moneytransfer.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class OwnTransferManager {
	public OwnTransferManager() {
	}

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static final Logger log = Logger.getLogger(OwnTransferManager.class);

	public long create(OwnTransfer ownTransferEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		Long OwnTransferID = null;
		try {
			transaction = session.beginTransaction();
			OwnTransferID = (Long) session.save(ownTransferEntity);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error adding new Own Transfer, Transaction rollback", e);
		} finally {
			session.close();
		}
		return OwnTransferID.longValue();
	}

	public List<OwnTransfer> findAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		List<OwnTransfer> loOwnTransfers = new ArrayList();
		try {
			transaction = session.beginTransaction();
			loOwnTransfers = session.createQuery("From OwnTransfer").list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error afinding all Own Transfers, Transaction rollback", e);
		} finally {
			session.close();
		}
		return loOwnTransfers;
	}

	public void update(OwnTransfer ownTransferEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(ownTransferEntity);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error updating Own Transfer, Transaction rollback", e);
		} finally {
			session.close();
		}
	}

	public OwnTransfer findByID(long id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		OwnTransfer ownTransferEntity = null;
		try {
			transaction = session.beginTransaction();
			ownTransferEntity = (OwnTransfer) session.get(OwnTransfer.class, Long.valueOf(id));
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error finding Own Transfer with id :" + id + ",Transaction rollback", e);
		} finally {
			session.close();
		}
		return ownTransferEntity;
	}

	public boolean delete(long OwnTransferID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean isDeleted = false;
		try {
			transaction = session.beginTransaction();
			OwnTransfer OwnTransfer = (OwnTransfer) session.get(OwnTransfer.class, Long.valueOf(OwnTransferID));
			session.delete(OwnTransfer);
			transaction.commit();
			isDeleted = true;
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error deleting Own Transfer with id :" + OwnTransferID + ",Transaction rollback", e);
		} finally {
			session.close();
		}
		return isDeleted;
	}

	public List<OwnTransfer> findByUserId(long userId) {
		Session session = sessionFactory.openSession();
		List<OwnTransfer> loOwnTransfer = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "from OwnTransfer where userId=:userId";
			Query query = session.createQuery(sql);
			query.setParameter("userId", Long.valueOf(userId));
			loOwnTransfer = query.list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Can not find transactions for user :" + userId + ", Transaction rollback", e);
		} finally {
			session.close();
		}
		return loOwnTransfer;
	}

	public void deleteAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "delete OwnTransfer";
			Query query = session.createQuery(sql);
			query.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error deleting OwnTransfers, Transaction rollback", e);
		} finally {
			session.close();
		}
	}
}
