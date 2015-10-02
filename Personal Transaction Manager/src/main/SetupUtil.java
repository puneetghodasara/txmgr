package main;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;
import me.puneetghodasara.txmgr.core.provider.TransactionHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Setup Class that will be called when the bean initializing happens.
 * 
 * @author Punit_Ghodasara
 *
 */
public class SetupUtil implements ApplicationContextAware, InitializingBean {

	// Application Context
	private ApplicationContext appContext;

	// To store all registered helper bean
	private static Map<HelperMap, String> helperBeanMap = new HashMap<SetupUtil.HelperMap, String>();

	// To store all properties
	private static Properties prop = new Properties();

	// Logger
	private static final Logger logger = Logger.getLogger(SetupUtil.class);

	/**
	 * Gives the bean name type account of bank
	 * 
	 * @param accountTypeEnum
	 * @param bankEnum
	 * @return
	 */
	public static String getHelperBeanName(AccountTypeEnum accountTypeEnum, BankEnum bankEnum) {
		return helperBeanMap.get(new HelperMap(accountTypeEnum, bankEnum));
	}

	public static Properties getProp() {
		return prop;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> helperMap = appContext.getBeansWithAnnotation(TransactionHelper.class);
		for (Object helper : helperMap.values()) {
			final Class<? extends Object> helperClass = helper.getClass();
			final TransactionHelper annotation = helperClass.getAnnotation(TransactionHelper.class);
			logger.info("Found Helper class: " + helperClass + ", for bank: " + annotation.bank() + ", for type: " + annotation.accountType());
			String[] beanNames = appContext.getBeanNamesForType(helperClass);
			if (beanNames.length > 0) {
				helperBeanMap.put(new HelperMap(annotation.accountType(), annotation.bank()), beanNames[0]);
			}
		}

		// Get All Properties
		try (FileInputStream propFile = new FileInputStream(new File("transfer.csv.prop"))) {
			prop.load(propFile);
		} catch (Exception e) {
			// Suppress
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	/**
	 * A key class to store all helper beans
	 * 
	 * @author Punit_Ghodasara
	 *
	 */
	private static class HelperMap {
		private AccountTypeEnum accountTypeEnum;
		private BankEnum bankEnum;

		public HelperMap(AccountTypeEnum accountTypeEnum, BankEnum bankEnum) {
			super();
			this.accountTypeEnum = accountTypeEnum;
			this.bankEnum = bankEnum;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((accountTypeEnum == null) ? 0 : accountTypeEnum.hashCode());
			result = prime * result + ((bankEnum == null) ? 0 : bankEnum.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			HelperMap other = (HelperMap) obj;
			if (accountTypeEnum != other.accountTypeEnum)
				return false;
			if (bankEnum != other.bankEnum)
				return false;
			return true;
		}

	}

}
