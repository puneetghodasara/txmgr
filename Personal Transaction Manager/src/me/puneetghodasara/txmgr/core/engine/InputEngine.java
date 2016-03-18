package me.puneetghodasara.txmgr.core.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.integration.TransactionRepository;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
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

@Component("inputEngine")
public class InputEngine {

	// Logger
	private static final Logger logger = Logger.getLogger(InputEngine.class);

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private StatementParser statementParser;

	@Autowired
	private TransactionParser transactionParser;

	@Autowired
	private TransactionRepository transactionRepository;

	public void processStatementFile(Statement stmt) {

		String absoluteFileName = stmt.getFilename();
		String accountName = stmt.getAccountname();

		logger.debug("Inside processStatementFile with filename " + absoluteFileName);

		// Step 1 : Check
		File statementFile = new File(absoluteFileName);
		if (!statementFile.exists()) {
			logger.error("File " + absoluteFileName + " not found.");
			return;
		}

		Account account = accountManager.getAccountByName(accountName);
		if (account == null) {
			logger.error("Account name " + accountName + " is not compatible.");
			return;
		}

		// Step 2 : get TransactionHelper of account.
		RecordParser recordParser = Factory.getRecordParser(account);
		DateParser dateParser = Factory.getDateParser(account);

		if (recordParser == null || dateParser == null) {
			logger.error("Helper class for account " + accountName + " is not registered.");
			return;
		}

		// Step 3 : parse to generic statement entries
		List<GenericStatementEntry> statementEntries;
		try {
			statementEntries = recordParser.parseStatementFile(statementFile.getAbsolutePath());
		} catch (Exception e) {
			logger.error("Error occurred while parsing statement file." + e);
			return;
		}

		// Step 4 : convert to transactions.
		statementParser.setDateParser(dateParser);
		List<Transaction> transactionList = new ArrayList<Transaction>();
		try {
			for (GenericStatementEntry stmtEntry : statementEntries) {
				transactionList.add(statementParser.getTransactionEntry(stmtEntry, account));
			}
		} catch (CustomException e) {
			logger.error("Error occurred while converting to transactions." + e);
			return;
		}

		// Step 5 : parse transactions to detail
		for (Transaction tx : transactionList) {
			// if it is old, override
			Integer txId = transactionRepository.isTransactionSaved(tx.getAccount().getId(), tx.getDescription(),
					tx.getAmount(), tx.getDate());
			Transaction originalTx = tx;
			if (txId != null) {
				originalTx = transactionRepository.getOne(txId);
			}

			// Parse
			TransactionDetail transactionDetail = transactionParser.parseTransaction(originalTx);
			if (transactionDetail == null) {
				// TODO Unprocessed, like to do anything?
			}

			// SAVE
			transactionRepository.save(originalTx);
		}

		// Exit

		logger.debug("Filename " + absoluteFileName + " processed.");
	}

}
