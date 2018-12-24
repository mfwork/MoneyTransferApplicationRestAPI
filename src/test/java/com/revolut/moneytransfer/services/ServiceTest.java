package com.revolut.moneytransfer.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.moneytransfer.business.TransfersCalcualtion;
import com.revolut.moneytransfer.dto.TransactionDTO;
import com.revolut.moneytransfer.exception.BuisnessException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.ExchangeRate;
import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.utils.HibernateUtil;

public class ServiceTest {

	protected static ServletContextHandler context;
	protected static Server server = null;
	protected static HttpClient client;
	protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:9999");
	protected ObjectMapper mapper = new ObjectMapper();

	@BeforeClass
	public static void setup() throws Exception {
		startServer();
		client = HttpClients.createDefault();
		loadTestingData();
	}
	
	protected static void startServer() throws Exception {
		if (server == null) {
			server = new Server(9999);
			context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
			ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
			servletHolder.setInitParameter("jersey.config.server.provider.classnames",
					UserService.class.getCanonicalName() + "," + AccountService.class.getCanonicalName() + ","
							+ TransferService.class.getCanonicalName() + ","
							+ ExchangeRateService.class.getCanonicalName());
			server.start();
		}
	}

	protected static void loadTestingData() throws BuisnessException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();

		User user1 = new User("Joan", "Smith", "user1@mail.com");
		User user2 = new User("Agata", "kacik", "user2@mail.com");

		Set<Account> user1Accounts = new HashSet<Account>();
		Account user1_account1 = new Account("USD22029551373481", "USD", 500);
		Account user1_account2 = new Account("PLN22029551373482", "PLN", 1000);
		Account user1_account3 = new Account("USD22029551373483", "USD", 500);

		user1_account1.setUser(user1);
		user1_account2.setUser(user1);
		user1_account3.setUser(user1);

		user1Accounts.add(user1_account1);
		user1Accounts.add(user1_account2);
		user1Accounts.add(user1_account3);

		Set<Account> user2Accounts = new HashSet<Account>();
		Account user2_account1 = new Account("USD22029551373484", "USD", 500);
		Account user2_account2 = new Account("PLN22029551373485", "PLN", 1000);
		Account user2_account3 = new Account("EGP22029551373486", "EGP", 500);

		user2_account1.setUser(user2);
		user2_account2.setUser(user2);
		user2_account3.setUser(user2);

		user2Accounts.add(user2_account1);
		user2Accounts.add(user2_account2);
		user2Accounts.add(user2_account3);

		user1.setAccounts(user1Accounts);
		user2.setAccounts(user2Accounts);

		// curreny - sellingBuying - buyingRate
		// USD - 0.27 - 0.25 means as Bank or Application buy USD with 0.25 and sell
		// with 0.27
		ExchangeRate er_PLN = new ExchangeRate("PLN", 1, 1); // base application or bank currency
		ExchangeRate er_USD = new ExchangeRate("USD", 0.27, 0.25); // means from PLN(base currency) to USD
		ExchangeRate er_EGP = new ExchangeRate("EGP", 4.74, 4.7); // means from PLN(base currency) to USD
		ExchangeRate er_EUR = new ExchangeRate("EUR", 0.23, 0.20);

		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			// Exchange rate setup
			session.save(er_PLN);
			session.save(er_USD);
			session.save(er_EGP);
			session.save(er_EUR);

			// User and account setup
			session.save(user1);
			session.save(user2);

			session.getTransaction().commit();
			loadTransferDataSample();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
		} finally {
			session.close();
		}

	}

	private static void loadTransferDataSample() throws BuisnessException, IOException {

		TransfersCalcualtion transfersCalcualtion = new TransfersCalcualtion();
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setUserId(1);
		transactionDTO.setAmount(new Double(25));
		transactionDTO.setFromAccount("USD22029551373481");
		transactionDTO.setToAccount("USD22029551373483");
		transactionDTO.setTransferCurrency("USD");
		transfersCalcualtion.ownTransferTransaction(transactionDTO);
	}
}
