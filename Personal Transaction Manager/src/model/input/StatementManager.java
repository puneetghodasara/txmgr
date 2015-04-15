package model.input;

import java.util.List;

import model.db.Account;
import model.db.Transaction;

public interface StatementManager {

	List<Transaction> getStatementEntries(Account account, String statementFileName) throws Exception;

}
