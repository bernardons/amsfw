package com.unisys.br.amsfw.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor d eString.
 * 
 * @author DelfimSM
 *
 */
@FacesConverter(forClass = String.class)
public class StringConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value != null ? value.trim() : value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value != null ? value.toString().trim() : null;
	}

}
