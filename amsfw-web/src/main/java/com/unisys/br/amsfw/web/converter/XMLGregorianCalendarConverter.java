package com.unisys.br.amsfw.web.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Conversor de XML Gregorian Calendar.
 * 
 * @author DelfimSM
 * 
 */
@FacesConverter("xmlGregorianCalendarConverter")
public class XMLGregorianCalendarConverter implements Converter {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (value == null || value.trim().length() < 1) {
			return null;
		}

		try {

			Date data = sdf.parse(value);

			GregorianCalendar gCal = new GregorianCalendar();
			gCal.setTimeInMillis(data.getTime());

			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal);

		} catch (Exception e) {

			FacesUtils.addErrorMessage(e, "Data Inválida");
			throw new ConverterException();
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value == null) {
			return null;
		}

		XMLGregorianCalendar xmlCalendar = (XMLGregorianCalendar) value;

		return sdf.format(xmlCalendar.toGregorianCalendar().getTime());

	}

}
