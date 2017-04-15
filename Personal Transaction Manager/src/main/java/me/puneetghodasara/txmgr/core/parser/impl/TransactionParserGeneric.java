package me.puneetghodasara.txmgr.core.parser.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.db.TransactionDetail;
import me.puneetghodasara.txmgr.core.parser.TransactionParser;
import me.puneetghodasara.txmgr.core.parser.TransactionProcessor;
import me.puneetghodasara.txmgr.core.util.UnmatchedTransactionWriter;

@Component(value = "transactionParser")
public class TransactionParserGeneric implements TransactionParser {

	// Logger
	private static final Logger logger = Logger.getLogger(TransactionParserGeneric.class);

	@Resource
	TransactionProcessor transactionProcessor;

	@Override
	public TransactionDetail parseTransaction(Transaction transaction) {
		logger.info("Parsing Transaction " + transaction);

		Rule matchedRule = transactionProcessor.getMatchingRule(transaction);

		TransactionDetail txDetail = null;
		if (matchedRule != null && matchedRule.isTransfer()) {
			// It is a transfer
			txDetail = transactionProcessor.getTransferDetail(matchedRule, transaction.getDescription());
			transaction.setTransactionDetail(txDetail);
			return txDetail;
		} else if (matchedRule != null) {
			// It is a direct expense
			txDetail = transactionProcessor.getExpenseDetail(matchedRule);
			transaction.setTransactionDetail(txDetail);
			return txDetail;
		}

		// ask for a rule
		logger.info("No match for " + transaction);
		UnmatchedTransactionWriter.write(transaction);

		logger.debug("Transaction Parsed " + transaction);
		return txDetail;

	}

}
