package com.revolut.moneytransfer.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateUtil {
	private static SessionFactory sessionFactory = buildSessionFactory();

	public HibernateUtil() {
	}

	private static SessionFactory buildSessionFactory() {
		try {
			if (sessionFactory == null) {
				
//				Eclipse Env.
//				Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
				
				// for runnable jar 
				Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/resources/hibernate.cfg.xml"));
				
				StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
				serviceRegistryBuilder.applySettings(configuration.getProperties());
				ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			}
			return sessionFactory;
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

	public static void dropSchema() {
		Configuration configuration = new Configuration()
				.configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.drop(true, true);
	}

}
