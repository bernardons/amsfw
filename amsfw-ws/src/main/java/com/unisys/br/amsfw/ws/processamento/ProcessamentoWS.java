package com.unisys.br.amsfw.ws.processamento;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;

import com.unisys.br.amsfw.service.processamento.ProcessamentoBatchAssincronoLocal;

/**
 * WebService de processamento.
 * 
 * @author DelfimSM
 * 
 */
@Stateless
@WebService
public class ProcessamentoWS implements ProcessamentoWSLocal {

	@EJB
	private ProcessamentoBatchAssincronoLocal processaAssincrono;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void executarProcessamento(int tipoProcessamento) {
		processaAssincrono.executarProcessamento(1);
	}

}
