package com.unisys.br.amsfw.web.validator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.faces.validator.ValidatorException;

import org.junit.Test;

/**
 * Teste do validator de email.
 * 
 * @author DelfimSM
 *
 */
public class EmailValidatorTest {

	@Test
	public void testEmailCorreto() {

		try {
			new EmailValidator().validate(null, null, "samuel.delfim@br.unisys.com");
			assertTrue("Deve validar o email.", true);
		} catch (Exception e) {
			fail("Não deveria lancar exceção.");
		}
	}
	
	@Test
	public void testEmailInvalido() {

		try {
			new EmailValidator().validate(null, null, "samuel.delfimbr.unisys.com");
			fail("Não deveria passar sem lançar exceção.");
		} catch (ValidatorException e) {
			assertTrue("Deve dar exceção.", true);
		}
		
		try {
			new EmailValidator().validate(null, null, "samuel.delfim@");
			fail("Não deveria passar sem lançar exceção.");
		} catch (ValidatorException e) {
			assertTrue("Deve dar exceção.", true);
		}
	}

}
