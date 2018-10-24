package com.unisys.br.amsfw.exception;

/**
 * Exception geral do framework que é tratada pela camada de visão.
 * 
 * @author DelfimSM
 * 
 */
public class AmsfwRedirectException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor da exceção de sistema.
	 * 
	 * @param msg
	 * @param e
	 */
	public AmsfwRedirectException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Construtor da exceção de sistema.
	 * 
	 * @param msg
	 * @param e
	 */
	public AmsfwRedirectException(String msg) {
		super(msg);
	}

	/**
	 * Construtor da exceção de sistema.
	 * 
	 */
	public AmsfwRedirectException() {
		super();
	}

}
