package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

	/**
	 * Arrendonda o valor passado por parametro para duas casas decimais.
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal formatarDuasCasas(BigDecimal value) {
		DecimalFormat df = new DecimalFormat("0.00");
		df.setMinimumFractionDigits(2);
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		df.setRoundingMode(RoundingMode.DOWN);
		String saida = df.format(value);
		return new BigDecimal(saida);
	}

	/**
	 * Retornar o valor passado por parametro com simbolo da moeda nacional.
	 * Ex.: Se passar 10.00 será retornado R$ 10,00
	 * 
	 * @param value
	 * @return
	 */
	public static String valorComSimboloMoeda(BigDecimal value) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("pt", "BR"));
		DecimalFormat df = new DecimalFormat("'R$' #,###,##0.00", dfs);
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(value);
	}

	/**
	 * Retornar o valor passado por parametro sem simbolo da moeda nacional.
	 * Ex.: Se passar 10.00 será retornado 10,00
	 * 
	 * @param value
	 * @return
	 */
	public static String valorSemSimboloMoeda(BigDecimal value) {

		String valorComSimboloMoeda = valorComSimboloMoeda(value);

		return valorComSimboloMoeda.substring(3);
	}

	/**
	 * Retorna o valor passado escrito por extenso em Reais. Ex.: Se for passado
	 * 1 000 110.00 retornará
	 */
	public static String valorPorExtensoEmReais(BigDecimal value) {
		return ValorExtensoUtil.escrever(value);
	}
}
