package com.unisys.br.amsfw.web.validator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * Teste do formatador.
 * 
 * @author DelfimSM
 *
 */
public class FormatadorUtilTest {

	private static final String CPF_COM_MASCARA = "298.882.351-09";
	private static final String CPF_SEM_MASCARA = "29888235109";
	private static final String CPF_MASK = "999.999.999-99";

	@Test
	public void testAplicarMascara() {

		String cpf = FormatadorUtil.aplicarMascara(CPF_SEM_MASCARA, CPF_MASK);
		assertEquals(cpf, CPF_COM_MASCARA);
	}
	
	@Test
	public void testRetirarMascara() {

		String cpf = FormatadorUtil.retirarMascara(CPF_COM_MASCARA);
		assertEquals(cpf, CPF_SEM_MASCARA);
	}

	@Test
	public void testFormatarMoeda() {

		String valor = FormatadorUtil.formatarMoeda(new BigDecimal(12));
		assertEquals(valor, "12,00");
	}

	
}
