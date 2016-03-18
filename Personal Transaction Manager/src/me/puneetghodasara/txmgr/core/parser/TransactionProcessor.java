package me.puneetghodasara.txmgr.core.parser;

import java.util.List;

import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.db.TransactionDetail;

public interface TransactionProcessor {

	TransactionDetail getTransferDetail(Rule matchedRule, String txDesc);

	TransactionDetail getExpenseDetail(Rule matchedRule);

	Rule getMatchingRule(Transaction transaction);

}
