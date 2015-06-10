package me.puneetghodasara.txmgr.model.parser;

import java.util.List;
import java.util.stream.Collectors;

import me.puneetghodasara.txmgr.integration.RuleRepository;
import me.puneetghodasara.txmgr.model.db.Rule;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.db.TransactionDetail;
import me.puneetghodasara.txmgr.util.UnmatchedTransactionWriter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "transactionParser")
public class GenericTransactionParser implements TransactionParser {

	private static final Logger logger = Logger.getLogger(GenericTransactionParser.class);

	@Autowired
	private RuleRepository ruleRepository;

	@Override
	public void parseTransaction(Transaction transaction) {
		logger.debug("Parsing Transaction " + transaction);

		final String txDesc = transaction.getDescription();
		Rule matchedRule = null;

		List<Rule> matchedRuleList = ruleRepository.getAllRules()
				.stream()
				.filter(rule -> (txDesc.contains(rule.getRule())))
				.collect(Collectors.toList());

		if(matchedRuleList.size()==1){
			matchedRule = matchedRuleList.get(0);
		}else if(matchedRuleList.size()>1){
			// Write logic for best match
			matchedRule = getBestMatchingRule(transaction,matchedRuleList);
		}
		
		
		if (matchedRule != null) {
			TransactionDetail txDetail = getTransactionDetail(matchedRule);
			transaction.setTransactionDetail(txDetail);
		}else{
			// ask for a rule
			logger.info("No match for "+transaction);
			UnmatchedTransactionWriter.write(transaction);
			
		}

		logger.debug("Transaction Parsed " + transaction);
	}

	private Rule getBestMatchingRule(Transaction transaction, List<Rule> matchedRuleList) {
		// TODO Auto-generated method stub
		return matchedRuleList.get(0);
	}

	private TransactionDetail getTransactionDetail(Rule rule) {
		TransactionDetail txDetail = new TransactionDetail();
		txDetail.setCategory(rule.getCategory());
		txDetail.setMerchant(rule.getMerchant());
		return txDetail;
	}

}
