package com.unisys.br.amsfw.ws.processamento;

import javax.ejb.Local;

/**
 * Interface local de processamento.
 * 
 * @author DelfimSM
 *
 */
@Local
public interface ProcessamentoWSLocal {

	/**
	 * Executar processamento do serviço.
	 * 
	 * @param tipoProcessamento
	 * @return
	 */
	void executarProcessamento(int tipoProcessamento);

}
