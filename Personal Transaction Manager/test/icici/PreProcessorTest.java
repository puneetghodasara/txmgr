package icici;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import main.Main;
import me.puneetghodasara.txmgr.integration.TransactionRepository;
import me.puneetghodasara.txmgr.manager.AccountManager;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.CreditDebitEnum;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.db.TransactionDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PreProcessorTest {

	private AccountManager accountManager;
	private Account iciciAcc;
	private Transaction tx;
	private TransactionRepository transactionRepository;
	
	@Before
	public void setupAccounts() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");
		accountManager = (AccountManager) appContext.getBean("accountManager");
		transactionRepository = (TransactionRepository) appContext.getBean("transactionRepository");

		iciciAcc = accountManager.getAccountByName("ICICI");
		
		tx = new Transaction();
	}

	@After
	public void tearupAccounts() {
		transactionRepository.deleteTransaction(tx);
	}

	@Ignore
	@Test
	public void testListFiles() throws Exception {
		Main main = new Main();
		HashMap<Account, Set<String>> allStatements = main.getAllStatements();
		allStatements.entrySet().stream().forEach(e -> {
			System.out.println("Account :" + e.getKey() + " - " + e.getValue());
		});
	}
	
	
	@Test
	public void testTransaction(){
		
		tx.setAccount(iciciAcc);
		tx.setAmount(200d);
		tx.setCreditDebit(CreditDebitEnum.CREDIT);
		tx.setDate(new Date());
		TransactionDetail txDtl = new TransactionDetail();
		txDtl.setCategory("Cat");
		txDtl.setMerchant("mer");
		tx.setTransactionDetail(txDtl);
		
		transactionRepository.saveTransaction(tx);
	}
}
