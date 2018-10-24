/**
 * 
 */
package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Teste Unitário classe utilitária para conversão de valores quem Native Querys para objeto.

 * @author AroAL
 *
 */
public class NativeQueryConvertTest {

	private static final Object VALUE = 1;
	private static final Object VALUE_NULL = null;
	private static final Integer TO_INTEGER = Integer.valueOf("1");
	private static final String TO_STRING = "1";
	private static final Long TO_LONG = 1L;
	private static final BigDecimal TO_BIG_DECIMAL = BigDecimal.ONE;
	private static final Date TO_DATE = new Date();
	private static final Double TO_DOUBLE = new Double(1);

	@Test
	public void testToBigDecimal() {
		assertEquals(TO_BIG_DECIMAL, NativeQueryConvert.toBigDecimal(VALUE)); 
		assertEquals(TO_BIG_DECIMAL, NativeQueryConvert.toBigDecimal(TO_INTEGER)); 
		assertEquals(TO_BIG_DECIMAL, NativeQueryConvert.toBigDecimal(TO_LONG)); 
		assertNotNull(NativeQueryConvert.toBigDecimal(VALUE)); 
		assertNull(NativeQueryConvert.toBigDecimal(VALUE_NULL)); 
	}
	
	@Test
	public void testToDate() {
		assertEquals(TO_DATE, NativeQueryConvert.toDate(TO_DATE)); 
		assertNotNull(NativeQueryConvert.toDate(TO_DATE)); 
		assertNull(NativeQueryConvert.toDate(VALUE_NULL)); 
	}

	@Test
	public void testToDouble() {
		assertEquals(TO_DOUBLE, NativeQueryConvert.toDouble(VALUE)); 
		assertEquals(TO_DOUBLE, NativeQueryConvert.toDouble(TO_BIG_DECIMAL)); 
		assertNotNull(NativeQueryConvert.toDouble(VALUE)); 
		assertNull(NativeQueryConvert.toDouble(VALUE_NULL)); 
	}

	@Test
	public void testToInteger() {
		assertEquals(TO_INTEGER, NativeQueryConvert.toInteger(VALUE)); 
		assertNotNull(NativeQueryConvert.toInteger(VALUE)); 
		assertNull(NativeQueryConvert.toInteger(VALUE_NULL)); 
	}

	@Test
	public void testToLong() {
		assertEquals(TO_LONG, NativeQueryConvert.toLong(VALUE)); 
		assertNotNull(NativeQueryConvert.toLong(VALUE)); 
		assertNull(NativeQueryConvert.toLong(VALUE_NULL)); 
	}
	
	@Test
	public void testToString() {
		assertEquals(TO_STRING, NativeQueryConvert.toString(VALUE)); 
		assertNotNull(NativeQueryConvert.toString(VALUE)); 
		assertNull(NativeQueryConvert.toString(VALUE_NULL)); 
	}

}
