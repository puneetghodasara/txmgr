package model.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import model.db.Account;
import model.db.Transaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import Util.ExcelToCSV;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

import exception.CustomException;
import exception.CustomException.ExceptionType;

public class GenericStatementManager implements StatementManager {

	@Autowired
	private ExcelToCSV excelToCsv;

	@Autowired
	private StatementParserFactory statementParserFactory;
	
	@Autowired
	private EntryParserFactory entryParserFactory;

	@Autowired
	@SuppressWarnings("rawtypes")
	private CsvToBean csvToBean;

	private static Logger logger = Logger.getLogger(GenericStatementManager.class);

	@Override
	public List<Transaction> getStatementEntries(Account account, String statementFileName) throws Exception {
		logger.debug("In getting Statement Entries for " + account + " with filename " + statementFileName);

		// Validate File
		File statementFile = new File(statementFileName);
		if (!statementFile.exists()) {
			logger.error("Statement file not found " + statementFileName);
			throw new FileNotFoundException(statementFileName);
		}

		// Convert to CSV
		File tempCsvFile = new File(statementFileName + ".csv");
		tempCsvFile.createNewFile();
		ExcelToCSV.convertToXls(statementFile, tempCsvFile);

		// Get the statement parser
		StatementParser statementParser = statementParserFactory.getStatementParser(account);
		if (statementParser == null) {
			logger.error("No valid statement parser found from factory for " + account);
			throw new CustomException(ExceptionType.INVALID_STATEMENT_PARSER);
		}

		// Get filter and strategy
		CSVReader csvReader = new CSVReader(new FileReader(tempCsvFile));
		CsvToBeanFilter filter = statementParser.getCsvToBeanFilter();
		MappingStrategy<GenericStatementEntry> strategy = statementParser.getCsvMappingStrategy();

		// Parse
		List<GenericStatementEntry> statementEntryList = null;
		try {
			statementEntryList = csvToBean.parse(strategy, csvReader, filter);
		} catch (Exception e) {
			logger.error("Parsing error :"+e);
			throw new CustomException(ExceptionType.CSV_PARSE_EXCEPTION);
		}

		// Get the entry parser
		EntryParser entryParser = entryParserFactory.getEntryParser(account);
		if (entryParser == null) {
			logger.error("No valid entry parser found from factory for " + account);
			throw new CustomException(ExceptionType.INVALID_ENTRY_PARSER);
		}
		
		List<Transaction> transactionEntryList = entryParser.convertEntries(account, statementEntryList);
		
		logger.debug("Returning statement entries list size "+transactionEntryList.size());
		return transactionEntryList;
	}

}
