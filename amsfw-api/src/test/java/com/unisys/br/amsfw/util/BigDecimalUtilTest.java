package com.unisys.br.amsfw.util;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Classe de testes para BigDecimal.
 * 
 * @author ReisOtaL
 * 
 */
public class BigDecimalUtilTest {

	@Test
	public void testDividir() {

		try {
			BigDecimal dividendo = new BigDecimal(99999.995);
			BigDecimal divisor = new BigDecimal(13.5);

			Assert.assertTrue("O resultado deveria ser 7407.40704!", BigDecimalUtil.dividir(dividendo, divisor)
					.toString().equals("7407.40704"));

			dividendo = new BigDecimal(0);
			Assert.assertTrue("O resultado deveria ser ZERO com precisao de 5 casas!",
					BigDecimalUtil.dividir(dividendo, divisor).equals(new BigDecimal(BigInteger.ZERO, 5)));

		} catch (ArithmeticException e) {
			fail("Rever implementacao do Metodo dividir.");
		}
	}

	@Test(expected = ArithmeticException.class)
	public void testDividirPorZero() {
		BigDecimal dividendo = new BigDecimal(99999.995);
		BigDecimal divisor = new BigDecimal(0);
		BigDecimalUtil.dividir(dividendo, divisor);
	}

	@Test
	public void testMultiplicar() {

		BigDecimal a = new BigDecimal(99999.995);
		BigDecimal b = new BigDecimal(13.5);
		Assert.assertTrue("O resultado deveria ser 1349999.93250!",
				BigDecimalUtil.multiplicar(a, b)
						.equals(new BigDecimal(1349999.93250).setScale(5, RoundingMode.HALF_UP)));

	}

	@Test
	public void testSomar() {

		BigDecimal a = new BigDecimal(99999.995);
		BigDecimal b = new BigDecimal(13.5);
		Assert.assertTrue("O resultado deveria ser 100013.49500!",
				BigDecimalUtil.somar(a, b).equals(new BigDecimal(100013.49500).setScale(5, RoundingMode.HALF_UP)));

		a = null;
		Assert.assertTrue("O resultado deveria ser 13.5!", BigDecimalUtil.somar(a, b).equals(new BigDecimal(13.5)));

		a = new BigDecimal(99999.995);
		b = null;
		Assert.assertTrue("O resultado deveria ser 99999.995!",
				BigDecimalUtil.somar(a, b).equals(new BigDecimal(99999.995)));

		a = null;
		b = null;
		Assert.assertTrue("O resultado deveria ser ZERO!", BigDecimalUtil.somar(a, b).equals(BigDecimal.ZERO));

	}
	
	

	@Test
	public void testSubtrair() {

		BigDecimal a = new BigDecimal(99999.995);
		BigDecimal b = new BigDecimal(13.5);
		Assert.assertTrue("O resultado deveria ser 99986.49500!",
				BigDecimalUtil.subtrair(a, b).equals(new BigDecimal(99986.49500).setScale(5, RoundingMode.HALF_UP)));

		a = null;
		Assert.assertTrue("O resultado deveria ser -13.5!",
				BigDecimalUtil.subtrair(a, b).equals(new BigDecimal(13.5).negate()));

		a = new BigDecimal(99999.995);
		b = null;
		Assert.assertTrue("O resultado deveria ser 99999.995!",
				BigDecimalUtil.subtrair(a, b).equals(new BigDecimal(99999.995)));

	}

	@Test
	public void testValorComSimboloMoeda() {
		BigDecimal value = new BigDecimal(10);
		String valorFormatado = BigDecimalUtil.valorComSimboloMoeda(value);
		Assert.assertTrue("R$ 10,00".equals(valorFormatado));

		BigDecimal value2 = new BigDecimal(10.5985);
		String valorFormatado2 = BigDecimalUtil.valorComSimboloMoeda(value2);
		Assert.assertTrue("R$ 10,59".equals(valorFormatado2));

	}

}
