package com.unisys.br.amsfw.web.converter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Testa a data no formato mes e ano.
 * 
 * @author lelesraj
 * 
 */
public class DataMesAnoConverterTest {

	private static final String DATA_FORMATADA = "02/2013";
	private static final String DATA_FORMATADA_ERRADA = "02/203";

	@Test
	public void testConverter() {

		DataMesAnoConverter converter = new DataMesAnoConverter();
		assertTrue("Datas devem ser iguais.", converter.getAsString(null, null, DATA_FORMATADA).equals(DATA_FORMATADA));

		try {
			converter.getAsString(null, null, DATA_FORMATADA_ERRADA);
			fail("Data passada por parametro é inválida, deveria ter ocorrido o erro.");
		} catch (Exception e) {
			assertTrue("Exceção de data lançada corretamente!", true);

		}

	}

}
