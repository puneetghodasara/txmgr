package me.puneetghodasara.txmgr.provider.citi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import main.Main;
import me.puneetghodasara.txmgr.exception.CustomException;
import me.puneetghodasara.txmgr.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.exception.MessageException;
import me.puneetghodasara.txmgr.manager.AccountManager;
import me.puneetghodasara.txmgr.manager.AccountManagerImpl;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.model.parser.DateParser;
import me.puneetghodasara.txmgr.model.parser.StatementParser;
import me.puneetghodasara.txmgr.util.EntryParserUtil;
import me.puneetghodasara.txmgr.util.ExcelToCSV;
import me.puneetghodasara.txmgr.util.Factory;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

public class CITIBankHelper implements StatementParser, DateParser {

	private static CsvToBeanFilter filter = (String[] line) -> {
		try {
			Double.parseDouble(line[0]);
		} catch (NumberFormatException nfe) {
			System.out.println("Ignoring "+line[0] +":"+nfe.getMessage());
			return false;
		}
		return true;
	};

	/*
	 * 
	 * "42010.0",
	 * "ATM WITHDRAWAL SUBJECT: EURONT 06JAN0902 Card no.: 5497XXX0XXXX2905 Ref: 5224 +THIPAASANDRA,INAGAR CDBANGALORE KAIN"
	 * , "4000.0", "", "80378.14",
	 */
	private static String[] columnMapping = new String[] { GenericStatementEntry.FieldMap.DATE.value, GenericStatementEntry.FieldMap.DESCRIPTION.value,
			GenericStatementEntry.FieldMap.DEBIT_AMOUNT.value, GenericStatementEntry.FieldMap.CREDIT_AMOUNT.value };

	@Override
	public CsvToBeanFilter getCsvToBeanFilter() {
		return filter;
	}

	@SuppressWarnings("deprecation")
	@Override
	public MappingStrategy<GenericStatementEntry> getCsvMappingStrategy() {
		ColumnPositionMappingStrategy<GenericStatementEntry> strategy = new ColumnPositionMappingStrategy<GenericStatementEntry>();
		strategy.setType(GenericStatementEntry.class);
		strategy.setColumnMapping(columnMapping);

		return strategy;
	}

	@Override
	public Date getDate(String dateString) {
		Date date = null;
		try{
			date = DateUtil.getJavaDate(Double.parseDouble(dateString));
		}catch(NumberFormatException nfe){
		}catch(Exception e){
		}
		return date;
	}
	
	
	public static void main(String[] args) throws Exception{

		Logger logger = Logger.getLogger(CITIBankHelper.class);
		
		CITIBankHelper citiBankHelper = new CITIBankHelper();
		
		String statementFileName = "CITI-Test.xls";
		File statementFile = new File(statementFileName);
		File tempCsvFile = new File(statementFileName  + ".csv");
		tempCsvFile.createNewFile();
		ExcelToCSV.convertToXls(statementFile, tempCsvFile);
		
		
		CSVReader csvReader = new CSVReader(new FileReader(tempCsvFile),',','\"');
		CsvToBeanFilter filter = citiBankHelper.getCsvToBeanFilter();
		MappingStrategy<GenericStatementEntry> strategy = citiBankHelper.getCsvMappingStrategy();

		logger.debug("start of parsing.");
		// Parse
		List<GenericStatementEntry> statementEntryList = null;
		try {
			CsvToBean csvToBean = new CsvToBean();
			statementEntryList = csvToBean.parse(strategy, csvReader, filter);
		} catch (Exception e) {
			logger.error("Parsing error :"+e);
		}
		logger.debug("Listed statement entries "+statementEntryList.size());

		EntryParserUtil entryParser = new EntryParserUtil(new CITIBankHelper());
		AccountManager accountManager = new AccountManagerImpl();
		Account account = new Account();
		account.setName("CITI-Test");
		account.setBank(BankEnum.ICICI.getBank());
		account.setAccountType(AccountTypeEnum.BANK_ACCOUNT.getAccountType());
		account.setTag("Test-Tag");
		account.setOpenDate(new Date());

		// Get the entry parser
		DateParser dateParser = Factory.getDateParser(account);
		if (dateParser == null) {
			logger.error("No valid date parser found from factory for " + account);
		}

		List<Transaction> transactionEntryList = entryParser.convertEntries(account, statementEntryList);
		logger.debug("Listed tx entries.");

		transactionEntryList.forEach(tx->{
			logger.info(tx);
		});

	
	}

}
