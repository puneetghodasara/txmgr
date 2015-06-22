package me.puneetghodasara.txmgr.util;

import main.Main;
import main.SetupUtil;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.parser.DateParser;
import me.puneetghodasara.txmgr.model.parser.StatementParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Factory {

	private static Logger logger = Logger.getLogger(Factory.class);

	public static StatementParser getStatementParser(Account account) {
		logger.debug("In statemetn parser factory.");
		String beanName = SetupUtil.getHelperBeanName(account.getAccountType().getEnumValue(), account.getBank().getEnumValue());
		if (!StringUtils.isBlank(beanName)) {
			StatementParser stmtParser = (StatementParser) Main.appContext.getBean(beanName);
			logger.info("Statement parser returned :" + stmtParser);
			return stmtParser;
		}
		logger.warn("Returning NULL statement parser.");
		return null;
	}

	public static DateParser getDateParser(Account account) {
		logger.debug("In date parser factory.");
		String beanName = SetupUtil.getHelperBeanName(account.getAccountType().getEnumValue(), account.getBank().getEnumValue());
		if (!StringUtils.isBlank(beanName)) {
			DateParser dateParser = (DateParser) Main.appContext.getBean(beanName);
			logger.info("date parser returned :" + dateParser);
			return dateParser;
		}
		logger.warn("Returning NULL date parser.");
		return null;

	}
}
