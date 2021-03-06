package me.puneetghodasara.txmgr.core.parser;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

import me.puneetghodasara.txmgr.core.model.input.GenericStatementEntry;

public interface CSVRecordParser extends RecordParser {

	static Logger logger = Logger.getLogger(CSVRecordParser.class);
	
	public CsvToBeanFilter getCsvToBeanFilter();

	public MappingStrategy<GenericStatementEntry> getCsvMappingStrategy();

	public default List<GenericStatementEntry> parseStatementFile(byte[] csvFile) throws Exception {
		List<GenericStatementEntry> statementEntryList = null;

		CSVReader csvReader;
		try {
			csvReader = getCSVReader(csvFile);
		} catch (FileNotFoundException e1) {
			logger.error("CSV File passed " + csvFile + " is not found.");
			return null;
		}

		CsvToBean csvToBean = getCSVToBean();
		try {
			statementEntryList = csvToBean.parse(getCsvMappingStrategy(), csvReader, getCsvToBeanFilter());
		} catch (Exception e) {
			logger.error("Parsing error :" + e);
			throw e;
		}
		return statementEntryList;
	}

//	public default CSVReader getCSVReader(String csvFile) throws FileNotFoundException {
//		return new CSVReader(new FileReader(csvFile), ',', '\"');
//	}

	
	public default CSVReader getCSVReader(byte[] csvFile) throws FileNotFoundException {
		return new CSVReader(new InputStreamReader(new ByteArrayInputStream(csvFile)), ',', '\"');
	}
	
	public default CsvToBean getCSVToBean() {
		return new CsvToBean();
	}

}
