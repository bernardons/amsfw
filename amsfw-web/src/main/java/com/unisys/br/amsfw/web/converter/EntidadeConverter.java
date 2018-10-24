package com.unisys.br.amsfw.web.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.unisys.br.amsfw.domain.Entidade;

/**
 * Converter para entidades genericas.
 * 
 * @author LelesRaJ
 * 
 */
@FacesConverter(value = "br.unisys.EntityConverter", forClass = Entidade.class)
public class EntidadeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (value != null) {
			return this.getAttributesFrom(component).get(value);
		}
		return null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		if (value != null && !"".equals(value)) {
			Entidade entity = (Entidade) value;

			if (entity.getId() != null) {
				this.addAttribute(component, entity);

				if (entity.getId() != null) {
					return String.valueOf(entity.getId());
				}
				return (String) value;
			}
		}
		return "";

	}

	private void addAttribute(UIComponent component, Entidade entity) {
		this.getAttributesFrom(component).put(entity.getId().toString(), entity);
	}

	private Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

}
