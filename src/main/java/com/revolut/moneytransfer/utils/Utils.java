package com.revolut.moneytransfer.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Utils {
	public Utils() {
	}

	private static Properties properties = new Properties();
	static Logger log = Logger.getLogger(Utils.class);

	public static Properties loadConfig(String fileName) throws IOException {
		properties = new Properties();
		properties.load(Utils.class.getResourceAsStream(fileName));
		return properties;
	}

	public static Properties loadAppConfig() throws IOException {
//		loadConfig("/appconfig.properties"); 
		loadConfig("/resources/appConfig.properties"); //runnable jar 
		
		return properties;
	}

	public static String generateRandomDigit() {
		long random = (long) (Math.random() * 1.0E14D);
		return String.valueOf(random);
	}

	public static String generateRandomAccount(String currrnecy) {
		return currrnecy + generateRandomDigit();
	}
	
	// Runnable jar
	public static void loadLog4jConfiguration() throws IOException {
		DOMConfigurator.configure(Utils.class.getResource("/resources/log4j.xml"));
		log.debug("Log4j appender configuration is successful !!");
	}

}
