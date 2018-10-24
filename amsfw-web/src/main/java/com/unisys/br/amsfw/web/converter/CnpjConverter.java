package com.unisys.br.amsfw.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor de CNPJ.
 * 
 * @author DelfimSM
 *
 */
@FacesConverter("br.unisys.CnpjConverter")
public class CnpjConverter implements Converter {

	private static final int TAMANHO_CNPJ = 14;
	private static final int DOZE = 12;
	private static final int OITO = 8;
	private static final int CINCO = 5;
	private static final int DOIS = 2;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent componente, String newValue) {

		String semformato = null;
		if (newValue != null) {
			semformato = newValue.trim().replaceAll("[. / -]", "");
		}

		return semformato;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object valor) {

		if (valor.toString().isEmpty()) {
			return "";
		}

		String placeHolder = "%-2s.%-3s.%-3s/%-4s-%-2s";
		String val = (String) valor;

		val = "0000000000000" + val.trim();
		val = val.substring(val.length() - TAMANHO_CNPJ);

		return String.format(
				placeHolder,
				val.subSequence(0, DOIS),
				val.substring(DOIS, CINCO),
				val.substring(CINCO, OITO),
				val.substring(OITO, DOZE),
				val.substring(DOZE, TAMANHO_CNPJ));
	}

}
