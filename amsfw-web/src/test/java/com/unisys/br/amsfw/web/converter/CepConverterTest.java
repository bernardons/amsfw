package com.unisys.br.amsfw.web.converter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Teste do converter do cep.
 * 
 * @author DelfimSM
 *
 */
public class CepConverterTest {

	private static final Integer CEP_INTEIRO = 35160243;
	private static final String CEP_STRING = "35160-243";

	@Test
	public void testConverter() {

		Integer cepInteiro = (Integer) new CepConverter().getAsObject(null, null, CEP_STRING);
		assertEquals(cepInteiro, CEP_INTEIRO);

		String cepString = new CepConverter().getAsString(null, null, CEP_INTEIRO);
		assertEquals(cepString, CEP_STRING);
	}

}
