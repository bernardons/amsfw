package com.unisys.br.amsfw.domain;

/**
 * Entidade VO para retornar a linha a ser impressa e o contador, para casos
 * onde seja impresso alem da linha o seu relacionamento 1-N.
 * 
 * @author ReisOtaL
 * 
 */
public class ObjetoImpressaoTxt {

	private String linha;
	private int contador;

	public String getLinha() {
		return linha;
	}

	public void setLinha(String linha) {
		this.linha = linha;
	}

	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}

}
