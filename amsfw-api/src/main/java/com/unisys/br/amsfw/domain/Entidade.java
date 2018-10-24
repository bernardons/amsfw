package com.unisys.br.amsfw.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Classe que deve ser extendida por toda classe persistente. Esta classe já
 * possui atributos como o id sequencial, a versão da entidade, entre outras. <br>
 * Implementa <i>toString()</i> - mas deixa a implementação de <i>equals()</i> e
 * <i>hashCode()</i> para cada especialização.
 * 
 * @author delfimsm
 * 
 */
@MappedSuperclass
public abstract class Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Retorna o id da entidade selecionada.
	 * 
	 * @return
	 */
	@Transient
	public abstract Serializable getId();

	/**
	 * Retorna nome da classe concatenado com seu id.
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " : id = " + this.getId();
	}

}