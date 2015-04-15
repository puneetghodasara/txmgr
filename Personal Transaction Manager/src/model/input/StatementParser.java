package model.input;

import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

public interface StatementParser {

	public CsvToBeanFilter getCsvToBeanFilter();

	public MappingStrategy<GenericStatementEntry> getCsvMappingStrategy();

}
