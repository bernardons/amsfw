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

	// regular expression para validação do cpf caso precise um dia.
	// ^[0-9]{3}\.[0-9]{3}\.[0-9]{3}\.[0-9]{2}$

	// 00.000.000/0001-00

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
		val = val.substring(val.length() - 11);

		return String.format(
				placeHolder,
				val.subSequence(0, 3),
				val.substring(3, 6),
				val.substring(6, 9),
				val.substring(9));
	}

}
