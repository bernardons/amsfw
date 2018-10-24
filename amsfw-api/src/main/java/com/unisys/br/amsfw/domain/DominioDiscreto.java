package com.unisys.br.amsfw.domain;


/**
 * Classe para representar todos os dominios discretos(classes do tipo ID-DESCRICAO).
 * 
 * @author silvamap
 * 
 */
public abstract class DominioDiscreto extends Entidade {

	private static final long serialVersionUID = 1L;
	
	public abstract String getDescricao();
	
}
