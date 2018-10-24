package com.unisys.br.amsfw.exception;

/**
 * Exception que reperensata falhas na chamada ao serviço de login.
 * 
 * Estas falhas podem ser por login inválido ou algum problema no serviço
 * chamado.
 * 
 * @author CoelhoGP
 * 
 */
public class LoginException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor da exceção de login.
	 * 
	 * @param msg
	 * @param e
	 */
	public LoginException(String msg, Throwable e) {
		super(msg, e);
	}

}
