package com.unisys.br.amsfw.web.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Converter para Datas.
 * 
 * @author LelesRaJ
 * 
 */
@FacesConverter(value = "br.unisys.DateMesAnoConverter")
public class DataMesAnoConverter implements Converter {

	private static final int TAMANHO_DATA = 7;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (value == null || value.equals("")) {
			return null;
		}

		if (value.length() < TAMANHO_DATA) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data incorreta.",
					"Favor informar uma data no padrão mm/aaaa."));
		}

		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value == null || value.equals("")) {
			return null;
		}

		if (value.toString().length() < TAMANHO_DATA) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data incorreta.",
					"Favor informar uma data no padrão MM/yyyy."));
		}
		return value.toString();

	}

}
