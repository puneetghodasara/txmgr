package me.puneetghodasara.txmgr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import me.puneetghodasara.txmgr.model.db.Rule;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

public class CSVRuleReader {

	public static Collection<Rule> loadAllRules(String fileName){
		
		CsvToBean<Rule> csvToBean = new CsvToBean<Rule>();
		Reader reader = null;
		try {
			reader = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ColumnPositionMappingStrategy<Rule> mapper = new ColumnPositionMappingStrategy<Rule>();
		mapper.setColumnMapping("rule","category","merchant");
		mapper.setType(Rule.class);
		
		List<Rule> ruleList = csvToBean.parse(mapper, reader);
		
		
		
		return ruleList;
		
	}
	
}
