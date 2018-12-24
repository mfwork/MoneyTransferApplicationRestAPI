package com.revolut.moneytransfer.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revolut.moneytransfer.dto.AccountDTO;
import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.utils.HibernateUtil;

public class AccountServiceTest extends ServiceTest {

	@Test
	public void testGetAllAccounts() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/account/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		Account[] accounts = mapper.readValue(jsonString, Account[].class);
		assertTrue(accounts.length > 0);
	}

	@Test
	public void testGetAccountDetailsByID() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/account/1").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		AccountDTO accountDetails = mapper.readValue(jsonString, AccountDTO.class);
		assertNotNull(accountDetails);
		assertTrue(accountDetails.getAccountBalance() > 0);
	}

	@Test
	public void testGetAccountDetailsByAccountNumber() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/account/PLN22029551373485/details").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		AccountDTO accountDetails = mapper.readValue(jsonString, AccountDTO.class);
		assertNotNull(accountDetails);
		assertTrue(accountDetails.getAccountNumber().equals("PLN22029551373485"));
		assertTrue(accountDetails.getAccountCurrency().equals("PLN"));
		assertTrue(accountDetails.getAccountBalance() == 1000);
	}

	@Test
	public void testCreateAccountWithGivenAccountNumber()
			throws URISyntaxException, ClientProtocolException, IOException {

		// 1 is userId, account should be assigned to user
		AccountDTO account = new AccountDTO(1, "USD", "USD123456789007878", Double.valueOf(50));
		String json = mapper.writeValueAsString(account);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/account/create").build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*account/[1-9]+"));
	}

	@Test
	public void testCreateAccountWithoutGivenAccountNumber()
			throws URISyntaxException, ClientProtocolException, IOException {

		// 1 is userId, account should be assigned to user
		AccountDTO account = new AccountDTO(1, "USD", null, Double.valueOf(50));
		String json = mapper.writeValueAsString(account);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/account/create").build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*account/[1-9]+"));
	}

	@Test
	public void testDeleteAccount() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/account/delete/1").build();
		HttpDelete request = new HttpDelete(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().equals("Account is deleted"));
	}

	@Test
	public void testUpdateAccount() throws URISyntaxException, ClientProtocolException, IOException {
		// 1 is userId, account should be assigned to user
		AccountDTO account = new AccountDTO(3, 1, "USD", "USD123456789007866", Double.valueOf(50));
		String json = mapper.writeValueAsString(account);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/account/update").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*account/[1-9]+"));
	}

}
