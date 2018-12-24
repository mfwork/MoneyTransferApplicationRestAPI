package com.revolut.moneytransfer;

import com.revolut.moneytransfer.exception.JsonProcessingExceptionMapper;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.ExchangeRate;
import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.services.AccountService;
import com.revolut.moneytransfer.services.ExchangeRateService;
import com.revolut.moneytransfer.services.TransferService;
import com.revolut.moneytransfer.services.UserService;
import com.revolut.moneytransfer.utils.HibernateUtil;
import com.revolut.moneytransfer.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class JDKHttpServer {
	public static final int port = 9998;
	public static final String host = "http://localhost/";
	static final Logger log = Logger.getLogger(JDKHttpServer.class);

	public JDKHttpServer() {
	}

	public static void main(String[] args) throws Exception {
		
		// for runnable jar
		Utils.loadLog4jConfiguration();
		
		Server server = new Server(9998);
		ServletContextHandler context = new ServletContextHandler(1);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
				UserService.class.getCanonicalName() + "," + AccountService.class.getCanonicalName() + ","
						+ TransferService.class.getCanonicalName() + "," + ExchangeRateService.class.getCanonicalName()
						+ "," + JsonProcessingExceptionMapper.class.getCanonicalName());
		server.start();
		
		log.info("#########################################################################################");
		log.info("####################### JDKHttpServer Server started successfully #######################");
		log.info("#########################################################################################");
		
		createDataSample();
	}

	private static void createDataSample() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<User> loUsers = new ArrayList();
		User user1 = new User("Joan", "Smith", "user1@mail.com");
		User user2 = new User("Agata", "kacik", "user2@mail.com");

		Set<Account> user1Accounts = new HashSet();
		Account user1_account1 = new Account("USD22029551373481", "USD", 500.0D);
		Account user1_account2 = new Account("PLN22029551373482", "PLN", 1000.0D);
		Account user1_account3 = new Account("USD22029551373483", "USD", 500.0D);

		user1_account1.setUser(user1);
		user1_account2.setUser(user1);
		user1_account3.setUser(user1);

		user1Accounts.add(user1_account1);
		user1Accounts.add(user1_account2);
		user1Accounts.add(user1_account3);

		Set<Account> user2Accounts = new HashSet();
		Account user2_account1 = new Account("USD22029551373484", "USD", 500.0D);
		Account user2_account2 = new Account("PLN22029551373485", "PLN", 1000.0D);
		Account user2_account3 = new Account("EGP22029551373486", "EGP", 500.0D);

		user2_account1.setUser(user2);
		user2_account2.setUser(user2);
		user2_account3.setUser(user2);

		user2Accounts.add(user2_account1);
		user2Accounts.add(user2_account2);
		user2Accounts.add(user2_account3);

		user1.setAccounts(user1Accounts);
		user2.setAccounts(user2Accounts);

		ExchangeRate er_PLN = new ExchangeRate("PLN", 1.0D, 1.0D);
		ExchangeRate er_USD = new ExchangeRate("USD", 0.27D, 0.25D);
		ExchangeRate er_EGP = new ExchangeRate("EGP", 4.74D, 4.7D);
		ExchangeRate er_EUR = new ExchangeRate("EUR", 0.23D, 0.2D);

		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			session.save(er_PLN);
			session.save(er_USD);
			session.save(er_EGP);
			session.save(er_EUR);

			session.save(user1);
			session.save(user2);

			session.getTransaction().commit();
			
			System.out.println("######### Server is Started and data sample loaded successfully ######### " );
			
			log.info("#########################################################################################");
			log.info("#######################      Sample data loaded successfully      #######################");
			log.info("#########################################################################################");
			
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			log.error("Error aloading data sample, Transaction rollback", e);
		} finally {
			session.close();
		}
	}
}
