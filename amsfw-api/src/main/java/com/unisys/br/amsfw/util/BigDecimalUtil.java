package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe utilitaria para bigDecimal.
 * 
 * @author ReisOtaL
 * 
 */
public class BigDecimalUtil {

	private BigDecimalUtil() {

	}

	/**
	 * Metodo para dividir dois BigDecimal.
	 * 
	 * @param dividendo
	 * @param divisor
	 * @return
	 */
	public static BigDecimal dividir(BigDecimal dividendo, BigDecimal divisor) {
		if (divisor == null || divisor.equals(BigDecimal.ZERO)) {
			throw new ArithmeticException("Impossível dividir por 0.");
		}
		if (dividendo == null) {
			return BigDecimal.ZERO;
		}
		return dividendo.divide(divisor, 5, RoundingMode.HALF_UP);
	}

	/**
	 * Metodo para multiplicar dois BigDecimal.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal multiplicar(BigDecimal a, BigDecimal b) {
		if (a == null || b == null) {
			return BigDecimal.ZERO;
		}
		return a.multiply(b).setScale(5, RoundingMode.HALF_UP);
	}

	/**
	 * Metodo para somar dois BigDecimal.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal somar(BigDecimal a, BigDecimal b) {
		if (a == null && b == null) {
			return BigDecimal.ZERO;
		}
		if (a == null) {
			return b;
		} else if (b == null) {
			return a;
		}

		return a.add(b).setScale(5, RoundingMode.HALF_UP);
	}

	/**
	 * Metodo para subtrair dois BigDecimal.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal subtrair(BigDecimal a, BigDecimal b) {
		if (a == null && b == null) {
			return BigDecimal.ZERO;
		}
		if (a == null) {
			return b.negate();
		} else if (b == null) {
			return a;
		}

		return a.subtract(b).setScale(5, RoundingMode.HALF_UP);
	}

}
