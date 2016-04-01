package me.puneetghodasara.txmgr.core.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.integration.TransactionRepository;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.Statement;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.db.TransactionDetail;
import me.puneetghodasara.txmgr.core.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.core.parser.DateParser;
import me.puneetghodasara.txmgr.core.parser.RecordParser;
import me.puneetghodasara.txmgr.core.parser.StatementParser;
import me.puneetghodasara.txmgr.core.parser.TransactionParser;
import me.puneetghodasara.txmgr.core.util.Factory;

@Component("statementProcessor")
public class StatementProcessor implements Runnable{

	// Logger
	private static final Logger logger = Logger.getLogger(StatementProcessor.class);

	@Autowired
	private StatementParser statementParser;

	@Autowired
	private TransactionParser transactionParser;

	@Autowired
	private TransactionRepository transactionRepository;

	private Statement statement;
	
	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	private void processStatementFile() {

		String absoluteFileName = statement.getFilename();
		Account account = statement.getAccount();

		logger.debug("Inside processStatementFile with filename " + absoluteFileName);

		// Step 1 : Check
		if (account == null) {
			logger.error("Account " + account + " is not compatible.");
			return;
		}

		// Step 2 : get TransactionHelper of account.
		RecordParser recordParser = Factory.getRecordParser(account);
		DateParser dateParser = Factory.getDateParser(account);

		if (recordParser == null || dateParser == null) {
			logger.error("Helper class for account " + account + " is not registered.");
			return;
		}

		// Step 3 : parse to generic statement entries
		List<GenericStatementEntry> statementEntries;
		try {
			statementEntries = recordParser.parseStatementFile(statement.getContent());
		} catch (Exception e) {
			logger.error("Error occurred while parsing statement file." + e);
			return;
		}

		// Step 4 : convert to transactions.
		statementParser.setDateParser(dateParser);
		List<Transaction> transactionList = new ArrayList<Transaction>();
		try {
			for (GenericStatementEntry stmtEntry : statementEntries) {
				Transaction transactionEntry = statementParser.getTransactionEntry(stmtEntry, account);
				transactionList.add(transactionEntry);
			}
		} catch (CustomException e) {
			logger.error("Error occurred while converting to transactions." + e);
			return;
		}

		// Step 5 : parse transactions to detail
		for (Transaction tx : transactionList) {
			// if it is old, override
			Transaction originalTx = transactionRepository.isTransactionSaved(tx.getAccount().getId(), tx.getDescription(),
					tx.getAmount(), tx.getDate());

			// Parse
			TransactionDetail transactionDetail = null;
			if(originalTx != null){
				transactionDetail = transactionParser.parseTransaction(originalTx);
				transactionRepository.save(originalTx);
			} else{
				transactionDetail = transactionParser.parseTransaction(tx);
				transactionRepository.save(tx);
			}

		}

		// Exit

		logger.debug("Filename " + absoluteFileName + " processed.");
	}

	@Override
	public void run() {
		if(statement != null)
			processStatementFile();
	}

}
