package me.puneetghodasara.txmgr.model.parser;

import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;

import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

public interface StatementParser {

	public CsvToBeanFilter getCsvToBeanFilter();

	public MappingStrategy<GenericStatementEntry> getCsvMappingStrategy();

}
