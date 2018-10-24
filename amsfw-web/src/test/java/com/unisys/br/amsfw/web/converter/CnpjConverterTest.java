package com.unisys.br.amsfw.web.converter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Teste do converter de cnpj.
 * 
 * @author DelfimSM
 *
 */
public class CnpjConverterTest {

	private static final String CNPJ_SEM_FORMATACAO = "45433645000156";
	private static final String CNPJ_FORMATADO = "45.433.645/0001-56";

	@Test
	public void testConverter() {

		String cnpjSemFormatacao = (String) new CnpjConverter().getAsObject(null, null, CNPJ_FORMATADO);
		assertEquals(cnpjSemFormatacao, CNPJ_SEM_FORMATACAO);

		String cnpjFormatado = new CnpjConverter().getAsString(null, null, CNPJ_SEM_FORMATACAO);
		assertEquals(cnpjFormatado, CNPJ_FORMATADO);
	}

}
