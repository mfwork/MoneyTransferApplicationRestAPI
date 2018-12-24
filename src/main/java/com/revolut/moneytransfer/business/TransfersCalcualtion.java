package com.revolut.moneytransfer.business;

import com.revolut.moneytransfer.dto.TransactionDTO;
import com.revolut.moneytransfer.exception.BuisnessException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.ExchangeRate;
import com.revolut.moneytransfer.model.OwnTransfer;
import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.model.manager.AccountManager;
import com.revolut.moneytransfer.model.manager.ExchangeRateManager;
import com.revolut.moneytransfer.model.manager.UserManager;
import com.revolut.moneytransfer.utils.HibernateUtil;
import com.revolut.moneytransfer.utils.Utils;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransfersCalcualtion {
	static final Logger log = Logger.getLogger(TransfersCalcualtion.class);

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static final AccountManager accountManager = new AccountManager();
	private static final UserManager userManager = new UserManager();
	private static final ExchangeRateManager exchangeRateManager = new ExchangeRateManager();

	public TransfersCalcualtion() {
	}

	public String ownTransferTransaction(TransactionDTO transactionDTO) throws BuisnessException, IOException {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		String transactioId = "";

		String baseCurrency = Utils.loadAppConfig().getProperty("appplicaton.baseCurrency");
		try {
			transaction = session.beginTransaction();
			Account fromAccountDetails = accountManager.findByAccNo(transactionDTO.getFromAccount());
			Account toAccountDetails = accountManager.findByAccNo(transactionDTO.getToAccount());

			double exchangeRate = getExchangeRate(fromAccountDetails.getAccountCurrency(),
					toAccountDetails.getAccountCurrency(), transactionDTO.getTransferCurrency());
			double debitAmount = 0.0D;

			if ((baseCurrency.equalsIgnoreCase(transactionDTO.getTransferCurrency())) || (fromAccountDetails
					.getAccountCurrency().equalsIgnoreCase(transactionDTO.getTransferCurrency()))) {
				debitAmount = transactionDTO.getAmount().doubleValue();
			} else if ((baseCurrency.equalsIgnoreCase(fromAccountDetails.getAccountCurrency()))
					&& (transactionDTO.getTransferCurrency().equalsIgnoreCase(toAccountDetails.getAccountCurrency()))) {
				debitAmount = transactionDTO.getAmount().doubleValue() / exchangeRate;
			} else {
				debitAmount = transactionDTO.getAmount().doubleValue() * exchangeRate;
			}

			if ((!fromAccountDetails.getAccountCurrency().equalsIgnoreCase(transactionDTO.getTransferCurrency()))
					&& (!fromAccountDetails.getAccountCurrency().equalsIgnoreCase(baseCurrency))) {
				debitAmount = debitAmount
						* exchangeRateManager.findByCurrency(fromAccountDetails.getAccountCurrency()).getSellingRate();
			}

			fromAccountDetails.setAccountBalance(fromAccountDetails.getAccountBalance() - debitAmount);

			if (transactionDTO.getTransferCurrency().equalsIgnoreCase(toAccountDetails.getAccountCurrency())) {
				toAccountDetails.setAccountBalance(
						toAccountDetails.getAccountBalance() + transactionDTO.getAmount().doubleValue());
			} else {
				double creditAmount = 0.0D;
				double creditExchangeRate = getExchangeRate(fromAccountDetails.getAccountCurrency(),
						toAccountDetails.getAccountCurrency(), transactionDTO.getTransferCurrency());
				if ((!transactionDTO.getTransferCurrency().equalsIgnoreCase(baseCurrency))
						&& (toAccountDetails.getAccountCurrency().equals(baseCurrency))) {
					creditAmount = transactionDTO.getAmount().doubleValue() / creditExchangeRate;
				} else {
					creditAmount = transactionDTO.getAmount().doubleValue() / creditExchangeRate;
				}
				toAccountDetails.setAccountBalance(toAccountDetails.getAccountBalance() + creditAmount);
			}

			OwnTransfer ownTransferEntity = new OwnTransfer();
			ownTransferEntity.setAmount(transactionDTO.getAmount());
			ownTransferEntity.setTransferCurrency(transactionDTO.getTransferCurrency());
			ownTransferEntity.setFromAccount(fromAccountDetails.getAccountNumber());
			ownTransferEntity.setToAccount(toAccountDetails.getAccountNumber());
			ownTransferEntity.setBenficaryName("Own-Transfer");
			ownTransferEntity.setExchangeRate(Double.valueOf(exchangeRate));
			ownTransferEntity.setFromAccountCurrency(fromAccountDetails.getAccountCurrency());
			ownTransferEntity.setToAccountCurrency(toAccountDetails.getAccountCurrency());
			ownTransferEntity.setTransferCurrency(transactionDTO.getTransferCurrency());

			User user = null;
			if (transactionDTO.getUserId() != 0L) {
				user = userManager.findByID(transactionDTO.getUserId());
			} else {
				user = userManager.findByID(fromAccountDetails.getUser().getUserId());
			}

			ownTransferEntity.setUser(user);

			session.update("Account", toAccountDetails);
			session.update("Account", fromAccountDetails);
			session.save(ownTransferEntity);
			session.getTransaction().commit();

			transactioId = String.valueOf(ownTransferEntity.getId());
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error creating transfer, Transaction rollback", e);
		} finally {
			session.close();
		}
		return transactioId;
	}

	private double getExchangeRate(String fromCurr, String toCurr, String transCurrency)
			throws BuisnessException, IOException {
		String baseCurrency = Utils.loadAppConfig().getProperty("appplicaton.baseCurrency");
		ExchangeRate exchangeRateEntity = null;
		double exchanceRate = 1.0D;

		if (!fromCurr.equalsIgnoreCase(toCurr)) {
			if ((baseCurrency.equalsIgnoreCase(fromCurr)) && (baseCurrency.equalsIgnoreCase(transCurrency))) {
				exchangeRateEntity = exchangeRateManager.findByCurrency(toCurr);
				if (exchangeRateEntity == null) {
					log.error("Exchange rate not exit, kindly add exchange rate for:" + toCurr);
					throw new BuisnessException("Exchange rate not exit, kindly add exchange rate for:" + toCurr);
				}
				exchanceRate = exchangeRateEntity.getSellingRate();
			} else {
				if (toCurr.equalsIgnoreCase(transCurrency)) {
					exchangeRateEntity = exchangeRateManager.findByCurrency(toCurr);
				} else
					exchangeRateEntity = exchangeRateManager.findByCurrency(transCurrency);
				if (exchangeRateEntity == null) {
					log.error("Exchange rate not exit, kindly add exchange rate for:" + toCurr);
					throw new BuisnessException("Exchange rate not exit, kindly add exchange rate for:" + toCurr);
				}
				exchanceRate = exchangeRateEntity.getSellingRate();
			}
		} else {
			exchanceRate = 1.0D;
		}
		return exchanceRate;
	}

	public String localTransferTransaction(TransactionDTO transactionDTO) throws BuisnessException {
		return null;
	}

	public String internationalTransferTransaction(TransactionDTO transactionDTO) throws BuisnessException {
		return null;
	}

	public String internalTransferTransaction(TransactionDTO transactionDTO) throws BuisnessException {
		return null;
	}
}
