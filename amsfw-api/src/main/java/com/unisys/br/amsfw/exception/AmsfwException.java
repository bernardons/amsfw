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

	private String[] parametros;

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
	 * @param msg
	 * @param e
	 */
	public AmsfwException(String msg, String[] parametros) {
		super(msg);
		this.chave = msg;
		this.parametros = parametros;
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

	public String[] getParametros() {
		return parametros;
	}

	public void setParametros(String[] parametros) {
		this.parametros = parametros;
	}

}
