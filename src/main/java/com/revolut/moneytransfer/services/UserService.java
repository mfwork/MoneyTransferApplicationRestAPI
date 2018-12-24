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

import com.revolut.moneytransfer.dto.UserDTO;
import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.User;
import com.revolut.moneytransfer.model.manager.AccountManager;
import com.revolut.moneytransfer.model.manager.UserManager;

@Path("/user")
@Produces({ "application/json" })
public class UserService {
	public UserService() {
	}

	static final Logger log = Logger.getLogger(UserService.class);

	static final UserManager userEntityManager = new UserManager();
	static final AccountManager accountManager = new AccountManager();

	@GET
	@Path("/all")
	public List<User> getAllUsers() {
		List<User> loUser = userEntityManager.findAll();
		return loUser;
	}

	@GET
	@Path("/{userId}")
	public Response getUserByID(@PathParam("userId") long userId) {
		User userEntity = userEntityManager.findByID(userId);
		if (userEntity != null) {
			return Response.ok(userEntity, "application/json").build();
		}
		return Response.ok(new UserMessageDTO("Can not find user with id:" + userId), "application/json").build();
	}

	@POST
	@Path("/create")
	public Response createUser(UserDTO userDTO) throws URISyntaxException {
		User userEntity = new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
		long id = userEntityManager.create(userEntity);
		if (id == 0L)
			return Response.ok(new UserMessageDTO("Can not creat user, please check logs")).build();
		if (id == -1L) {
			return Response.ok(new UserMessageDTO("User with mail '" + userDTO.getEmail()
					+ "' is exist, try to change the mail address as it is unique")).build();
		}
		return Response.ok(new UserMessageDTO("User added successfully for more details::"
				+ new URI(new StringBuilder("http://localhost:9998/user/").append(id).toString()))).build();
	}

	@GET
	@Path("/{userId}/accounts")
	public Response getUserAccounts(@PathParam("userId") long userId) {
		List<Account> loAccount = accountManager.findByUserId(userId);
		return Response.ok(loAccount, "application/json").build();
	}

	@DELETE
	@Path("/delete/{userID}")
	public Response deleteUser(@PathParam("userID") long userID) {
		if (!userEntityManager.delete(userID)) {
			return Response.ok(new UserMessageDTO("Can not delete user, please check logs")).build();
		}
		return Response.ok(new UserMessageDTO("User is deleted")).build();
	}

	@PUT
	@Path("/update")
	public Response updateUser(UserDTO userDTO) throws URISyntaxException {
		User userEntity = new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
		userEntity.setUserId(userDTO.getId());

		if (!userEntityManager.update(userEntity)) {
			return Response.ok(new UserMessageDTO("Can not update user, please check logs, please check logs")).build();
		}
		return Response
				.ok(new UserMessageDTO("User updated successfully for more details::" + new URI(
						new StringBuilder("http://localhost:9998/user/").append(userEntity.getUserId()).toString())))
				.build();
	}
}
