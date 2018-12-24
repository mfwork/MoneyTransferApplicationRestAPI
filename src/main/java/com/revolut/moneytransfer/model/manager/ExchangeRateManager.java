package com.revolut.moneytransfer.model.manager;

import com.revolut.moneytransfer.model.ExchangeRate;
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

public class ExchangeRateManager {
	public ExchangeRateManager() {
	}

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static final Logger log = Logger.getLogger(ExchangeRateManager.class);

	public long create(ExchangeRate exchangeRate) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		long exchangeRateId = 0L;

		if (findByCurrency(exchangeRate.getCurreny()) != null) {
			return -1L;
		}
		try {
			transaction = session.beginTransaction();
			exchangeRateId = ((Long) session.save(exchangeRate)).longValue();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error adding new Exchange Rate, Transaction rollback", e);
		} finally {
			session.close();
		}
		return exchangeRateId;
	}

	public List<ExchangeRate> findAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		List<ExchangeRate> loExchangeRates = new ArrayList();
		try {
			transaction = session.beginTransaction();
			loExchangeRates = session.createQuery("From ExchangeRate").list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error afinding all Exchange Rate, Transaction rollback", e);
		} finally {
			session.close();
		}
		return loExchangeRates;
	}

	public void update(ExchangeRate exchangeRate) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(exchangeRate);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error updating Exchange Rate, Transaction rollback", e);
		} finally {
			session.close();
		}
	}

	public ExchangeRate findByID(long id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		ExchangeRate exchangeRate = null;
		try {
			transaction = session.beginTransaction();
			exchangeRate = (ExchangeRate) session.get(ExchangeRate.class, Long.valueOf(id));
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error finding Exchange Rate with id :" + id + ",Transaction rollback", e);
		} finally {
			session.close();
		}
		return exchangeRate;
	}

	public boolean delete(long exchangeRateId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean isDeleted = false;
		try {
			transaction = session.beginTransaction();
			ExchangeRate exchangeRate = (ExchangeRate) session.get(OwnTransfer.class, Long.valueOf(exchangeRateId));
			session.delete(exchangeRate);
			transaction.commit();
			isDeleted = true;
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error deleting Exchange Rate with id :" + exchangeRateId + ",Transaction rollback", e);
		} finally {
			session.close();
		}
		return isDeleted;
	}

	public ExchangeRate findByCurrency(String currency) {
		Session session = sessionFactory.openSession();
		ExchangeRate exchangeRate = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "from ExchangeRate where currency=:currency";
			Query query = session.createQuery(sql);
			query.setParameter("currency", currency);
			exchangeRate = (ExchangeRate) query.uniqueResult();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Can not find Exchange Rate for currency :" + currency + ", Transaction rollback", e);
		} finally {
			session.close();
		}
		return exchangeRate;
	}

	public void deleteAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "delete ExchangeRate";
			Query query = session.createQuery(sql);
			query.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error deleting ExchangeRates, Transaction rollback", e);
		} finally {
			session.close();
		}
	}
}
