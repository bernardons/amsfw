package com.unisys.br.amsfw.web.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Teste do utilitário de CPF.
 * 
 * @author DelfimSM
 *
 */
public class CpfUtilTest {
	
	@Test
	public void testCpfCorreto() {

			boolean resultado = CpfUtil.validaCPF("29888235109");
			assertTrue("Deve validar o cpf.", resultado);
	}
	
	@Test
	public void testCpfInvalido() {

			boolean resultado = CpfUtil.validaCPF("29888235103");
			assertTrue("Deve dar exceção.", !resultado);
			
			resultado = CpfUtil.validaCPF("11111111111");
			assertTrue("Deve dar exceção.", !resultado);
	}

}
