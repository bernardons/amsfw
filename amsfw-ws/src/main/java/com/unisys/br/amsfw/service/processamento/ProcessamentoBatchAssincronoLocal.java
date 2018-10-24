package com.unisys.br.amsfw.service.processamento;

import javax.ejb.Local;

/**
 * Interface local de processamento assincrono.
 * 
 * @author DelfimSM
 *
 */
@Local
public interface ProcessamentoBatchAssincronoLocal {

	/**
	 * Executar processamento do serviço.
	 * 
	 * @param tipoProcessamento
	 * @return
	 */
	void executarProcessamento(int tipoProcessamento);

}
