package com.unisys.br.amsfw.domain;

/**
 * Interface para impressao de entidade.
 * 
 * @author ReisOtaL
 * 
 */
public interface ImpressaoTxtInterface {

	String SEPARADOR = "@#";

	/**
	 * Metodo que deve ser implementado para impressao da linha da Entidade,
	 * quando houver uma lista associada, a impressao deve ocorrer dentro deste
	 * metodo e o contador de retorno deve ser incrementado.
	 * 
	 * Quando um atributo da classe contiver join, deve ser feito um novo
	 * mapeamento com a chave do relacionamento e que seja somente leitura.
	 * Exemplo:
	 * <br>
	 * ManyToOne(fetch = FetchType.LAZY)<br> JoinColumns({<br> JoinColumn(name =
	 * "nu_natural_unidade", referencedColumnName = "nu_natural"),<br>
	 * JoinColumn(name = "nu_unidade", referencedColumnName = "nu_unidade") })<br>
	 * private UnidadeSigro unidadeCenario;
	 * 
	 * 
	 * 
	 * O atributo unidade deveria ser mapeado da seguinte forma: <br>Column(name =
	 * "nu_unidade", columnDefinition = "int2", insertable = false, updatable =
	 * false) <br>private Integer numeroUnidade; <br>Column(name = "nu_natural_unidade",
	 * insertable = false, updatable = false)<br> private Integer
	 * numeroNaturalUnidade;
	 * 
	 * @return
	 */
	ObjetoImpressaoTxt imprimirEntidade();

}
