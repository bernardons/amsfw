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
		val = val.substring(val.length() - 14);

		return String.format(
				placeHolder,
				val.subSequence(0, 2),
				val.substring(2, 5),
				val.substring(5, 8),
				val.substring(8, 12),
				val.substring(12, 14));
	}

}
