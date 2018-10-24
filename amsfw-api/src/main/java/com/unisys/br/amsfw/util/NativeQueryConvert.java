/**
 * 
 */
package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe utilitária para conversão de valores quem Native Querys para objeto.

 * @author AroAL
 *
 */
public class NativeQueryConvert {

	private NativeQueryConvert() {
	}

	/**
	 * Converter um valor recuperado por uma Native Query para BigDecimal.
	 * 
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object value) {
		if (value != null) {
			if (value instanceof Long || value instanceof Integer) {
				return BigDecimal.valueOf(toLong(value));
			} else {
				return (BigDecimal) value;
			}
		} else {
			return null;
		}
	}

	/**
	 * Converter um valor recuperado por uma Native Query para Date.
	 * 
	 * @return
	 */
	public static Date toDate(Object value) {
		if (value != null) {
			return (Date) value;
		} else {
			return null;
		}
	}

	/**
	 * Converter um valor recuperado por uma Native Query para Double.
	 * 
	 * @return
	 */
	public static Double toDouble(Object value) {
		if (value != null) {
			return Double.parseDouble(value.toString());
		} else {
			return null;
		}
	}
			
	/**
	 * Converter um valor recuperado por uma Native Query para Integer.
	 * 
	 * @return
	 */
	public static Integer toInteger(Object value) {
		if (value != null) {
			return Integer.valueOf(value.toString());
		} else {
			return null;
		}
	}

	/**
	 * Converter um valor recuperado por uma Native Query para Long.
	 * 
	 * @return
	 */
	public static Long toLong(Object value) {
		if (value != null) {
			return Long.valueOf(value.toString());
		} else {
			return null;
		}
	}

	/**
	 * Converter um valor recuperado por uma Native Query para String.
	 * 
	 * @return
	 */
	public static String toString(Object value) {
		if (value != null) {
			return value.toString();
		} else {
			return null;
		}
	}

}
