package me.puneetghodasara.txmgr.core.parser.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.core.exception.RuleParserException;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.parser.MyCSVParser;
import me.puneetghodasara.txmgr.core.parser.RuleParser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

@Component(value="ruleParser")
public class RuleParserCSV extends MyCSVParser implements RuleParser {

	// Logger
	private static final Logger logger = Logger.getLogger(RuleParserCSV.class);

	// CSV File name
	@Value("${file.rule}")
	private String fileName;

	// CSV To Bean
	@Autowired(required=false)
	private CsvToBean<Rule> csvToBean;

	public RuleParserCSV() {
		super();
	}

	@Override
	public CsvToBeanFilter getCsvToBeanFilter() {
		return null;
	}

	@Override
	public MappingStrategy<Rule> getCsvMappingStrategy() {
		ColumnPositionMappingStrategy<Rule> mapper = new ColumnPositionMappingStrategy<Rule>();
		mapper.setColumnMapping("rule", "category", "merchant", "targetAccount", "propRef");
		mapper.setType(Rule.class);
		return mapper;
	}

	@Override
	public List<Rule> getRules() throws Exception {
		logger.debug("Inside Loading rules from file " + fileName);

		Reader reader = null;
		try {
			reader = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuleParserException(ExceptionType.RULE_FILE_NOT_FOUND);
		}

		// Parsing
		List<Rule> ruleList;
		try {
			ruleList = getCsvToBean().parse(getCsvMappingStrategy(), reader);
		} catch (Exception e) {
			logger.error("Error while parsing rule file."+e);
			throw e;
		}

		logger.info("Rules found :" + ruleList.size());
		return ruleList;
	}

	public CsvToBean<Rule> getCsvToBean() {
		if(csvToBean == null){
			setCsvToBean(null);
		}
		return csvToBean;
	}

	public void setCsvToBean(CsvToBean<Rule> csvToBean) {
		this.csvToBean = new CsvToBean<Rule>();
	}
	
}
