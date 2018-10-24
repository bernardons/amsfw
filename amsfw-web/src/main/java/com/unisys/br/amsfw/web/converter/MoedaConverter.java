package com.unisys.br.amsfw.web.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;

import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Conveter para moedas.
 * 
 * @author LelesRaJ
 * 
 */
@FacesConverter(value = "br.unisys.MoedasConverter")
public class MoedaConverter implements Converter {

	private static final String DATA_INVALIDA = "Data inválida";

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.trim().equals("")) {
			return 0.0D;
		}

		NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
		nf.setMinimumFractionDigits(2);
		Number number = null;

		try {
			number = nf.parse(value);
			return new BigDecimal(number.doubleValue());

		} catch (Exception e) {
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(DATA_INVALIDA + " "
					+ e.getMessage()));

		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (object == null || object.toString().trim().equals("")) {
			return "0.00";
		} else {
			NumberFormat nF = NumberFormat.getInstance(FacesContext.getCurrentInstance().getViewRoot().getLocale());
			nF.setMaximumFractionDigits(2);
			nF.setMinimumFractionDigits(2);

			return nF.format(Double.valueOf(object.toString()));
		}
	}

}
