package com.revolut.moneytransfer.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revolut.moneytransfer.dto.AccountDTO;
import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.exception.BuisnessException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.model.manager.AccountManager;
import com.revolut.moneytransfer.model.manager.UserManager;
import com.revolut.moneytransfer.utils.DataConverterUtil;

@Path("/account")
@Produces({ "application/json" })
public class AccountService {
	static final Logger log = Logger.getLogger(AccountService.class);

	static final AccountManager accountManager = new AccountManager();
	static final UserManager userManager = new UserManager();

	public AccountService() {
	}

	@GET
	@Path("/all")
	public Response getAllAccounts() {
		List<Account> loAccountEntity = accountManager.findAll();
		return Response.ok(loAccountEntity, "application/json").build();
	}

	@GET
	@Path("/{accountId}")
	public Response getAccountDetailsByID(@PathParam("accountId") long accountId) {
		Account account = accountManager.findByID(accountId);
		if (account != null) {
			AccountDTO accountDTO = DataConverterUtil.convertAccountEntityToAccountDTO(account);
			return Response.ok(accountDTO, "application/json").build();
		}
		return Response.ok(new UserMessageDTO("Can not find account with id:" + accountId), "application/json").build();
	}

	@GET
	@Path("/{accountNumber}/details")
	public Response getAccountDetailsByAccountNumber(@PathParam("accountNumber") String accountNumber) {
		Account account = accountManager.findByAccNo(accountNumber);
		AccountDTO accountDTO = DataConverterUtil.convertAccountEntityToAccountDTO(account);
		return Response.ok(accountDTO, "application/json").build();
	}

	@POST
	@Path("/create")
	public Response createAccount(AccountDTO accountDTO) throws URISyntaxException {
		Account account = new Account(accountDTO.getAccountNumber(), accountDTO.getAccountCurrency(),
				accountDTO.getAccountBalance());
		User user = userManager.findByID(accountDTO.getUserId());
		account.setUser(user);
		long id = accountManager.create(account);
		if (id == 0L) {
			return Response.ok(new UserMessageDTO("Create Error please check logs")).build();
		}
		return Response.ok(new UserMessageDTO("Account is added successfully for more details::"
				+ new URI(new StringBuilder("http://localhost:9998/account/").append(id).toString()))).build();
	}

	@DELETE
	@Path("/delete/{accountId}")
	public Response deleteAccount(@PathParam("accountId") long accountId) {
		if (!accountManager.delete(accountId)) {
			return Response.ok(new UserMessageDTO("Can not delete account, please check logs")).build();
		}
		return Response.ok(new UserMessageDTO("Account is deleted")).build();
	}

	@PUT
	@Path("/update")
	public Response updateAccount(AccountDTO accountDTO) throws URISyntaxException, BuisnessException {
		Account accountEntity = accountManager.findByID(accountDTO.getId());
		accountEntity.setAccountBalance(accountDTO.getAccountBalance());
		if (!accountManager.update(accountEntity)) {
			return Response
					.ok(new UserMessageDTO(
							"Can not update account, please note that balance can only be updated, please check logs"))
					.build();
		}
		return Response
				.ok(new UserMessageDTO("Account is updated successfully for more details::" + new URI(
						new StringBuilder("http://localhost:9998/account/").append(accountEntity.getId()).toString())))
				.build();
	}
}
