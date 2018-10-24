package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.DefaultFieldDecorator;
import com.github.ffpojo.metadata.FieldDecorator;

/**
 * 
 * @author ReisOtaL
 *
 */
public class FfPojoDecoratorUtil {
	
	
	private static String mesAno = "yyyyMM";
	private static String diaMesAno = "ddMMyyyy";
	private static String anoMesDia = "yyyyMMdd";
	
	/**
	 * Decorator de Data.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class DateMesAnoDecorator implements FieldDecorator<Date> {
		/**
		 * Converte de string para data.
		 * 
		 */
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || str.trim().equals("") ? null : new SimpleDateFormat(mesAno).parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		/**
		 * Converte uma data para String.
		 */
		public String toString(Date obj) {
			Date date = (Date) obj;
			return new SimpleDateFormat(mesAno).format(date);
		}
	}

	/**
	 * Decorator de Data.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class DateDecorator implements FieldDecorator<Date> {

		/**
		 * Converte de string para data.
		 * 
		 */
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || str.trim().equals("") ? null : new SimpleDateFormat(anoMesDia).parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		/**
		 * Converte uma data para String.
		 */
		public String toString(Date obj) {
			Date date = (Date) obj;
			return new SimpleDateFormat(anoMesDia).format(date);
		}
	}
	
	/**
	 * Decorator de Data.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class DateDecoratorDiaMesAno implements FieldDecorator<Date> {

		/**
		 * Converte de string para data.
		 * 
		 */
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || str.trim().equals("") ? null : new SimpleDateFormat(diaMesAno).parse(str.replace(".", "")
						.replace("-", "").replace("/", ""));
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		/**
		 * Converte uma data para String.
		 */
		public String toString(Date obj) {
			Date date = (Date) obj;
			return new SimpleDateFormat(diaMesAno).format(date);
		}
	}


	/**
	 * Decorator de Data.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class DateTimeDecorator implements FieldDecorator<Date> {
		/**
		 * Converte de String para Data.
		 */
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || str.equals("") ? null : DateUtil.formataDataHoraMinutoSemSeparadorParser(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
			}
		}

		/**
		 * Converte de Data para String.
		 */
		public String toString(Date obj) {
			Date date = (Date) obj;
			return DateUtil.getDataHoraMinutoSemSeparador(date);
		}
	}

	/**
	 * Decorator de Data.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class ValorCincoCasasDecorator implements FieldDecorator<BigDecimal> {

		/**
		 * Converte de String para BigDecimal.
		 */
		public BigDecimal fromString(String str) throws FieldDecoratorException {
			String parteNumerica = str.substring(0, str.length() - 5);
			String parteDecimal = str.substring(str.length() - 5);
			String numeroCompleto = parteNumerica + "." + parteDecimal;

			return new BigDecimal(numeroCompleto);
		}

		/**
		 * Converte de BigDecimal para String.
		 */
		public String toString(BigDecimal obj) {

			String valor = getApplicationDecimalFormat().format(obj);
			return valor.replace(",", "");
		}
	}
	
	/**
	 * Decorator de Data.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class ValorDuasCasasDecorator implements FieldDecorator<BigDecimal> {

		/**
		 * Converte de String para BigDecimal.
		 */
		public BigDecimal fromString(String str) throws FieldDecoratorException {
			String parteNumerica = str.substring(0, str.length() - 3);
			String parteDecimal = str.substring(str.length() - 2);
			String numeroCompleto = parteNumerica + "." + parteDecimal;

			return new BigDecimal(numeroCompleto);
		}

		/**
		 * Converte de BigDecimal para String.
		 */
		public String toString(BigDecimal obj) {

			String valor = getApplicationDecimalFormat().format(obj);
			return valor.replace(",", "");
		}
	}

	/**
	 * Pega a formatação no formato Brasileiro.
	 */
	private static DecimalFormat getApplicationDecimalFormat() {

		Locale brasil = new Locale("pt", "BR");
		DecimalFormat numberFormat = new DecimalFormat("#######0.00000", new DecimalFormatSymbols(brasil));
		numberFormat.setParseBigDecimal(true);
		numberFormat.setDecimalSeparatorAlwaysShown(true);
		numberFormat.setMinimumFractionDigits(5);
		return numberFormat;
	}

	/**
	 * Classe para conversao de long.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class LongDecorator extends DefaultFieldDecorator {

		@Override
		public Object fromString(String str) {

			return str == null || "".equals(str) ? null : Long.valueOf(str);
		}
	}

	/**
	 * Classe para conversao de long.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class IntegerDecorator extends DefaultFieldDecorator {

		@Override
		public Object fromString(String str) {
			return str == null || "".equals(str) ? null : Integer.valueOf(str.trim());
		}
	}
}
