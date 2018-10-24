package com.unisys.br.amsfw.util;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * Método de teste de data.
 * 
 * @author DelfimSm
 * 
 */
public class DateUtilTest {

	@Test
	public void testGetMesExtenso() {
		assertTrue("Mês deve ser Janeiro", DateUtil.getMesExtenso(Calendar.JANUARY).equals("Janeiro"));

		assertTrue("Mês deve ser Dezembro", DateUtil.getMesExtenso(Calendar.DECEMBER).equals("Dezembro"));

		assertTrue("Mês deve ser Nulo", DateUtil.getMesExtenso(13) == null);
	}

	@Test
	public void testBuscaAnoMesAnterior() {
		assertTrue("Ano deve ser 2011", DateUtil.buscaAnoMesAnterior(2012, 0).get("ano").equals(2011));
		assertTrue("Mês deve ser 11", DateUtil.buscaAnoMesAnterior(2012, 0).get("mes").equals(11));

	}

	/**
	 * Realiza o teste para saber se é dia util.
	 * 
	 * @author RegoIanC
	 */
	@Test
	public void testDiaUtil() {
		try {
			Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-04");
			assertTrue("Deve ser dia útil", DateUtil.isDiaUtil(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza o teste para saber se o dia nao nao e util.
	 * 
	 * @author RegoIanC
	 */
	@Test
	public void testNaoDiaUtil() {
		try {
			Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-08");
			assertFalse("Não é dia útil", DateUtil.isDiaUtil(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza o teste para recuperar o proximo dia util.
	 * 
	 * @author RegoIanC
	 */
	@Test
	public void testProximoDiaUtil() {
		try {
			Date data = new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-08");
			data = DateUtil.recuperarProximoDiaUtil(data);
			assertTrue("Tem que ser 10/02/2013", DateUtil.formataData(data).equals("10/02/2014"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
