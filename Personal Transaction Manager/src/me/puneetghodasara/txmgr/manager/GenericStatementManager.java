package me.puneetghodasara.txmgr.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import me.puneetghodasara.txmgr.exception.CustomException;
import me.puneetghodasara.txmgr.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.integration.TransactionRepository;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.model.parser.DateParser;
import me.puneetghodasara.txmgr.model.parser.StatementParser;
import me.puneetghodasara.txmgr.model.parser.TransactionParser;
import me.puneetghodasara.txmgr.util.EntryParserUtil;
import me.puneetghodasara.txmgr.util.ExcelToCSV;
import me.puneetghodasara.txmgr.util.Factory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

@Component(value = "statementManager")
public class GenericStatementManager implements StatementManager {

	@Autowired
	@Qualifier(value = "excel2csv")
	private ExcelToCSV excelToCsv;

	@SuppressWarnings("rawtypes")
	private CsvToBean csvToBean = new CsvToBean();

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionParser transactionParser;

	private static Logger logger = Logger.getLogger(GenericStatementManager.class);

	@Override
	public boolean processStatement(Account account, String statementFileName) throws Exception {
		logger.debug("In getting Statement Entries for " + account + " with filename " + statementFileName);

		// Validate File
		File statementFile = new File(statementFileName);
		if (!statementFile.exists()) {
			logger.error("Statement file not found " + statementFileName);
			throw new FileNotFoundException(statementFileName);
		}
		logger.debug("validated.");

		// Convert to CSV
		File tempCsvFile = new File(statementFileName + ".csv");
		tempCsvFile.createNewFile();
		ExcelToCSV.convertToXls(statementFile, tempCsvFile);
		logger.debug("converted to csv.");

		// Get the statement parser
		StatementParser statementParser = Factory.getStatementParser(account);
		if (statementParser == null) {
			logger.error("No valid statement parser found from factory for " + account);
			throw new CustomException(ExceptionType.INVALID_STATEMENT_PARSER);
		}

		// Get filter and strategy
		CSVReader csvReader = new CSVReader(new FileReader(tempCsvFile), ',', '\"');
		CsvToBeanFilter filter = statementParser.getCsvToBeanFilter();
		MappingStrategy<GenericStatementEntry> strategy = statementParser.getCsvMappingStrategy();

		logger.debug("start of parsing.");
		// Parse
		List<GenericStatementEntry> statementEntryList = null;
		try {
			statementEntryList = csvToBean.parse(strategy, csvReader, filter);
		} catch (Exception e) {
			logger.error("Parsing error :" + e);
			throw new CustomException(ExceptionType.CSV_PARSE_EXCEPTION);
		}
		logger.debug("Listed statement entries " + statementEntryList.size());

		// Get the entry parser
		DateParser dateParser = Factory.getDateParser(account);
		if (dateParser == null) {
			logger.error("No valid date parser found from factory for " + account);
			throw new CustomException(ExceptionType.INVALID_DATE_PARSER);
		}

		EntryParserUtil entryParser = new EntryParserUtil(dateParser);
		List<Transaction> transactionEntryList = entryParser.convertEntries(account, statementEntryList);
		logger.debug("Listed tx entries.");

		// Store the tx entries
		transactionEntryList.forEach(tx -> {
			Integer txId = transactionRepository.isTransactionSaved(tx);
			if (txId == null) {
				// New Tx
				transactionParser.parseTransaction(tx);
				transactionRepository.saveTransaction(tx);
			} else {
				Transaction originalTx = transactionRepository.getTransactionById(txId);
				if(originalTx.getTransactionDetail() == null){
					// Unprocessed old
					transactionParser.parseTransaction(originalTx);
					transactionRepository.saveTransaction(originalTx);
				}
			}
		});

		// Delete tempCsv
		if (tempCsvFile.exists())
			tempCsvFile.delete();

		logger.debug("Returning statement entries list size " + transactionEntryList.size());
		return true;
	}

}
