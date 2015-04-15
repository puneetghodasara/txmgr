package model.input;

import java.util.List;

import exception.MessageException;
import model.db.Account;
import model.db.Transaction;

public interface EntryParser {

	List<Transaction> convertEntries(Account account, List<GenericStatementEntry> statementEntryList) throws MessageException;

}
