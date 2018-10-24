package com.unisys.br.amsfw.web.validator;

import static org.junit.Assert.*;

import javax.faces.validator.ValidatorException;

import org.junit.Test;

/**
 * Teste do validator de cpf.
 * 
 * @author DelfimSM
 *
 */
public class CpfValidatorTest {

	@Test
	public void testCpfCorreto() {

		try {
			new CpfValidator().validate(null, null, "298.882.351-09");
			assertTrue("Deve validar o cpf.", true);
		} catch (ValidatorException e) {
			fail("Não deveria lancar exceção.");
		}
	}
	
	@Test
	public void testCpfInvalido() {

		try {
			new CpfValidator().validate(null, null, "298.882.351-03");
			fail("Não deveria passar sem lançar exceção.");
		} catch (NullPointerException e) {
			assertTrue("Deve dar exceção.", true);
		}
	}
}
