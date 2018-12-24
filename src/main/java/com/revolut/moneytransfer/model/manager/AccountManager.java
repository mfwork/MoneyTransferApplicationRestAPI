package com.revolut.moneytransfer.model.manager;

import com.revolut.moneytransfer.dto.AccountDTO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.utils.HibernateUtil;
import com.revolut.moneytransfer.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class AccountManager {
	private static final Logger log = Logger.getLogger(AccountManager.class);
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public AccountManager() {
	}

	public long create(Account accountEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		long id = 0L;

		if (accountEntity.getAccountNumber() == null) {
			accountEntity.setAccountNumber(Utils.generateRandomAccount(accountEntity.getAccountCurrency()));
		}
		try {
			transaction = session.beginTransaction();
			id = ((Long) session.save(accountEntity)).longValue();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error adding new account, Transaction rollback", e);
		} finally {
			session.close();
		}
		return id;
	}

	public boolean delete(long accountID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean isDeleted = false;
		try {
			transaction = session.beginTransaction();
			Account accountEntity = (Account) session.get(Account.class, Long.valueOf(accountID));
			session.delete(accountEntity);
			transaction.commit();
			isDeleted = true;
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error deleting account, Transaction rollback", e);
		} finally {
			session.close();
		}
		return isDeleted;
	}

	public boolean update(Account accountEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean isUpdated = true;
		try {
			transaction = session.beginTransaction();
			session.update(accountEntity);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			isUpdated = false;
			log.error("error updating account, Transaction rollback", e);
		} finally {
			session.close();
		}
		return isUpdated;
	}

	public List<Account> findAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		List<Account> loAccountEntity = new ArrayList();
		try {
			transaction = session.beginTransaction();
			String sql = "from Account";
			Query query = session.createQuery(sql);
			loAccountEntity = query.list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error findAll account, Transaction rollback", e);
		} finally {
			session.close();
		}
		return loAccountEntity;
	}

	public Account findByID(long id) {
		Session session = sessionFactory.openSession();
		Account account = null;
		Transaction transaction = null;
		AccountDTO accountDTO = new AccountDTO();
		try {
			transaction = session.beginTransaction();
			account = (Account) session.get(Account.class, Long.valueOf(id));
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error finding account with id :" + id + ", Transaction rollback", e);
		} finally {
			session.close();
		}

		return account;
	}

	public Account findByAccNo(String accountNumber) {
		Session session = sessionFactory.openSession();
		Account accountEntity = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "from Account where accountNumber=:accountNumber";
			Query query = session.createQuery(sql);
			query.setParameter("accountNumber", accountNumber);
			accountEntity = (Account) query.uniqueResult();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error finding account no :" + accountNumber + ", Transaction rollback", e);
		} finally {
			session.close();
		}
		return accountEntity;
	}

	public List<Account> findByUserId(long userId) {
		Session session = sessionFactory.openSession();
		List<Account> loAccount = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "from Account where userId=:userId";
			Query query = session.createQuery(sql);
			query.setParameter("userId", Long.valueOf(userId));
			loAccount = query.list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error finding accounts for user :" + userId + ", Transaction rollback", e);
		} finally {
			session.close();
		}
		return loAccount;
	}

	public void deleteAll() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			String sql = "delete Account";
			Query query = session.createQuery(sql);
			query.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("error deleting accounts, Transaction rollback", e);
		} finally {
			session.close();
		}
	}
}
