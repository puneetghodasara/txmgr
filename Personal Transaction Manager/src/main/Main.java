package main;

import java.util.List;

import me.puneetghodasara.txmgr.integration.TransactionRepository;
import me.puneetghodasara.txmgr.model.db.Transaction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");
		TransactionRepository transactionRepository = (TransactionRepository) appContext.getBean("transactionRepository");
		
		List<Transaction> allTransactions = transactionRepository.getAllTransactions();
		/*StatementManager myMananger = new GenericStatementManager();
		String statementFileName = null;
		Account account = null;
		try {
			List<Transaction> statementEntries = myMananger.getStatementEntries(account, statementFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
