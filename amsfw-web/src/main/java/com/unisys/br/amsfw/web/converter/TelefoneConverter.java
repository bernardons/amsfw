package com.unisys.br.amsfw.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor de telefone.
 * 
 * @author DelfimSM
 * 
 */
@FacesConverter("br.unisys.TelefoneConverter")
public class TelefoneConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value.trim().equals("") || value == null) {
			return "";
		}
		return value.replaceAll("[( ) -]", "");
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value.toString().isEmpty()) {
			return "";
		}

		if (value instanceof Integer && ((Integer) value) == 0) {
			return "";
		}

		// String val = "00000000" + String.valueOf(value);
		// String cep = val.substring(val.length() - 8);

		return String.valueOf(value);

	}

}
