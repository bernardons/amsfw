package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.FieldDecorator;

/**
 * 
 * @author ReisOtaL
 * 
 */
public class FfPojoDecoratorUtil {

	private static final int PARTE_DECIMAL = 5;

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
				throw new FieldDecoratorException("ffpojo.parse.data|" + mesAno + "," + str, e);
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
				throw new FieldDecoratorException("ffpojo.parse.data|" + anoMesDia + "," + str, e);
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
				return str == null || str.trim().equals("") ? null : new SimpleDateFormat(diaMesAno).parse(str
						.replace(".", "").replace("-", "").replace("/", ""));
			} catch (ParseException e) {
				throw new FieldDecoratorException("ffpojo.parse.data|" + diaMesAno + "," + str, e);
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
				throw new FieldDecoratorException("ffpojo.parse.data|yyyyMMddHHmm," + str, e);
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
			if (str == null || str.isEmpty()) {
				return null;
			}

			try {
				String parteNumerica = str.substring(0, str.length() - PARTE_DECIMAL);
				String parteDecimal = str.substring(str.length() - PARTE_DECIMAL);
				String numeroCompleto = parteNumerica + "." + parteDecimal;

				return new BigDecimal(numeroCompleto);
			} catch (Exception e) {
				throw new FieldDecoratorException("ffpojo.parse.ValorCincoCasas|" + str, e);
			}
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

		private static final int TAMANHO_PARTE_DECIMAL = 2;

		/**
		 * Converte de String para BigDecimal.
		 */
		public BigDecimal fromString(String str) throws FieldDecoratorException {

			if (str == null || str.isEmpty()) {
				return null;
			}

			try {
				str = str.replaceAll("\\.", "").replaceAll("\\,", "");
				String parteNumerica = str.substring(0, str.length() - TAMANHO_PARTE_DECIMAL);
				String parteDecimal = str.substring(str.length() - TAMANHO_PARTE_DECIMAL);
				String numeroCompleto = parteNumerica + "." + parteDecimal;

				return new BigDecimal(numeroCompleto);
			} catch (Exception e) {
				throw new FieldDecoratorException("ffpojo.parse.ValorDuasCasas|" + str, e);
			}
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
		numberFormat.setMinimumFractionDigits(PARTE_DECIMAL);
		return numberFormat;
	}

	/**
	 * Classe para conversao de long.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class LongDecorator implements FieldDecorator<Long> {

		/**
		 * Converte para Long.
		 */
		public Long fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || "".equals(str) ? null : Long.valueOf(str);
			} catch (Exception e) {
				throw new FieldDecoratorException("ffpojo.parse.numerico|" + str, e);
			}
		}

		@Override
		public String toString(Long field) throws FieldDecoratorException {
			return field == null ? "" : field.toString();
		}
	}

	/**
	 * Classe para conversao de long.
	 * 
	 * @author delfimsm
	 * 
	 */
	public static class IntegerDecorator implements FieldDecorator<Integer> {

		@Override
		public Integer fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || "".equals(str) ? null : Integer.valueOf(str.trim());
			} catch (Exception e) {
				throw new FieldDecoratorException("ffpojo.parse.numerico|" + str, e);
			}
		}

		@Override
		public String toString(Integer field) throws FieldDecoratorException {
			return field == null ? "" : field.toString();
		}
	}

	/**
	 * Conversor para BigInteger.
	 * 
	 * @author RegoIanC
	 * 
	 */
	public static class BigIntegerDecorator implements FieldDecorator<BigInteger> {

		@Override
		public BigInteger fromString(String str) throws FieldDecoratorException {
			try {
				return str == null || "".equals(str) ? null : new BigInteger(str.trim());
			} catch (Exception e) {
				throw new FieldDecoratorException("ffpojo.parse.numerico|" + str, e);
			}
		}

		@Override
		public String toString(BigInteger field) throws FieldDecoratorException {
			return field == null ? "" : field.toString();
		}
	}
	
	/**
	 * Decorator de Data sem Lenient.
	 * 
	 * @author FernandF
	 * 
	 */
	public static class DateDecoratorLenientFalse implements FieldDecorator<Date> {
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(anoMesDia);
				sdf.setLenient(false);
				return str == null || str.trim().equals("") ? null : sdf.parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("ffpojo.parse.data|" + anoMesDia + "," + str, e);
			}
		}

		public String toString(Date obj) {
			Date date = (Date) obj;
			return new SimpleDateFormat(anoMesDia).format(date);
		}
	}

	/**
	 * Metodo criado para lançar uma mensagem de data do balancete inválida.
	 * 
	 * @author FernandF
	 * 
	 */
	public static class DateDecoratorDataBalancete implements FieldDecorator<Date> {
		public Date fromString(String str) throws FieldDecoratorException {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(anoMesDia);
				sdf.setLenient(false);
				return str == null || str.trim().equals("") ? null : sdf.parse(str);
			} catch (ParseException e) {
				throw new FieldDecoratorException("ffpojo.parse.data.balancete", e);
			}
		}

		public String toString(Date obj) {
			Date date = (Date) obj;
			return new SimpleDateFormat(anoMesDia).format(date);
		}
	}
}
