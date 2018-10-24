package com.unisys.br.amsfw.web.faces;

/**
 * Retorna a descricao desejada de acordo com o objeto passado como parametro.
 * 
 * @author JesusRJ
 */
public interface ObjectDescription {

	/**
	 * Recupera a descrição.
	 * 
	 * @param x
	 * @return
	 */
	String getDescription(Object x);

}
