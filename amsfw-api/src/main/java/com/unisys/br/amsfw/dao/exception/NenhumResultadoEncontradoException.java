package com.unisys.br.amsfw.dao.exception;

/**
 * Exceção de nenhum resultado encontrado.
 * 
 * @author SilvaMaP
 *
 */
public class NenhumResultadoEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor da excecao nenhum resultado encontrado sem parametro.
	 * 
	 */
	public NenhumResultadoEncontradoException() {
		super("Nenhum resultado encontrado!");
	}

}
