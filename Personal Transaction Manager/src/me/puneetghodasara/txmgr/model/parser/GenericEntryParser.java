package me.puneetghodasara.txmgr.model.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.puneetghodasara.txmgr.exception.CustomException;
import me.puneetghodasara.txmgr.exception.MessageException;
import me.puneetghodasara.txmgr.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.CreditDebitEnum;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;

import org.apache.log4j.Logger;

public class GenericEntryParser implements EntryParser {

	Logger logger = Logger.getLogger(GenericEntryParser.class);

	@Override
	public List<Transaction> convertEntries(Account account, List<GenericStatementEntry> statementEntryList) throws MessageException {

		List<Transaction> txList = new ArrayList<Transaction>();

		List<String> errorEntryList = new ArrayList<String>();

		for (GenericStatementEntry statementEntry : statementEntryList) {
			try {
				Transaction tx = getTransactionEntry(account, statementEntry);
				txList.add(tx);
			} catch (CustomException e) {
				String srNo = statementEntry.getSrNo();
				logger.error("Statement Entry no " + srNo + " for " + account + " can not be converted to transaction. Reason :" + e.getMessage());
				errorEntryList.add(srNo);
			}
		}
		if (errorEntryList.size() > 0) {
			String message = "Following Statement Entries are invalid :\n"
					+ errorEntryList;
			throw new MessageException(message);
		}

		return txList;
	}

	private Transaction getTransactionEntry(Account account, GenericStatementEntry statementEntry) throws CustomException {
		Transaction tx = new Transaction();
		tx.setAccount(account);
		tx.setDescription(statementEntry.getDescription());

		Double amount = null;
		CreditDebitEnum creditDebit = null;

		if (statementEntry.getAbsAmount() == null) {
			String credit = statementEntry.getCredit();
			String debit = statementEntry.getDebit();

			try {
				amount = Double.parseDouble(credit);
				creditDebit = CreditDebitEnum.CREDIT;
			} catch (NumberFormatException nfe) {
			}
			try {
				amount = Double.parseDouble(debit);
				creditDebit = CreditDebitEnum.DEBIT;
			} catch (NumberFormatException nfe) {
			}
			if (amount == null) {
				logger.error("Number Format Exception for :" + credit + " / " + debit);
				throw new CustomException(ExceptionType.CREDIT_DEBIT_PARSE_ERROR);
			}
		} else {
			String absAmount = statementEntry.getAbsAmount();
			try {
				amount = Double.parseDouble(absAmount);
				creditDebit = amount > 0 ? CreditDebitEnum.DEBIT : CreditDebitEnum.CREDIT;
			} catch (NumberFormatException nfe) {
				logger.error("Number Format Exception for :" + absAmount);
				throw new CustomException(ExceptionType.CREDIT_DEBIT_PARSE_ERROR);
			}
		}

		tx.setAmount(amount);
		tx.setCreditDebit(creditDebit);

		String dateString = statementEntry.getDate();
		Date date;
		try {
			date = DateFormat.getInstance().parse(dateString);
		} catch (ParseException pe) {
			logger.error("Date Parse Exception for :" + dateString);
			throw new CustomException(ExceptionType.DATE_PARSE_ERROR);
		}
		tx.setDate(date);

		return tx;
	}
}
