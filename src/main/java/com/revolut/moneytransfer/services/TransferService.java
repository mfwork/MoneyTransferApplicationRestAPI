package com.revolut.moneytransfer.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revolut.moneytransfer.business.TransfersCalcualtion;
import com.revolut.moneytransfer.dto.TransactionDTO;
import com.revolut.moneytransfer.dto.UserMessageDTO;
import com.revolut.moneytransfer.exception.BuisnessException;
import com.revolut.moneytransfer.model.OwnTransfer;
import com.revolut.moneytransfer.model.manager.OwnTransferManager;
import com.revolut.moneytransfer.model.manager.UserManager;
import com.revolut.moneytransfer.utils.DataConverterUtil;

@Path("/transfer")
@Produces({ "application/json" })
public class TransferService {
	private static final Logger log = Logger.getLogger(TransferService.class);
	private static final OwnTransferManager ownTransferManager = new OwnTransferManager();
	private static final UserManager userManager = new UserManager();

	public TransferService() {
	}

	@GET
	@Path("/all")
	public Response getAllOwnTransfers() throws BuisnessException {
		List<OwnTransfer> loOwnTransferEntity = ownTransferManager.findAll();
		return Response.ok(loOwnTransferEntity, "application/json").build();
	}

	@GET
	@Path("/{ownTransferId}")
	public Response getOwnTransferDetailsByID(@PathParam("ownTransferId") long ownTransferId) throws BuisnessException {
		OwnTransfer OwnTransfer = ownTransferManager.findByID(ownTransferId);
		TransactionDTO transactionDTO = DataConverterUtil.convertOwnTransferEntityToTransferDTO(OwnTransfer);
		return Response.ok(transactionDTO, "application/json").build();
	}

	@GET
	@Path("/usertrans/{userId}")
	public Response getOwnTransfersByuserId(@PathParam("userId") long userId) throws BuisnessException {
		List<OwnTransfer> loOwnTransfers = ownTransferManager.findByUserId(userId);
		return Response.ok(loOwnTransfers, "application/json").build();
	}

	@POST
	@Path("/create")
	public Response create(@Valid TransactionDTO transactionDTO)
			throws URISyntaxException, BuisnessException, IOException {
		TransfersCalcualtion transfersCalcualtion = new TransfersCalcualtion();
		String id = transfersCalcualtion.ownTransferTransaction(transactionDTO);
		return Response.ok(new UserMessageDTO("Record added succfully for more details::"
				+ new URI(new StringBuilder("http://localhost:9998/transfer/").append(id).toString()))).build();
	}
}
