package com.unisys.br.amsfw.web.converter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Teste de CPF.
 * 
 * @author DelfimSM
 *
 */
public class CpfConverterTest {

	private static final String CPF_SEM_FORMATACAO = "51322145334";
	private static final String CPF_FORMATADO = "513.221.453-34";

	@Test
	public void testConverter() {

		String cpfSemFormatacao = (String) new CpfConverter().getAsObject(null, null, CPF_FORMATADO);
		assertEquals(cpfSemFormatacao, CPF_SEM_FORMATACAO);

		String cpfFormatado = new CpfConverter().getAsString(null, null, CPF_SEM_FORMATACAO);
		assertEquals(cpfFormatado, CPF_FORMATADO);
	}

}
