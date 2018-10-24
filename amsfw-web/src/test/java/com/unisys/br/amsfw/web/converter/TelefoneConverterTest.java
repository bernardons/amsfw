package com.unisys.br.amsfw.web.converter;

import static org.junit.Assert.*;

import org.junit.Test;

public class TelefoneConverterTest {

	@Test
	public void testGetAsObject() {
		TelefoneConverter converter = new TelefoneConverter();
		assertEquals(converter.getAsObject(null, null, null), "");
		assertEquals(converter.getAsObject(null, null, ""), "");
		assertEquals(converter.getAsObject(null, null, "   "), "");
		assertEquals(converter.getAsObject(null, null, "(31)3333-3333"), "3133333333");
		
		
		
	}

	@Test
	public void testGetAsString() {
		TelefoneConverter converter = new TelefoneConverter();
		assertEquals(converter.getAsString(null, null, null), "");
		assertEquals(converter.getAsString(null, null, ""), "");
		assertEquals(converter.getAsString(null, null, "       "), "");
		assertEquals(converter.getAsString(null, null,0), "");
		assertEquals(converter.getAsString(null, null,44444444), "44444444");
		
		
		
	}

}
