package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.Predicate;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Classe utilitária geral do sistema.
 * 
 * @author DelfimSM
 *
 */
public class Util {

	private Util() {
		super();
	}

	/**
	 * Monta a data de acordo com a string passada como parâmetro.
	 * 
	 * @param data
	 * @return
	 */
	public static XMLGregorianCalendar montaData(String data) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		GregorianCalendar gCalendar = new GregorianCalendar();
		XMLGregorianCalendar cal = null;
		try {

			gCalendar.setTime(sdf.parse(data));
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
			return cal;

		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Método que realiza o parse de uma data para XML.
	 * 
	 * @param data
	 * @return
	 */
	public static XMLGregorianCalendar parseToXMLGregorianCalendar(Date data) {

		GregorianCalendar gCalendar = new GregorianCalendar();
		XMLGregorianCalendar cal = null;
		try {

			gCalendar.setTime(data);
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
			return cal;

		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Método que realiza o parse de uma data.
	 * 
	 * @param data
	 * @return
	 */
	public static Date parseToDate(String data) {

		if (data == null || data.trim().length() == 0) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			return sdf.parse(data);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Método que formata a data para diamesano.
	 * 
	 * @param data
	 * @return
	 */
	public static String formatDDMMYYYY(Date data) {
		if (data == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

	/**
	 * Método de formatação de data.
	 * 
	 * @param data
	 * @param pattern
	 * @return
	 */
	public static String formataData(Date data, String pattern) {
		if (data == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(data);
	}

	/**
	 * Método de formatação de Moeda.
	 * 
	 * @param valor
	 * @return
	 */
	public static String formataMoeda(Double valor) {

		if (valor == null) {
			return "0,00";
		}

		DecimalFormat dFormat = new DecimalFormat("#,###.##");
		dFormat.setMinimumFractionDigits(2);
		return dFormat.format(valor.doubleValue());
	}

	/**
	 * Método de formatação de moeda.
	 * 
	 * @param valor
	 * @return
	 */
	public static String formataMoeda(BigDecimal valor) {

		return formataMoeda(valor.doubleValue());

	}
	
	
	/**
	 * Método de formatação de moeda.
	 * 
	 * @param valor
	 * @return
	 */
	public static String formataMoeda(String valor) {

		if (valor == null) {
			return "0,00";
		}

		DecimalFormat dFormat = new DecimalFormat("#,###.##");
		dFormat.setMinimumFractionDigits(2);
		return dFormat.format(Double.parseDouble(valor.replace(",", "."))); 

	}

	/**
	 * Método que formata uma data para XML.
	 * 
	 * @param data
	 * @return
	 */
	public static String formataDataXML(XMLGregorianCalendar data) {
		return formatDDMMYYYY(data.toGregorianCalendar().getTime());
	}

	/**
	 * Método que converte uma data para um XML Gregorian Calendar.
	 * 
	 * @param data
	 * @return
	 */
	public XMLGregorianCalendar toXmlDate(String data) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdf.parse(data);

			return toXmlDate(d);
		} catch (ParseException e) {
			return null;
		}

	}

	/**
	 * Método que pega a data e formata como XML.
	 * 
	 * @param data
	 * @return
	 */
	public XMLGregorianCalendar toXmlDate(Date data) {
		try {

			GregorianCalendar greg = new GregorianCalendar();
			greg.setTime(data);

			return DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);

		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	/**
	 * Método que verifica se o email é válido.
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmailValido(String email) {

		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return validaRegex(email, regex);

	}

	/**
	 * Método que valida se um regex é válido.
	 * 
	 * @param valor
	 * @param regex
	 * @return
	 */
	public static boolean validaRegex(String valor, String regex) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(valor);
		return m.matches();

	}

	/**
	 * Formata Cep.
	 * 
	 * @param cep
	 * @return
	 */
	public static String formataCEP(String cep) {
		if (cep == null) {
			return null;
		}
		String val = "00000000" + cep.trim();
		cep = val.substring(val.length() - 8);
		return cep.substring(0, 5) + "-" + cep.substring(5);

	}
	
	/**
	 * Converter lista de Predicate para array.
	 * @param lista
	 * @return
	 */
	public static Predicate[] toArray(List<Predicate> lista) {

		Predicate[] array = new Predicate[lista.size()];

		for (int i = 0; i < lista.size(); i++) {
			array[i] = lista.get(i);
		}

		return array;
	}
	
	/**
	 * Remove zeros a esquerda da String passada por parametro.
	 * Sendo a String um valor numerico.
	 * @param filtro
	 * @return
	 */
	public static String removerZerosEsquerda(String filtro) {
		Long numeroSemZeros = 0L;
		try {
			numeroSemZeros = Long.parseLong(filtro.trim());
			filtro = numeroSemZeros.toString();
		} catch (Exception e) {
			System.out.println("");
		}
		return filtro;

	}

}
