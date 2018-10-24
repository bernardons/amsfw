package com.unisys.br.amsfw.test.testbuilder;

import java.util.Collection;

/**
 * Interface do contrutor de objetos de teste que mostra o conjunto de métodos que deve ser
 * implementados.
 * 
 * @author DelfimSM
 * 
 */
public interface TestBuilder {

	/**
	 * Método a ser implementado para recuperar as dependências do projeto.
	 * 
	 * @return
	 */
	Collection<Class<? extends TestBuilder>> getDependencias();

	/**
	 * Método a ser implementado para recuperar os nomes dos arquivos XLS ou XML.
	 * 
	 * @return
	 */
	Collection<String> getNomesArquivosDados();

}
