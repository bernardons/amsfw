package com.unisys.br.amsfw.web.validator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Teste do validador de telefone.
 * 
 * @author DelfimSM
 *
 */
public class TelefoneValidatorTest {

	@Test
	public void testTelefoneCorreto() {

		try {
			new TelefoneValidator().validate(null, null, "(31)8243-2341");
			new TelefoneValidator().validate(null, null, "(31)82432-2341");
			assertTrue("Deve validar o telefone.", true);
		} catch (Exception e) {
			fail("Não deveria lancar exceção.");
		}
	}

	@Test
	public void testTelefoneInvalido() {

		try {
			new TelefoneValidator().validate(null, null, "(31)82432-12341");
			fail("Não deveria passar sem lançar exceção.");
		} catch (NullPointerException e) {
			assertTrue("Deve dar exceção.", true);
		}

		try {
			new TelefoneValidator().validate(null, null, "asdas");
			fail("Não deveria passar sem lançar exceção.");
		} catch (NullPointerException e) {
			assertTrue("Deve dar exceção.", true);
		}
	}

}
