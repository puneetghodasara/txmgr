package me.puneetghodasara.txmgr.provider.citi;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;

import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.manager.impl.AccountManagerImpl;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.core.parser.CSVRecordParser;
import me.puneetghodasara.txmgr.core.parser.DateParser;
import me.puneetghodasara.txmgr.core.provider.TransactionHelper;
import me.puneetghodasara.txmgr.core.util.ExcelToCSV;
import me.puneetghodasara.txmgr.core.util.Factory;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

@Component(value="citiBankHelper")
@TransactionHelper(accountType=AccountTypeEnum.BANK_ACCOUNT, bank=BankEnum.CITI_BANK)
public class CITIBankHelper implements CSVRecordParser, DateParser {

	private static CsvToBeanFilter filter = (String[] line) -> {
		try {
			Double.parseDouble(line[0]);
		} catch (NumberFormatException nfe) {
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
	
	

}
