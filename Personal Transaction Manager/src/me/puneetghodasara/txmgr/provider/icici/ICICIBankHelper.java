package me.puneetghodasara.txmgr.provider.icici;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.model.parser.DateParser;
import me.puneetghodasara.txmgr.model.parser.StatementParser;
import me.puneetghodasara.txmgr.provider.TransactionHelper;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

@Component(value="iciciBankHelper")
@TransactionHelper(accountType=AccountTypeEnum.BANK_ACCOUNT, bank=BankEnum.ICICI_BANK)
public class ICICIBankHelper implements StatementParser, DateParser {

	private static CsvToBeanFilter filter = (String[] line) -> {
		try {
			Integer.parseInt(line[1]);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	};

	private static String[] columnMapping = new String[] { "", GenericStatementEntry.FieldMap.SERIAL_NUMBER.value, GenericStatementEntry.FieldMap.DATE.value,
			"", "", GenericStatementEntry.FieldMap.DESCRIPTION.value, GenericStatementEntry.FieldMap.DEBIT_AMOUNT.value,
			GenericStatementEntry.FieldMap.CREDIT_AMOUNT.value };

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
		DateFormat instance = new SimpleDateFormat("DD/MM/yyyy");
		try {
			return instance.parse(dateString);
		} catch (ParseException e) {
		}
		return null;
	}

}
