package com.revolut.moneytransfer.utils;

import java.util.List;

import com.revolut.moneytransfer.dto.AccountDTO;
import com.revolut.moneytransfer.dto.TransactionDTO;
import com.revolut.moneytransfer.dto.UserDTO;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.OwnTransfer;
import com.revolut.moneytransfer.model.User;

public class DataConverterUtil {
	public DataConverterUtil() {
	}

	public static java.util.List<UserDTO> UserEntityDataConverter(java.util.List<User> loUsers) {
		List<UserDTO> loUserDTO = new java.util.ArrayList<UserDTO>();
		for (User user : loUsers) {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(user.getUserId());
			userDTO.setFirstName(user.getFirstName());
			userDTO.setLastName(user.getLastName());
			userDTO.setEmail(user.getEmail());
			loUserDTO.add(userDTO);
		}
		return loUserDTO;
	}

	public static AccountDTO convertAccountEntityToAccountDTO(Account account) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(account.getId());
		accountDTO.setAccountBalance(Double.valueOf(account.getAccountBalance()));
		accountDTO.setAccountCurrency(account.getAccountCurrency());
		accountDTO.setAccountNumber(account.getAccountNumber());
		accountDTO.setUserId(account.getUser().getUserId());
		return accountDTO;
	}

	public static TransactionDTO convertOwnTransferEntityToTransferDTO(OwnTransfer ownTransfer) {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionId(ownTransfer.getId());
		transactionDTO.setFromAccount(ownTransfer.getFromAccount());
		transactionDTO.setFromAccountCurrency(ownTransfer.getFromAccountCurrency());
		transactionDTO.setToAccount(ownTransfer.getToAccount());
		transactionDTO.setToAccountCurrency(ownTransfer.getToAccountCurrency());
		transactionDTO.setComment(ownTransfer.getComment());
		transactionDTO.setBenficaryName(ownTransfer.getBenficaryName());
		transactionDTO.setUserId(ownTransfer.getUser().getUserId());
		transactionDTO.setAmount(ownTransfer.getAmount());
		transactionDTO.setTransferCurrency( ownTransfer.getTransferCurrency());
		return transactionDTO;
	}
}
