package com.unisys.br.amsfw.test.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author FlanmarP
 * 
 */
public class LoadProperties {
	private static Properties props = new Properties();

	private LoadProperties() {
	}

	/**
	 * 
	 * o usuario informa o nome do propertie para recuperar os dados.
	 * 
	 * @author FlanmarP
	 * @param properties
	 * @return
	 * @throws IOException
	 * 
	 */
	private static Properties getProp(String properties) {
		try {

			FileInputStream file = new FileInputStream("./src/test/resources/" + properties + ".properties");
			props.load(file);

		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return props;
	}

	/**
	 * metodo retorna a String do properties.
	 * 
	 * @author FlanmarP
	 * @param texto
	 * @param props
	 * @return
	 */
	public static String props(String props, String texto) {
		return getProp(props).getProperty(texto);
	}
}
