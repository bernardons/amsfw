package com.unisys.br.amsfw.exception;

/**
 * 
 * Esta classe representa uma exceção do causada por algum erro inesperado do
 * Sistema. Ou seja algum erro não previsto no negócio, que impediu o serviço
 * chamado de retornar o objeto esperado.
 * 
 * 
 * @author Guilherme Coelho
 * 
 */
public class SystemException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor da exceção de sistema.
	 * 
	 * @param msg
	 * @param e
	 */
	public SystemException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Construtor da exceção de sistema.
	 * 
	 */
	public SystemException() {
		super();
	}

}
