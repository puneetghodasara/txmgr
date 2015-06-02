package me.puneetghodasara.txmgr.model.parser;

import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry.FieldMap;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

public class ICICIBankStatementParser implements StatementParser {

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

	@Override
	public MappingStrategy<GenericStatementEntry> getCsvMappingStrategy() {
		ColumnPositionMappingStrategy<GenericStatementEntry> strategy = new ColumnPositionMappingStrategy<GenericStatementEntry>();
		strategy.setType(GenericStatementEntry.class);
		strategy.setColumnMapping(columnMapping);

		return strategy;

	}

}
