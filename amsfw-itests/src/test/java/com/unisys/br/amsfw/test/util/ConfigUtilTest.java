package com.unisys.br.amsfw.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.unisys.br.amsfw.test.util.ConfigUtil;

public class ConfigUtilTest {

	@Test
	public void deve_Carregar_Propriedades_De_Arquivo() {
		// Verifica se todas as propriedades do arquivo itests-config.properties estão setadas
		assertEquals("12345", ConfigUtil.getProperty("abc"));
		assertEquals("teste", ConfigUtil.getProperty("aaabbb"));
		assertEquals("nonnonnon", ConfigUtil.getProperty("bbb"));
		assertEquals("valor1Teste", ConfigUtil.getProperty("jpa.testePersistenceUnit_propriedade1.teste"));
		assertEquals("valor2Teste", ConfigUtil.getProperty("jpa.testePersistenceUnit_propriedade2.teste"));
	}
	
	@Test
	public void deve_Retornar_Propriedades_Por_Prefixo() {
		Collection<String> propertiesComecandoPorA = ConfigUtil.getPropertiesComecandoPor("a");
		
		Iterator<String> i = propertiesComecandoPorA.iterator();
		
		assertTrue("Nao localizou nenhuma propriedade", i.hasNext());
		assertEquals("aaabbb", i.next());
		assertEquals("abc", i.next());
	}
	
	@Test
	public void deve_Retornar_Propriedades_De_Um_Persistence_Unit_JPA() {
		Map<String, String> properties = ConfigUtil.getJPAProperties("testePersistenceUnit");
		
		assertTrue(properties.keySet().contains("propriedade1.teste"));
		assertTrue(properties.keySet().contains("propriedade2.teste"));
		
		assertEquals("valor1Teste", properties.get("propriedade1.teste"));
		assertEquals("valor2Teste", properties.get("propriedade2.teste"));
	}

}
