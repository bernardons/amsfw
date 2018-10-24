package com.unisys.br.amsfw.web.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Exception Handler Factory.
 * 
 * @author DelfimSM
 *
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
	
	private ExceptionHandlerFactory parent;

	/**
	 * Construtor de Exception Handler.
	 * 
	 * @param parent
	 */
	public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {

		ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());

		return handler;
	}

}
