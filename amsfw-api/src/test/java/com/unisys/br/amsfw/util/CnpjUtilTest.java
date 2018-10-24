package com.unisys.br.amsfw.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CnpjUtilTest {

	@Test
	public void testCnpjCorreto() {

		assertEquals(true, CnpjUtil.validarCNPJ("60.919.221/0001-90"));
		assertEquals(false, CnpjUtil.validarCNPJ("60.919.221/0001-91"));
	}

}
