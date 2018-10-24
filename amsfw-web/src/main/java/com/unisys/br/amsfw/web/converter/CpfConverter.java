package com.unisys.br.amsfw.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor de CPF.
 * 
 * @author DelfimSM
 * 
 */
@FacesConverter("br.unisys.CPFConverter")
public class CpfConverter implements Converter {

	private static final int TRES = 3;
	private static final int SEIS = 6;
	private static final int NOVE = 9;
	private static final int TAMANHO_CPF = 11;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent componente, String newValue) {

		String semformato = null;
		if (newValue != null && !newValue.isEmpty()) {
			semformato = newValue.trim().replaceAll("[. -]", "");
		}

		return semformato;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object valor) {

		if (valor.toString().isEmpty()) {
			return "";
		}

		String placeHolder = "%-3s.%-3s.%-3s-%-2s";
		String val = (String) valor;
		val = "00000000000" + val.trim();
		val = val.substring(val.length() - TAMANHO_CPF);

		return String.format(
				placeHolder,
				val.subSequence(0, TRES),
				val.substring(TRES, SEIS),
				val.substring(SEIS, NOVE),
				val.substring(NOVE));
	}

}
