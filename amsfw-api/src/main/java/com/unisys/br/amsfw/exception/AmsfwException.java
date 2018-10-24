package com.unisys.br.amsfw.exception;

/**
 * Exception geral do framework que é tratada pela camada de visão.
 * 
 * @author DelfimSM
 * 
 */
public class AmsfwException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String chave;

	/**
	 * Construtor da exceção de sistema.
	 * 
	 * @param msg
	 * @param e
	 */
	public AmsfwException(String msg, Throwable e) {
		super(msg, e);
		this.chave = msg;
	}

	/**
	 * Construtor da exceção de sistema.
	 * 
	 * @param msg
	 * @param e
	 */
	public AmsfwException(String msg) {
		super(msg);
		this.chave = msg;
	}

	/**
	 * Construtor da exceção de sistema.
	 * 
	 */
	public AmsfwException() {
		super();
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

}
