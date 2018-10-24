package com.unisys.br.amsfw.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilTest {

	@Test
	public void converterMoedaDoisDigitosDoubleTest() {

		assertTrue(
				"Erro ao converter valor {1850} deveria ter retornado {1850.0} mais retornou "
						+ Double.valueOf(Util.converterMoedaDoisDigitosDouble("1850")),
				Double.valueOf(Util.converterMoedaDoisDigitosDouble("1850")).toString().equals("1850.0"));

		assertTrue(
				"Erro ao converter valor {1850.333} deveria ter retornado {1850.33} mais retornou "
						+ Double.valueOf(Util.converterMoedaDoisDigitosDouble("1850.333")),
				Double.valueOf(Util.converterMoedaDoisDigitosDouble("1850.333")).toString().equals("1850.33"));

		assertTrue(
				"Erro ao converter valor {1850,7740} deveria ter retornado {1850.77} mais retornou "
						+ Double.valueOf(Util.converterMoedaDoisDigitosDouble("1850,7740")),
				Double.valueOf(Util.converterMoedaDoisDigitosDouble("1850,7740")).toString().equals("1850.77"));

	}

}
