package com.unisys.br.amsfw.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor de Cep.
 * 
 * @author DelfimSM
 *
 */
@FacesConverter("br.unisys.CepConverter")
public class CepConverter implements Converter {

	private static final int INDICE_SEPARADOR = 5;
	private static final int TAMANHO_TOTAL_CAMPO = 8;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		String str = value.replace("-", "").replace(".", "");

		if (str.isEmpty()) {
			return "";
		}

		return new Integer(str);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value == null) {
			return "";
		}

		String val = "00000000" + String.valueOf(value);
		String cep = val.substring(val.length() - TAMANHO_TOTAL_CAMPO);

		return cep.substring(0, INDICE_SEPARADOR) + "-" + cep.substring(INDICE_SEPARADOR);

	}

}
