package com.unisys.br.amsfw.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.DateDecorator;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.DateDecoratorDiaMesAno;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.DateMesAnoDecorator;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.DateTimeDecorator;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.IntegerDecorator;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.LongDecorator;
import com.unisys.br.amsfw.util.FfPojoDecoratorUtil.ValorCincoCasasDecorator;

/**
 * Executa os testes de parser de ocorrência.
 * 
 * @author delfimsm
 * 
 */
public class FfPojoDecoratorUtilTest {

	private static final String VALOR_FORMATADO_5_CASAS = "1234300000";

	@Test
	public void testeValueCincoCasasDecorator() {

		ValorCincoCasasDecorator valueCincoCasasDecorator = new ValorCincoCasasDecorator();
		BigDecimal valor = new BigDecimal(12343.00);
		String valorFormatadoCincoCasas = valueCincoCasasDecorator.toString(valor);
		Assert.assertTrue(
				"Valor formatado com 5 casas deve ser 1234300000.",
				valorFormatadoCincoCasas.equals(VALOR_FORMATADO_5_CASAS));

		try {
			Assert.assertTrue(
					"O valor deve retornar o big decimal correto.",
					valueCincoCasasDecorator.fromString(VALOR_FORMATADO_5_CASAS).compareTo(valor) == 0);
		} catch (FieldDecoratorException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseData() {

		DateDecorator dateDecorator = new DateDecorator();
		Date data = null;
		try {
			data = dateDecorator.fromString("20121201");
		} catch (FieldDecoratorException e) {
			Assert.assertTrue("Falha ao efetuar parse da data.", false);
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		Assert.assertTrue("Ano deve ser 2012", calendar.get(Calendar.YEAR) == 2012);
		Assert.assertTrue("Mês deve ser Dezembro ou 11 na classe Calendar", calendar.get(Calendar.MONTH) == 11);
		Assert.assertTrue("Dia deve ser 1", calendar.get(Calendar.DAY_OF_MONTH) == 1);

	}

	@Test
	public void testParseDataHora() {

		DateTimeDecorator dateDecorator = new DateTimeDecorator();
		Date data = null;
		try {
			data = dateDecorator.fromString("201212012210");
		} catch (FieldDecoratorException e) {
			Assert.assertTrue("Falha ao efetuar parse da data.", false);
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		Assert.assertTrue("Ano deve ser 2012", calendar.get(Calendar.YEAR) == 2012);
		Assert.assertTrue("Mês deve ser Dezembro ou 11 na classe Calendar", calendar.get(Calendar.MONTH) == 11);
		Assert.assertTrue("Dia deve ser 1", calendar.get(Calendar.DAY_OF_MONTH) == 1);
		Assert.assertTrue("Hora deve ser 22", calendar.get(Calendar.HOUR_OF_DAY) == 22);
		Assert.assertTrue("Minuto deve ser 10", calendar.get(Calendar.MINUTE) == 10);
	}

	@Test
	public void testParseDataMesAno() {

		DateMesAnoDecorator dateDecorator = new DateMesAnoDecorator();
		Date data = null;
		try {
			data = dateDecorator.fromString("201212");
		} catch (FieldDecoratorException e) {
			Assert.assertTrue("Falha ao efetuar parse da data.", false);
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		Assert.assertTrue("Ano deve ser 2012", calendar.get(Calendar.YEAR) == 2012);
		Assert.assertTrue("Mês deve ser Dezembro ou 11 na classe Calendar", calendar.get(Calendar.MONTH) == 11);
	}

	@Test
	public void testParseDataDiaMesAno() {

		DateDecoratorDiaMesAno dateDecorator = new DateDecoratorDiaMesAno();
		Date data = null;
		try {
			data = dateDecorator.fromString("01122012");
		} catch (FieldDecoratorException e) {
			Assert.assertTrue("Falha ao efetuar parse da data.", false);
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		Assert.assertTrue("Ano deve ser 2012", calendar.get(Calendar.YEAR) == 2012);
		Assert.assertTrue("Mês deve ser Dezembro ou 11 na classe Calendar", calendar.get(Calendar.MONTH) == 11);
		Assert.assertTrue("Dia deve ser 1", calendar.get(Calendar.DAY_OF_MONTH) == 1);
	}

	@Test
	public void testParseCamposNumericos() {
		String integer = "123";
		String campoLong = "234235";
		LongDecorator longDecorator = new LongDecorator();
		IntegerDecorator integerDecorator = new IntegerDecorator();
		try {
			Assert.assertTrue( longDecorator.fromString(campoLong) == 234235);
			Assert.assertTrue( integerDecorator.fromString(integer) == 123);
		} catch (FieldDecoratorException e) {
			e.printStackTrace();
		}

	}

}
