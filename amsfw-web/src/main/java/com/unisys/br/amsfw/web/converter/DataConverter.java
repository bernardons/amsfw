package com.unisys.br.amsfw.web.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@FacesConverter(value = "br.unisys.DateConverter")
public class DataConverter implements Converter {

	private static DateFormat FORMAT_DATE = null;

	/**
	 * Seta o formato da data de acordo com o locale.
	 */
	public DataConverter() {
		FORMAT_DATE = new SimpleDateFormat(getFormatoData());
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (value == null || value.equals("")) {
			return null;
		}

		Date dataConvertida = null;
		try {
			dataConvertida = FORMAT_DATE.parse(value);

		} catch (ParseException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Data incorreta.",
						"Favor informar uma data correta."));
		}
		return dataConvertida;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		Date data = (Date) value;
		return FORMAT_DATE.format(data);

	}

	private String getFormatoData() {

		String country = FacesContext.getCurrentInstance().getViewRoot().getLocale().getCountry();

		if (country.equalsIgnoreCase("BR")) {
			return "dd/MM/yyyy";
		} else {
			return "yyyy-MM-dd";
		}

	}

}
