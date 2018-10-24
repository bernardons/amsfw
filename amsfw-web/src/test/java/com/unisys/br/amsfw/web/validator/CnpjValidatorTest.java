package com.unisys.br.amsfw.web.validator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.faces.validator.ValidatorException;

import org.junit.Test;

/**
 * Teste do validator de cnpj.
 * 
 * @author DelfimSM
 * 
 */
public class CnpjValidatorTest {

	@Test
	public void testCnpjCorreto() {

		try {
			CnpjValidator validator = new CnpjValidator();
			validator.validate(null, null, "60.919.221/0001-90");
			assertTrue("Deve validar o cnpj.", true);
		} catch (ValidatorException e) {
			fail("Não deveria lancar exceção.");
		}
	}

	@Test
	public void testCnpjInvalido() {

		try {
			CnpjValidator validator = new CnpjValidator();
			validator.validate(null, null, "60.919.221/0001-91");
			fail("Não deveria passar sem lançar exceção.");
		} catch (NullPointerException e) {
			assertTrue("Deve dar exceção.", true);
		}
	}

}
