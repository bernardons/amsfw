package com.unisys.br.amsfw.ws.processamento;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.unisys.br.amsfw.service.processamento.ProcessamentoBatchAssincronoLocal;

/**
 * WebService de processamento.
 * 
 * @author DelfimSM
 * 
 */
public abstract class ProcessamentoWS implements ProcessamentoWSLocal {

	@EJB
	private ProcessamentoBatchAssincronoLocal processaAssincrono;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	protected void executarProcessamentoGeral(int tipoProcessamento) {
		processaAssincrono.executarProcessamento(tipoProcessamento);
	}
	
}
