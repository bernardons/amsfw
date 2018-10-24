package com.unisys.br.amsfw.util;

import static org.junit.Assert.*;

import java.text.ParseException;
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

	@Test
	public void testSabadoOuDomingo() throws ParseException {
		Date dataSabado = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-05");
		assertTrue(DateUtil.isSabadoOuDomingo(dataSabado));

		Date dataDomingo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-06");
		assertTrue(DateUtil.isSabadoOuDomingo(dataDomingo));

		Date dataSegunda = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-07");
		assertFalse(DateUtil.isSabadoOuDomingo(dataSegunda));

	}

	@Test
	public void testDateDiffDaysUteis() throws ParseException {
		Date inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-07");
		Date fim = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-14");
		assertTrue(DateUtil.dateDiffDaysUteis(inicio, fim) == 6);

		inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-07");
		fim = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-11");
		assertTrue(DateUtil.dateDiffDaysUteis(inicio, fim) == 5);

		inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-07");
		fim = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-07");
		assertTrue(DateUtil.dateDiffDaysUteis(inicio, fim) == 1);

	}

	@Test
	public void testAdicionarDiasDesprezandoSabadosDomingos() throws ParseException {
		Date inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-18");
		Date fim = DateUtil.adicionarDiasDesprezandoSabadosDomingos(inicio, 60);
		assertTrue(!DateUtil.isSabadoOuDomingo(fim));
		assertTrue(DateUtil.dateDiffDays(fim, new SimpleDateFormat("yyyy-MM-dd").parse("2016-06-10")) == 0);

		inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-11");
		fim = DateUtil.adicionarDiasDesprezandoSabadosDomingos(inicio, 3);
		assertTrue(!DateUtil.isSabadoOuDomingo(fim));
		assertTrue(DateUtil.dateDiffDays(fim, new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-16")) == 0);

		inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-14");
		fim = DateUtil.adicionarDiasDesprezandoSabadosDomingos(inicio, 2);
		assertTrue(!DateUtil.isSabadoOuDomingo(fim));
		assertTrue(DateUtil.dateDiffDays(fim, new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-16")) == 0);

		inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-14");
		fim = DateUtil.adicionarDiasDesprezandoSabadosDomingos(inicio, 1);
		assertTrue(!DateUtil.isSabadoOuDomingo(fim));
		assertTrue(DateUtil.dateDiffDays(fim, new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-15")) == 0);

		inicio = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-21");
		fim = DateUtil.adicionarDiasDesprezandoSabadosDomingos(inicio, 123);
		assertTrue(!DateUtil.isSabadoOuDomingo(fim));
		assertTrue(DateUtil.dateDiffDays(fim, new SimpleDateFormat("yyyy-MM-dd").parse("2016-09-08")) == 0);
	}

}
