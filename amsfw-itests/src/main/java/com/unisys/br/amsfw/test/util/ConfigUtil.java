package com.unisys.br.amsfw.test.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Classe que carrega as configurações definidas no arquivo itests-config.properties.
 * 
 * @author DelfimSM
 */
public class ConfigUtil {

	public static final String PREFIX_JPA_PROPERTIES = "jpa.";
	public static final String ITESTS_CONFIG_PROPERTIES = "itests-config.properties";
	public static final String SISTEMA_PROPERTIES = "sistema.properties";
	private static final Logger LOGGER = Logger.getLogger(ConfigUtil.class);

	private ConfigUtil() {
	}

	private static Properties properties = new Properties();

	private static Properties propertiesSistema = new Properties();

	/**
	 * Carrega o arquivo itests-config.properties.
	 */
	static {
		try {
			InputStream resource = ConfigUtil.class.getClassLoader().getResourceAsStream(ITESTS_CONFIG_PROPERTIES);
			if (resource != null) {
				properties.load(resource);
			}

		} catch (Exception e) {
			LOGGER.error("Falha ao ler o arquivo itests-config.properties.", e);
		}
	}

	/**
	 * Carrega o arquivo sistema.properties.
	 */
	static {
		try {
			InputStream resource = ConfigUtil.class.getClassLoader().getResourceAsStream(SISTEMA_PROPERTIES);
			if (resource != null) {
				propertiesSistema.load(resource);
			}

		} catch (Exception e) {
			LOGGER.error("Falha ao ler o arquivo sistema.properties.", e);
		}
	}

	/**
	 * Retorna a propriedade.
	 * 
	 * @param chave
	 * @return
	 */
	public static String getProperty(String chave) {
		return properties.getProperty(chave);
	}

	/**
	 * Retorna as chaves das propriedades que iniciam por uma dada string.
	 * 
	 * @param chaveComecandoPor
	 * @return
	 */
	protected static Collection<String> getPropertiesComecandoPor(String chaveComecandoPor) {
		Collection<String> result = new ArrayList<String>();
		Enumeration<Object> enumeracao = properties.keys();

		while (enumeracao.hasMoreElements()) {
			String chaveAtual = (String) enumeracao.nextElement();
			if (chaveAtual.startsWith(chaveComecandoPor)) {
				result.add(chaveAtual);
			}
		}

		return result;
	}

	/**
	 * Retorna as JPA properties de acordo com a persistence unit passada como parâmetro.
	 * 
	 * @param persistenceUnit
	 * @return
	 */
	public static Map<String, String> getJPAProperties(String persistenceUnit) {
		HashMap<String, String> valores = new HashMap<String, String>();

		for (String nomePropriedade : getPropertiesComecandoPor(PREFIX_JPA_PROPERTIES + persistenceUnit)) {
			valores.put(nomePropriedade.substring(nomePropriedade.indexOf("_") + 1), properties.getProperty(nomePropriedade));
		}

		return valores;
	}

	/**
	 * Properties geral do sistema. <br>
	 * <b>Exemplo:</b> <br>
	 * <i>Mensagem de campo obrigatório</i> <br>
	 * <i>Mensagem de erro</i> <br>
	 * <i>Mensagem de sucesso</i>
	 * 
	 * @author RegoIanC
	 */
	public static String getPropertiesSistema(String key) {
		return (String) propertiesSistema.getProperty(key);
	}

}
