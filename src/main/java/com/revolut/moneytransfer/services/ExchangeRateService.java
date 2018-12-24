package com.revolut.moneytransfer.services;

import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.exception.BuisnessException;
import com.revolut.moneytransfer.model.ExchangeRate;
import com.revolut.moneytransfer.model.manager.ExchangeRateManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/exchangerate")
@Produces({ "application/json" })
public class ExchangeRateService {
	private static final Logger log = Logger.getLogger(ExchangeRateService.class);
	private static final ExchangeRateManager exchangeRateManager = new ExchangeRateManager();

	public ExchangeRateService() {
	}

	@GET
	@Path("/all")
	public Response getAllExchangeRates() throws BuisnessException {
		List<ExchangeRate> loExchangeRateEntity = exchangeRateManager.findAll();
		return Response.ok(loExchangeRateEntity, "application/json").build();
	}

	@GET
	@Path("/{currency}")
	public Response getExchangeRateDetailsByID(@PathParam("currency") String currency) throws BuisnessException {
		ExchangeRate exchangeRate = exchangeRateManager.findByCurrency(currency);
		return Response.ok(exchangeRate, "application/json").build();
	}

	@POST
	@Path("/create")
	public Response create(ExchangeRate exchangeRate) throws URISyntaxException, BuisnessException {
		long id = exchangeRateManager.create(exchangeRate);
		if (id == 0L)
			return Response.ok(new UserMessageDTO("Can not creat Exchange Rate, please check logs")).build();
		if (id == -1L) {
			return Response.ok(new UserMessageDTO("Exchange Rate for '" + exchangeRate.getCurreny() + "' is exist"))
					.build();
		}
		return Response
				.ok(new UserMessageDTO("Exchange Rate for '" + exchangeRate.getCurreny()
						+ "'is added successfully for more details::"
						+ new URI(new StringBuilder("http://localhost:9998/exchangerate/").append(id).toString())))
				.build();
	}

	@PUT
	@Path("/update")
	public Response update(ExchangeRate exchangeRate) throws URISyntaxException, BuisnessException {
		ExchangeRate rate = exchangeRateManager.findByCurrency(exchangeRate.getCurreny());
		rate.setBuyingRate(exchangeRate.getBuyingRate());
		rate.setSellingRate(exchangeRate.getSellingRate());
		exchangeRateManager.update(rate);
		return Response
				.ok(new UserMessageDTO("Exchange Rate is updated succfully for more details::" + new URI(
						new StringBuilder("http://localhost:9998/exchangerate/").append(rate.getId()).toString())))
				.build();
	}
}
