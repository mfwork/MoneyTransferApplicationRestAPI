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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.model.ExchangeRate;
import com.revolut.moneytransfer.utils.HibernateUtil;

public class ExchangeRateTest extends ServiceTest {
	
	@Test
	public void testGetAllExchangeRates() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = builder.setPath("/exchangerate/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		ExchangeRate[] exchangeRates = mapper.readValue(jsonString, ExchangeRate[].class);
		assertTrue(exchangeRates.length > 0);
	}

	@Test
	public void testGetExchangeRateDetailsByCurrency() throws URISyntaxException, ParseException, IOException {
		URI uri = builder.setPath("/exchangerate/USD").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		ExchangeRate exchangeRate = mapper.readValue(jsonString, ExchangeRate.class);
		assertNotNull(exchangeRate);
		assertTrue(exchangeRate.getBuyingRate() > 0);
		assertTrue(exchangeRate.getSellingRate() > 0);
	}

	@Test
	public void testCreate() throws URISyntaxException, ClientProtocolException, IOException {
		ExchangeRate exchangeRate = new ExchangeRate("CHF", 1.7d, 1.65d);
		String json = mapper.writeValueAsString(exchangeRate);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/exchangerate/create").build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*exchangerate/+[1-9]+"));
	}

	@Test
	public void testupdate() throws URISyntaxException, ClientProtocolException, IOException {
		ExchangeRate exchangeRate = new ExchangeRate("EGP", 1.8d, 1.66d);
		String json = mapper.writeValueAsString(exchangeRate);
		StringEntity entity = new StringEntity(json);

		URI uri = builder.setPath("/exchangerate/update").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String jsonString = EntityUtils.toString(response.getEntity());
		UserMessageDTO userMessageDTO = mapper.readValue(jsonString, UserMessageDTO.class);
		assertNotNull(userMessageDTO);
		assertTrue(userMessageDTO.getMessage().matches(".*exchangerate/[1-9]+"));
	}

}
