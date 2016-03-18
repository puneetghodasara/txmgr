package me.puneetghodasara.txmgr.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import main.Main;
import me.puneetghodasara.txmgr.config.SetupUtil;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.parser.DateParser;
import me.puneetghodasara.txmgr.core.parser.RecordParser;

public class Factory {

	private static Logger logger = Logger.getLogger(Factory.class);

	public static RecordParser getRecordParser(Account account) {
		logger.debug("In statemetn parser factory.");
		String beanName = SetupUtil.getHelperBeanName(account.getAccountType(), account.getBank());
		if (!StringUtils.isBlank(beanName)) {
			RecordParser recordParser = (RecordParser) Main.appContext.getBean(beanName);
			logger.info("Statement parser returned :" + recordParser);
			return recordParser;
		}
		logger.warn("Returning NULL statement parser.");
		return null;
	}

	public static DateParser getDateParser(Account account) {
		logger.debug("In date parser factory.");
		String beanName = SetupUtil.getHelperBeanName(account.getAccountType(), account.getBank());
		if (!StringUtils.isBlank(beanName)) {
			DateParser dateParser = (DateParser) Main.appContext.getBean(beanName);
			logger.info("date parser returned :" + dateParser);
			return dateParser;
		}
		logger.warn("Returning NULL date parser.");
		return null;

	}
}
