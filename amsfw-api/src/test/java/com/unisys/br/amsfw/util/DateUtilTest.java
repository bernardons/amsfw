package com.unisys.br.amsfw.util;

import static org.junit.Assert.*;

import java.util.Calendar;

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
	public void testBuscaAnoMesAnterior(){
		assertTrue("Ano deve ser 2011", DateUtil.buscaAnoMesAnterior(2012, 0).get("ano").equals(2011));
		assertTrue("Mês deve ser 11", DateUtil.buscaAnoMesAnterior(2012, 0).get("mes").equals(11));

	}

}
