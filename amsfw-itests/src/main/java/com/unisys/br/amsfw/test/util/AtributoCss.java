package com.unisys.br.amsfw.test.util;

/**
 * 
 * @author FlanmarP
 *
 */
public class AtributoCss {

	private String atributo;

	public String getCss() {
		return atributo;
	}

	public AtributoCss(String tag, String atributo, String valor) {
		tag += "[" + atributo + "=\"" + valor + "\"]";
		this.atributo = tag;
	}
}
