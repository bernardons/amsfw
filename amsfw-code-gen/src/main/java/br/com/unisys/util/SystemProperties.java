package br.com.unisys.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Arquivo de propriedades do sistema.
 * 
 * @author DelfimSM
 * 
 */
public class SystemProperties {

	private static final String PASTA_MAVEN_JAVA = "src/main/java/";
	private static final String SEPARADOR = "/";
	public static final String PASTA_BASE = "pasta.projeto.base";
	public static final String NOME_PROJETO = "nome.projeto";
	public static final String PACOTE_BASE = "pacote.base";
	public static final String PACOTE_DOMINIO = "pacote.dominio";
	public static final String CONEXAO_BANCO = "conexao.banco";
	public static final String USUARIO_BANCO = "usuario.banco"; 
	public static final String SENHA_BANCO = "senha.banco";

	private static Properties propriedades;

	private SystemProperties() {
	}

	/**
	 * Busca as propriedades a partir do arquivo.
	 * 
	 * @return
	 */
	private static Properties getPropriedades() {
		if (propriedades == null) {
			propriedades = new Properties();
			try {
				propriedades.load(SystemProperties.class.getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return propriedades;
	}

	/**
	 * Recupera a propriedade passada como parâmetro.
	 * 
	 * @param nomePropriedade
	 * @return
	 */
	public static String getPropriedade(String nomePropriedade) {
		return getPropriedades().getProperty(nomePropriedade);
	}

	public static String getDiretorioRaiz() {
		return getPropriedades().getProperty(PASTA_BASE);
	}

	public static String getDiretorioProjetoWeb() {
		return getPropriedades().getProperty(PASTA_BASE) + SEPARADOR + getPropriedades().getProperty(NOME_PROJETO)
				+ "-web/";
	}

	public static String getDiretorioPaginasWeb() {
		return getDiretorioProjetoWeb() + "src/main/webapp/pages";
	}

	public static String getDiretorioController() {
		return getDiretorioProjetoWeb() + PASTA_MAVEN_JAVA + getPropriedades().getProperty(PACOTE_BASE)
				+ getPropriedades().getProperty(NOME_PROJETO) + "/web/controller";
	}

	public static String getDiretorioProjetoApi() {
		return getPropriedades().getProperty(PASTA_BASE) + SEPARADOR + getPropriedades().getProperty(NOME_PROJETO)
				+ "-api/";
	}

	public static String getDiretorioEntidades() {
		return getDiretorioProjetoApi() + PASTA_MAVEN_JAVA + getPropriedades().getProperty(PACOTE_BASE)
				+ getPropriedades().getProperty(NOME_PROJETO) + "/domain/";
	}

	public static String getDiretorioInterfacesDao() {
		return getDiretorioProjetoApi() + PASTA_MAVEN_JAVA + getPropriedades().getProperty(PACOTE_BASE)
				+ getPropriedades().getProperty(NOME_PROJETO) + "/dao";
	}

	public static String getDiretorioInterfacesServices() {
		return getDiretorioProjetoApi() + PASTA_MAVEN_JAVA + getPropriedades().getProperty(PACOTE_BASE)
				+ getPropriedades().getProperty(NOME_PROJETO) + "/service/";
	}

	public static String getDiretorioProjetoEjb() {
		return getPropriedades().getProperty(PASTA_BASE) + SEPARADOR + getPropriedades().getProperty(NOME_PROJETO)
				+ "-ejb/";
	}

	public static String getDiretorioImplementacaoDao() {
		return getDiretorioProjetoEjb() + PASTA_MAVEN_JAVA + getPropriedades().getProperty(PACOTE_BASE)
				+ getPropriedades().getProperty(NOME_PROJETO) + "/dao";
	}

	public static String getDiretorioImplementacaoServices() {
		return getDiretorioProjetoEjb() + PASTA_MAVEN_JAVA + getPropriedades().getProperty(PACOTE_BASE)
				+ getPropriedades().getProperty(NOME_PROJETO) + "/service/";
	}

	/**
	 * Método utilizado para fins de teste.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getPropriedade(NOME_PROJETO));
		System.out.println(getDiretorioRaiz());
		System.out.println(getDiretorioProjetoWeb());
		System.out.println(getDiretorioPaginasWeb());
		System.out.println(getDiretorioController());
		System.out.println(getDiretorioProjetoApi());
		System.out.println(getDiretorioProjetoEjb());
		System.out.println(getDiretorioEntidades());
		System.out.println(getDiretorioInterfacesDao());
		System.out.println(getDiretorioInterfacesServices());
		System.out.println(getDiretorioImplementacaoDao());
		System.out.println(getDiretorioImplementacaoServices());
	}

}
