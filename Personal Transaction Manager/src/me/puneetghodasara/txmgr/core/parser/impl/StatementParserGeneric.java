package me.puneetghodasara.txmgr.core.parser.impl;

import java.util.Date;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.core.exception.StatementParseException;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.CreditDebitEnum;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.core.parser.DateParser;
import me.puneetghodasara.txmgr.core.parser.StatementParser;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component(value="statementParser")
public class StatementParserGeneric implements StatementParser {

	// Logger
	private static final Logger logger = Logger.getLogger(StatementParserGeneric.class);

	// Required to generate date from date-string.
	private DateParser dateParser;

	public StatementParserGeneric() {
		super();
	}

	/**
	 * Converts StatementEntry to Transaction
	 * 
	 * @param account
	 * @param statementEntry
	 * @param account 
	 * @return
	 * @throws CustomException
	 */
	@Override
	public Transaction getTransactionEntry(GenericStatementEntry statementEntry, Account account) throws StatementParseException {
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
				throw new StatementParseException(ExceptionType.CREDIT_DEBIT_PARSE_ERROR);
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
			System.out.println(absAmount);
			try {
				amount = Double.parseDouble(absAmount);
				creditDebit = amount > 0 ? CreditDebitEnum.DEBIT : CreditDebitEnum.CREDIT;
			} catch (NumberFormatException nfe) {
				logger.error("Number Format Exception for :" + absAmount);
				throw new StatementParseException(ExceptionType.CREDIT_DEBIT_PARSE_ERROR);
			}
		}

		tx.setAmount(amount);
		tx.setCreditDebit(creditDebit);

		String dateString = statementEntry.getDate();
		Date date = dateParser.getDate(dateString);
		tx.setDate(date);

		return tx;
	}

	public void setDateParser(DateParser dateParser) {
		this.dateParser = dateParser;
	}

}
