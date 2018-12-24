package com.revolut.moneytransfer.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.revolut.moneytransfer.dto.AccountDTO;
import com.revolut.moneytransfer.dto.TransactionDTO;
import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.model.OwnTransfer;
import com.revolut.moneytransfer.utils.HibernateUtil;

public class TransferServiceTest extends ServiceTest {
	
	@Test
	public void testGetAllOwnTransfers() throws URISyntaxException, ParseException, IOException {
		URI uri = builder.setPath("/transfer/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		OwnTransfer[] ownTransfers = mapper.readValue(jsonString, OwnTransfer[].class);
		assertTrue(ownTransfers.length > 0);
	}

	@Test
	public void testGetOwnTransferDetailsByID()
			throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		URI uri = builder.setPath("/transfer/1").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		TransactionDTO transactionDTO = mapper.readValue(jsonString, TransactionDTO.class);
		assertNotNull(transactionDTO);
		assertTrue(transactionDTO.getAmount() == 25);
	}

	@Test
	public void testGetOwnTransfersByuserId() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/transfer/usertrans/1").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		OwnTransfer[] ownTransfer = mapper.readValue(jsonString, OwnTransfer[].class);
		assertNotNull(ownTransfer);
		assertTrue(ownTransfer.length > 0);
	}

	@Test
	public void testCreate() throws ParseException, IOException, URISyntaxException {
		// 1 is userId, account should be assigned to user
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setUserId(2);
		transactionDTO.setAmount(new Double(25));
		transactionDTO.setFromAccount("USD22029551373484");
		transactionDTO.setToAccount("PLN22029551373485");
		transactionDTO.setTransferCurrency("USD");
		String json = mapper.writeValueAsString(transactionDTO);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/transfer/create").build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*transfer/[1-9]+"));

		// New balance should be 500 - 25 = 475 USD for Debit account
		testNewBalnceForDebitAccount();

		// New balance should be 1000 - (25/0.27) = 1092.5925925925926 PLN for credit
		// account // selling exchange 1 PLN = 0.27 USD
		testNewBalnceForCreaditAccount();
	}

	private void testNewBalnceForDebitAccount() throws ParseException, IOException, URISyntaxException {
		URI uri = builder.setPath("/account/USD22029551373484/details").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		AccountDTO accountDetails = mapper.readValue(jsonString, AccountDTO.class);
		assertNotNull(accountDetails);
		;
		assertTrue(accountDetails.getAccountBalance() == 475);
	}

	private void testNewBalnceForCreaditAccount() throws ParseException, IOException, URISyntaxException {
		URI uri = builder.setPath("/account/PLN22029551373485/details").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		AccountDTO accountDetails = mapper.readValue(jsonString, AccountDTO.class);
		assertNotNull(accountDetails);
		assertTrue(accountDetails.getAccountBalance() == 1092.5925925925926);
	}
}
