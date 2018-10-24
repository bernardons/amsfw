package com.unisys.br.amsfw.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entidade VO para retornar a linha a ser impressa e o contador, para casos
 * onde seja impresso alem da linha o seu relacionamento 1-N.
 * 
 * @author ReisOtaL
 * 
 */
public class ObjetoImpressaoTxt {

	private boolean isMultiplosArquivos;

	private Map<String, ObjetoImpressaoTxt> mapArquivoObjeto;

	private String linha;

	private int contador;
	
	private String cabecalho;

	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

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

	public boolean isMultiplosArquivos() {
		return isMultiplosArquivos;
	}

	public void setMultiplosArquivos(boolean isMultiplosArquivos) {
		this.isMultiplosArquivos = isMultiplosArquivos;
	}

	public Map<String, ObjetoImpressaoTxt> getMapArquivoObjeto() {
		return mapArquivoObjeto;
	}

	public void setMapArquivoObjeto(Map<String, ObjetoImpressaoTxt> mapArquivoObjeto) {
		this.mapArquivoObjeto = mapArquivoObjeto;
	}

	public void adicionarLinhaContadorObjeto(String chave, String linha, int contador, String cabecalho) {
		if (mapArquivoObjeto == null) {
			mapArquivoObjeto = new HashMap<String, ObjetoImpressaoTxt>();
		}
		ObjetoImpressaoTxt objeto;
		if (!mapArquivoObjeto.containsKey(chave)) {
			objeto = new ObjetoImpressaoTxt();
			objeto.setLinha("");
			objeto.setCabecalho(cabecalho + "\r\n");
			objeto.setContador(0);
			mapArquivoObjeto.put(chave, objeto);
		} else {
			objeto = mapArquivoObjeto.get(chave);
		}
		objeto.setLinha(objeto.getLinha() + linha);
		objeto.setContador(objeto.getContador() + contador);
	}

	public void adicionarConteudoAoObjeto(ObjetoImpressaoTxt objetoImpressaoTxt) {
		StringBuilder sb = new StringBuilder(linha == null ? "" : linha);
		sb.append(objetoImpressaoTxt.getLinha());
		contador += objetoImpressaoTxt.getContador();
		linha = sb.toString().replaceAll("@#", "@");
	}
	
	public void adicionarConteudoAoObjetoComQuebraLinha(ObjetoImpressaoTxt objetoImpressaoTxt) {
		StringBuilder sb = new StringBuilder(linha == null ? "" : linha);
		sb.append(objetoImpressaoTxt.getLinha() + "\r\n");
		contador += objetoImpressaoTxt.getContador();
		linha = sb.toString();
	}

	public void adicionarContadorAoRodape() {
		StringBuilder sb = new StringBuilder(linha == null ? "" : linha);
		sb.append(contador);
		linha = sb.toString();
	}
	
	public void adicionarCabecalhoData() {
		StringBuilder export = new StringBuilder(linha == null ? "" : linha);
		export.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		export.append("\r\n");
		linha = export.toString();
	}

}
