package com.unisys.br.amsfw.web.converter;

import java.util.Calendar;
import java.util.Date;


import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;


/**
 * Teste
 * 
 * @author DelfimSM
 * 
 */
public class DataConverterTest {

	private static final String FORMATO_DATA = "dd/MM/yyyy";
	private static final String DATA_FORMATADA = "01/02/2013";

	@Test
	public void testConverter() {

		Calendar calendarFixo = Calendar.getInstance();
		calendarFixo.set(Calendar.DAY_OF_MONTH, 1);
		calendarFixo.set(Calendar.MONTH, Calendar.FEBRUARY);
		calendarFixo.set(Calendar.YEAR, 2013);
		
		DataConverter converter = mock(DataConverter.class);
		when(converter.getFormatoData()).thenReturn(FORMATO_DATA);    // Mock implementation
		when(converter.getAsObject(null, null, DATA_FORMATADA)).thenCallRealMethod();  // Real implementation
		
		assertTrue("Deveria retornar dd/MM/yyyy", converter.getFormatoData().equals(FORMATO_DATA));
		
		Date data = (Date) converter.getAsObject(null, null, DATA_FORMATADA);
		Calendar dataConvertida = Calendar.getInstance();
		dataConvertida.setTime(data);

		assertTrue(
				"Datas devem ser iguais.",
				dataConvertida.get(Calendar.DAY_OF_MONTH) == calendarFixo.get(Calendar.DAY_OF_MONTH));
		assertTrue(
				"Datas devem ser iguais.",
				dataConvertida.get(Calendar.MONTH) == calendarFixo.get(Calendar.MONTH));
		assertTrue(
				"Datas devem ser iguais.",
				dataConvertida.get(Calendar.YEAR) == calendarFixo.get(Calendar.YEAR));
		
	}

}
