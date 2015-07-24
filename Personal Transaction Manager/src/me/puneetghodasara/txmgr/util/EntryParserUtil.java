package me.puneetghodasara.txmgr.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.puneetghodasara.txmgr.exception.CustomException;
import me.puneetghodasara.txmgr.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.exception.MessageException;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.CreditDebitEnum;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.model.parser.DateParser;

import org.apache.log4j.Logger;

public class EntryParserUtil {

	private DateParser dateParser;

	public EntryParserUtil(DateParser dateParser) {
		this.dateParser = dateParser;
	}

	static final Logger logger = Logger.getLogger(EntryParserUtil.class);

	public List<Transaction> convertEntries(Account account, List<GenericStatementEntry> statementEntryList) throws MessageException {

		// Return List
		List<Transaction> txList = new ArrayList<Transaction>();

		// Error List
		List<String> errorEntryList = new ArrayList<String>();

		// Convert Each and add to return list or error list
		statementEntryList.forEach(stmtEntry -> {
			try {
				txList.add(getTransactionEntry(account, stmtEntry));
			} catch (Exception e) {
				String srNo = stmtEntry.getSrNo();
				logger.error("Statement Entry no " + srNo + " for " + account + " can not be converted to transaction. Reason :" + e.getMessage());
				errorEntryList.add(srNo);
			}
		});

		if (errorEntryList.size() > 0) {
			String message = "Following Statement Entries are invalid :\n" + errorEntryList;
			throw new MessageException(message);
		}

		return txList;
	}

	/**
	 * Converts StatementEntry to Transaction
	 * 
	 * @param account
	 * @param statementEntry
	 * @return
	 * @throws CustomException
	 */
	private Transaction getTransactionEntry(Account account, GenericStatementEntry statementEntry) throws CustomException {
		Transaction tx = new Transaction();
		tx.setAccount(account);
		tx.setDescription(statementEntry.getDescription());

		Double amount = null;
		CreditDebitEnum creditDebit = null;

		if (statementEntry.getAbsAmount() == null) {
			String credit = statementEntry.getCredit().replaceAll("[^0-9.]", "");
			String debit = statementEntry.getDebit().replaceAll("[^0-9.]", "");

			Double crAmount = null;
			Double drAmount = null;
			try {
				crAmount = Double.parseDouble(credit);
			} catch (NumberFormatException nfe) {
			}
			try {
				drAmount = Double.parseDouble(debit);
			} catch (NumberFormatException nfe) {
			}
			if ((crAmount == null && drAmount == null) || (crAmount != null && crAmount.doubleValue() == 0 && drAmount != null && drAmount == 0)) {
				logger.error("Number Format Exception for :" + credit + " / " + debit);
				throw new CustomException(ExceptionType.CREDIT_DEBIT_PARSE_ERROR);
			}
			if (crAmount == null || crAmount.doubleValue() == 0) {
				amount = drAmount;
				creditDebit = CreditDebitEnum.DEBIT;
			} else if (drAmount == null || drAmount.doubleValue() == 0) {
				amount = crAmount;
				creditDebit = CreditDebitEnum.CREDIT;
			} else {
				amount = crAmount;
				creditDebit = CreditDebitEnum.CREDIT;
			}
		} else {
			String absAmount = statementEntry.getAbsAmount().replaceAll("[^0-9.-]", "");
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
		Date date = dateParser.getDate(dateString);
		tx.setDate(date);

		return tx;
	}

}
