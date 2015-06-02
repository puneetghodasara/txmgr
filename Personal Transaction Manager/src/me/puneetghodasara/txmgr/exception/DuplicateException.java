package me.puneetghodasara.txmgr.exception;

public class DuplicateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateException(String duplicateName) {
		super(duplicateName);
	}

}
