package com.unisys.br.amsfw.test.util;

/**
 * Classe para gerar String para testar o tamanho dos campos.
 * 
 * @author FlanmarP
 * 
 */
public class GeraCharacter {

	private GeraCharacter() {
	}

	/**
	 * gera strings para teste.
	 * 
	 * @author FlanmarP
	 */
	public static StringBuilder geraCharacter(int tamnhoDaString) {
		StringBuilder saida = new StringBuilder();
		for (int i = 0; i < tamnhoDaString; i++) {
			saida.append("W");
		}
		return saida;
	}

	/**
	 * gera inteiros para teste.
	 * 
	 * @author FlanmarP
	 */
	public static StringBuilder geraInteiros(int tamnhoDoInteiro) {
		StringBuilder saida = new StringBuilder();
		for (int i = 0; i < tamnhoDoInteiro; i++) {
			saida.append("9");
		}
		return saida;
	}
}
