package com.unisys.br.amsfw.dao.util.service.email;

import javax.ejb.Local;

import com.unisys.br.amsfw.domain.Email;


/**
 * Interface Serviço de email do framework amsfw.
 * 
 * @author AroAL
 * 
 */
@Local
public interface EmailServiceLocal {

	/**
	 * Envia emai para o sistema.
	 * 
	 * @param email
	 */
	void enviarEmail(Email email);

	/**
	 * Envia email com anexo.
	 * 
	 * @author RegoIanC
	 * 
	 * @param email
	 */
	void enviarEmailAnexo(Email email);

}