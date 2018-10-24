package br.com.unisys.modelo;

/**
 * Enumeração para o Tipo de Geração.
 * 
 * @author DelfimSM
 *
 */
public enum TipoGeracaoEnum {

	CRUD_COMPLETO("Crud Completo"),
	SOMENTE_PESQUISA("Somente Pesquisa"),
	SOMENTE_CADASTRO("Somente Cadastro"),
	DAO_DAO_LOCAL("Somente DAO e DAO Local");

	private String label;

	private TipoGeracaoEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
