package me.puneetghodasara.txmgr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import me.puneetghodasara.txmgr.exception.CustomException;
import me.puneetghodasara.txmgr.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.model.db.Rule;

import org.apache.log4j.Logger;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

public class CSVRuleReader {

	private static final Logger logger = Logger.getLogger(CSVRuleReader.class);
	
	public static Collection<Rule> loadAllRules(String fileName) throws CustomException{
		logger.debug("Inside Loading rules from file "+fileName);
		
		CsvToBean<Rule> csvToBean = new CsvToBean<Rule>();
		Reader reader = null;
		try {
			reader = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new CustomException(ExceptionType.RULE_FILE_NOT_FOUND);
		}
		
		ColumnPositionMappingStrategy<Rule> mapper = new ColumnPositionMappingStrategy<Rule>();
		mapper.setColumnMapping("rule","category","merchant");
		mapper.setType(Rule.class);
		
		List<Rule> ruleList = csvToBean.parse(mapper, reader);
		
		logger.info("Rules found :"+ruleList.size());
		return ruleList;
	}
	
}
