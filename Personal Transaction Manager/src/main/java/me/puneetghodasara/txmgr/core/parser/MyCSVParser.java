package me.puneetghodasara.txmgr.core.parser;

import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

public abstract class MyCSVParser {

	public abstract CsvToBeanFilter getCsvToBeanFilter();

	public abstract MappingStrategy getCsvMappingStrategy();

}
