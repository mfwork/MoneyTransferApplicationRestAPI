package com.revolut.moneytransfer.services;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.revolut.moneytransfer.utils.HibernateUtil;

@RunWith(Suite.class)
@SuiteClasses({ AccountServiceTest.class, ExchangeRateTest.class, TransferServiceTest.class, UserServiceTest.class })
public class AllTests extends ServiceTest {

	@AfterClass
	public static void stopServer() throws Exception {
		server.stop();
		HibernateUtil.dropSchema();
	}
}
