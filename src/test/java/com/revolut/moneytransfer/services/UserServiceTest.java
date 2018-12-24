package com.revolut.moneytransfer.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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

import com.revolut.moneytransfer.dto.UserDTO;
import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.utils.HibernateUtil;

public class UserServiceTest extends ServiceTest {
	
	@Test
	public void testGetAllUsers() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/user/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		User[] users = mapper.readValue(jsonString, User[].class);
		assertTrue(users.length > 0);
	}

	@Test
	public void testGetUserByID() throws URISyntaxException, ParseException, IOException {
		URI uri = builder.setPath("/user/1").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		User user = mapper.readValue(jsonString, User.class);
		assertNotNull(user);
		assertNotNull(user.getFirstName());
		assertTrue(user.getUserId() == 1);

	}

	@Test
	public void testCreateUser() throws URISyntaxException, ClientProtocolException, IOException {
		// long id is generated automatically 0 for long value used in update by id
		UserDTO userDTO = new UserDTO(0, "test1", "test1", "test@test.com");
		String json = mapper.writeValueAsString(userDTO);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/user/create").build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*user/[1-9]+"));
	}

	@Test
	public void testGetUserAccounts() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/user/1/accounts").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		Account[] accounts = mapper.readValue(jsonString, Account[].class);
		assertTrue(accounts.length > 0);
	}

	@Test
	public void testDeleteUser() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/user/delete/2").build();
		HttpDelete request = new HttpDelete(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().equals("User is deleted"));
	}

	@Test
	public void testUpdateUser() throws URISyntaxException, ClientProtocolException, IOException {
		UserDTO userDTO = new UserDTO(1, "test1-update", "test1-update", "testupdated@test.com");
		String json = mapper.writeValueAsString(userDTO);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/user/update").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*user/[1-9]+"));
	}

}
