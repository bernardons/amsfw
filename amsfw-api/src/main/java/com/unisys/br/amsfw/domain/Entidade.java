package com.unisys.br.amsfw.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;



/**
 * Classe que deve ser extendida por toda classe persistente. Esta classe já
 * possui atributos como o id sequencial, a versão da entidade, entre outras.
 * <br>Implementa <i>toString()</i> - mas deixa a implementação de <i>equals()</i>
 * e <i>hashCode()</i> para cada especialização.
 * 
 * @author delfimsm
 * 
 */
@MappedSuperclass
public abstract class Entidade implements Serializable {

	private static final long serialVersionUID = 6902278226581541263L;

//	@Version
//	@Column(name = "AUDI_IDE_VERSAO_REGISTRO")
//	private Long version;
//
//	@Column(name = "AUDI_SEQ_ID_USUARIO_INC_REG")
//	private Integer usuarioInclusao;
//
//	@Column(name = "AUDI_SEQ_ID_USUARIO_ALT_REG")
//	private Integer usuarioAlteracao;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "AUDI_TMS_INCLUI_REGISTRO")
//	private Date horarioInclusao;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "AUDI_TMS_ALTERA_REGISTRO")
//	private Date horarioAlteracao;

	/**
	 * Retorna o id da entidade selecionada.
	 * 
	 * @return
	 */
	@Transient
	public abstract Serializable getId();

//	public Long getVersion() {
//		return version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}
//
//	public Integer getUsuarioInclusao() {
//		return usuarioInclusao;
//	}
//
//	public void setUsuarioInclusao(Integer usuarioInclusao) {
//		this.usuarioInclusao = usuarioInclusao;
//	}
//
//	public Integer getUsuarioAlteracao() {
//		return usuarioAlteracao;
//	}
//
//	public void setUsuarioAlteracao(Integer usuarioAlteracao) {
//		this.usuarioAlteracao = usuarioAlteracao;
//	}
//
//	public Date getHorarioInclusao() {
//		return horarioInclusao == null ? null : (Date) horarioInclusao.clone();
//	}
//
//	public void setHorarioInclusao(Date horarioInclusao) {
//		this.horarioInclusao = (horarioInclusao == null ? null : (Date) horarioInclusao.clone());
//	}
//
//	public Date getHorarioAlteracao() {
//		return horarioAlteracao == null ? null : (Date) horarioAlteracao.clone();
//	}
//
//	public void setHorarioAlteracao(Date horarioAlteracao) {
//		this.horarioAlteracao = (horarioAlteracao == null ? null : (Date) horarioAlteracao.clone());
//	}

	/**
	 * Retorna nome da classe concatenado com seu id.
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " : id = " + this.getId();
	}

//	/**
//	 * Preenche horarioInclusao com Data/Hora atual do sistema.
//	 */
//	@PrePersist
//	public void preencheDadosInsercao() {
//		this.horarioInclusao = DateUtil.getDataAtualHoraMinuto();
//	}
//
//	/**
//	 * Preenche horarioAlteracao com Data/Hora atual do sistema.
//	 */
//	@PreUpdate
//	public void preencheDadosAtualizacao() {
//		this.horarioAlteracao = DateUtil.getDataAtualHoraMinuto();
//	}

}