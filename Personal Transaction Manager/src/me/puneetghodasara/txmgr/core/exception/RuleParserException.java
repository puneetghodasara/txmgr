package me.puneetghodasara.txmgr.core.exception;

import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;

public class RuleParserException extends GenericException {

	public RuleParserException(ExceptionType errorType) {
		super(errorType);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
