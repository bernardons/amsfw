package br.com.unisys.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;

import br.com.unisys.jdbc.CampoJDBC;
import br.com.unisys.jdbc.JDBCUtil;
import br.com.unisys.util.SystemProperties;

/**
 * Classe utilitária para montagem das Strings a partir das classes.
 * 
 * @author DelfimSM
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ClasseUtil {

	private ClasseUtil() {
	}

	/**
	 * Retorna o mapa de classes.
	 * 
	 * @return
	 */
	public static Map<String, String> getMapaClasses() {

		Map<String, String> classes = new HashMap<String, String>();

		Reflections reflections = new Reflections(SystemProperties.getPropriedade(SystemProperties.PACOTE_DOMINIO));
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

		Iterator<Class<?>> iterator = annotated.iterator();

		while (iterator.hasNext()) {
			Class classe = iterator.next();
			classes.put(classe.getSimpleName(), classe.getName());
		}

		return classes;
	}

	/**
	 * Retorna a lista de classes.
	 * 
	 * @return
	 */
	public static List<String> getListaClasses() {

		List<String> lista = new ArrayList<String>();

		Reflections reflections = new Reflections(SystemProperties.getPropriedade(SystemProperties.PACOTE_DOMINIO));
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

		Iterator<Class<?>> iterator = annotated.iterator();

		while (iterator.hasNext()) {
			Class classe = iterator.next();
			lista.add(classe.getSimpleName());
		}

		return lista;
	}

	/**
	 * Retorna a lista de classes.
	 * 
	 * @return
	 */
	public static Map<String, Field> getMapaMetodos(String nomeClasse) {

		Map<String, Field> metodos = new HashMap<String, Field>();
		Class<?> classe = null;
		try {
			classe = Class.forName(nomeClasse);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Field campo : classe.getDeclaredFields()) {
			if (!campo.getName().equals("serialVersionUID")) {
				if (!campoTransiente(campo)) {
					metodos.put(campo.getName(), campo);
					System.out.println(campo.getName());
				}
			}
		}

		return metodos;
	}

	/**
	 * Retorna a lista de classes.
	 * 
	 * @return
	 */
	public static List<String> getListaMetodos(String nomeClasse) {

		List<String> metodos = new ArrayList<String>();
		Class<?> classe = null;
		try {
			classe = Class.forName(nomeClasse);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Field campo : classe.getDeclaredFields()) {
			if (!campo.getName().equals("serialVersionUID")) {
				if (!campoTransiente(campo)) {
					metodos.add(campo.getName());
				}
			}
		}

		return metodos;
	}

	/**
	 * Retorna o tipo da chave da classe.
	 * 
	 * @return
	 */
	public static String getTipoChave(String nomeClasse) {

		String chave = null;
		Class<?> classe = null;
		try {
			classe = Class.forName(nomeClasse);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Field campo : classe.getDeclaredFields()) {
			for (Annotation anotacaoAtual : campo.getAnnotations()) {
				if (anotacaoAtual.annotationType().getName().equals(Id.class.getName())) {
					chave = campo.getType().getSimpleName();
				}
				break;
			}
		}

		return chave;
	}

	/**
	 * Verifica se o campo é transiente ou não.
	 * 
	 * @param field
	 * @return
	 */
	public static boolean campoTransiente(Field field) {
		Annotation[] anotacoes = field.getAnnotations();

		boolean possuiTransiente = false;

		for (Annotation anotacaoAtual : anotacoes) {
			if (anotacaoAtual.annotationType().getName().equals(Transient.class.getName())) {
				possuiTransiente = true;
				break;
			}
		}

		return possuiTransiente;
	}

	/**
	 * Recupera a lista de campos do nome da classe passado como parâmetro.
	 * 
	 * @param nomeClasse
	 * @return
	 */
	public static List<String> getStringCampos(String nomeClasse) {

		List<String> resultados = new ArrayList<String>();
		Class<?> classe = null;
		try {
			classe = Class.forName(nomeClasse);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Field campo : classe.getDeclaredFields()) {
			System.out.println(campo.getName() + " : " + campo.getType().getName());
		}

		return resultados;
	}

	/**
	 * Recupera a lista de campos de filtro.
	 * 
	 * @param classe
	 * @param campos
	 * @return
	 */
	public static List<String> getListaCamposFiltro(Class classe, List<Field> campos) {

		List<String> camposCadastro = new ArrayList<String>();

		for (Field campo : campos) {
			String campoCadastroAtual = getStringCampoCadastro(classe, campo);
			campoCadastroAtual = campoCadastroAtual.replace(".objeto.", ".objetoFiltro.");
			campoCadastroAtual = campoCadastroAtual.replace(" required=\"true\"", "");
			if (!StringUtils.isEmpty(campoCadastroAtual)) {
				camposCadastro.add(campoCadastroAtual);
			}
		}

		return camposCadastro;
	}

	/**
	 * Recupera a lista de campos de cadastro.
	 * 
	 * @param classe
	 * @param campos
	 * @return
	 */
	public static List<String> getListaCamposCadastro(Class classe, List<Field> campos) {

		List<String> camposCadastro = new ArrayList<String>();

		for (Field campo : campos) {
			String campoCadastroAtual = getStringCampoCadastro(classe, campo);
			if (!StringUtils.isEmpty(campoCadastroAtual)) {
				camposCadastro.add(campoCadastroAtual);
			}
		}

		return camposCadastro;
	}

	protected static String getStringCampoCadastro(Class classe, Field campo) {

		StringBuilder resultado = new StringBuilder();

		String nomeTabela = getNomeTabela(classe);
		String nomeCampo = getNomeColuna(campo);

		if (nomeTabela == null || nomeCampo == null) {
			return null;
		}
		CampoJDBC campoJDBC = JDBCUtil.getDadosColunas(nomeTabela, nomeCampo);

		String tipo = getTipo(campo);
		if (tipo.equals("")) {
			return "";
		}
		resultado.append(tipo).append(getIdentificador(campo));
		resultado.append(getValue(campo));
		resultado.append(getLabel(campo.getName()));
		resultado.append(getObrigatorio(campoJDBC));
		resultado.append(getMaxLength(campoJDBC));

		resultado.append(" />");

		return resultado.toString();
	}

	private static String getStringPrimeiraLetraMaiuscula(String valor) {
		return valor.substring(0, 1).toUpperCase() + valor.substring(1, valor.length());
	}

	/**
	 * Recupera a lista de Strings de Pesquisa para o DAO.
	 * 
	 * @param classe
	 * @param campos
	 * @return
	 */
	public static List<String> getListaStringPesquisaDAO(Class classe, List<Field> campos) {
		List<String> retorno = new ArrayList<String>();

		for (Field campo : campos) {
			String valor = getStringPesquisaDAO(classe, campo);
			if (valor != null && !valor.equals("")) {
				retorno.add(valor);
			}

		}

		return retorno;
	}

	protected static String getStringPesquisaDAO(Class classe, Field campo) {

		StringBuilder resultado = new StringBuilder();
		String prefixo = "";
		String path = "";
		String predicate = "";
		String addPredicate = "";

		if (campo.getType().getName().equals("java.lang.String")) {
			prefixo =
					"\t\tif (filtro.get" + getStringPrimeiraLetraMaiuscula(campo.getName())
							+ "() != null && !filtro.get" + getStringPrimeiraLetraMaiuscula(campo.getName())
							+ "().equals(\"\")) { \n";
			path = "\t\t\tExpression<String> path = root.get(\"" + campo.getName() + "\");\n";
			predicate =
					"\t\t\tPredicate condicao = criteriaBuilder.like(criteriaBuilder.lower(path), filtro.get"
							+ getStringPrimeiraLetraMaiuscula(campo.getName())
							+ "().toLowerCase() + \"%\");\n";
			addPredicate = "\t\t\tlistaPredicate.add(condicao);\n\t\t}";
		} else if (campo.getType().getName().equals("java.lang.Long")
				|| campo.getType().getName().equals("java.lang.Integer")
				|| campo.getType().getName().equals("java.util.Date")
				|| campo.getType().getName().equals("java.math.BigDecimal")) {

			prefixo =
					"\t\tif (filtro.get" + getStringPrimeiraLetraMaiuscula(campo.getName())
							+ "() != null) { \n";
			path = "\t\t\tExpression<String> path = root.get(\"" + campo.getName() + "\");\n";
			predicate =
					"\t\t\tPredicate condicao = criteriaBuilder.equal(path, filtro.get"
							+ getStringPrimeiraLetraMaiuscula(campo.getName())
							+ "());\n";
			addPredicate = "\t\t\tlistaPredicate.add(condicao);\n\t\t}";
		}

		resultado.append(prefixo);
		resultado.append(path);
		resultado.append(predicate);
		resultado.append(addPredicate);

		return resultado.toString();
	}

	/**
	 * Recupera a lista de campos da tabela de pesquisa.
	 * 
	 * @param classe
	 * @param campos
	 * @return
	 */
	public static List<String> getListaCamposTabelaPesquisa(Class classe, List<Field> campos) {

		List<String> camposTabelaPesquisa = new ArrayList<String>();

		for (Field campo : campos) {
			String campoTabelaPesquisaAtual = getStringColunasTabelaPesquisa(classe, campo);
			if (!StringUtils.isEmpty(campoTabelaPesquisaAtual)) {
				camposTabelaPesquisa.add(campoTabelaPesquisaAtual);
			}
		}

		return camposTabelaPesquisa;
	}

	/**
	 * Recupera a String dos colulas da tabela de pesquisa.
	 * 
	 * @param classe
	 * @param campo
	 * @return
	 */
	public static String getStringColunasTabelaPesquisa(Class classe, Field campo) {

		StringBuilder resultado = new StringBuilder();

		String nomeTabela = getNomeTabela(classe);
		String nomeCampo = getNomeColuna(campo);

		CampoJDBC campoJDBC = JDBCUtil.getDadosColunas(nomeTabela, nomeCampo);

		if (nomeTabela == null || nomeCampo == null) {
			return null;
		}

		String tipo = getTipo(campo);
		if (tipo.equals("")) {
			return "";
		}

		String textoInicio = "<p:column";
		String label = getLabel(campo.getName()).replace("label", "headerText");
		String identificador = getIdentificador(campo).replace("identificador", "id");
		String textoMeio = " styleClass=\"sorting\" style=\"width:auto\" >";

		String valor = "";
		if (campoJDBC.getTipo().equals("date")) {
			valor =
					"<h:outputText value=\"#{objeto." + campo.getName()
							+ "}\"><f:convertDateTime pattern=\"dd/MM/yyyy\" /> </h:outputText>";
		} else {
			valor = "#{objeto." + campo.getName() + "}";
		}

		String textoFinal = "</p:column>";

		resultado.append(textoInicio).append(label);
		resultado.append(identificador);
		resultado.append(textoMeio);
		resultado.append(valor);
		resultado.append(textoFinal);

		return resultado.toString();
	}

	protected static String getTipo(Field campo) {
		if (campo.getType().getName().equals("java.lang.String")) {
			return "<u:input";
		} else if (campo.getType().getName().equals("java.lang.Long")
				|| campo.getType().getName().equals("java.lang.Integer")) {
			return "<u:number";
		} else if (campo.getType().getName().equals("java.util.Date")) {
			return "<u:data";
		} else if (campo.getType().getName().equals("java.math.BigDecimal")) {
			return "<u:moeda";
		}
		return "";
	}

	protected static String getIdentificador(Field campo) {
		return " identificador=\"" + campo.getName() + "\"";
	}

	/**
	 * Recupera a String dividida por letra maiúscula.
	 * 
	 * @param nomeCampo
	 * @return
	 */
	public static String[] getStringDivididaPorLetraMaiuscula(String nomeCampo) {
		String[] stringDivididaMaiuscula = nomeCampo.split("(?=\\p{Upper})");

		return stringDivididaMaiuscula;

	}

	protected static String getLabel(String nomeCampo) {

		String[] stringDivididaMaiuscula = nomeCampo.split("(?=\\p{Upper})");
		StringBuilder builder = new StringBuilder();

		for (String valorAtual : stringDivididaMaiuscula) {
			if (valorAtual.length() > 1) {
				builder.append(valorAtual.substring(0, 1).toUpperCase() + valorAtual.substring(1) + " ");
			} else {
				builder.append(valorAtual);
			}
		}

		String retorno = builder.toString();
		if (retorno.substring(retorno.length() - 1).equals(" ")) {
			retorno = retorno.substring(0, retorno.length() - 1);
		}

		return " label=\"" + retorno + "\"";
	}

	protected static String getObrigatorio(CampoJDBC campoJdbc) {
		if (!campoJdbc.isPermiteNulo()) {
			return " required=\"true\"";
		}
		return "";
	}

	protected static String getValue(Field campo) {
		return " value=\"#{controller.objeto." + campo.getName() + "}\"";
	}

	protected static String getMaxLength(CampoJDBC campoJdbc) {
		if (campoJdbc.getTipo().equals("integer") || campoJdbc.getTipo().equals("smallint")
				|| campoJdbc.getTipo().equals("numeric")) {
			return " maxlenght=\"" + campoJdbc.getPrecisaoNumerica() + "\"";
		} else if (campoJdbc.getTipo().equals("character") || campoJdbc.getTipo().equals("character varying")) {
			return " maxlenght=\"" + campoJdbc.getTamanhoString() + "\"";
		}

		return "";
	}

	/**
	 * Recupera o nome da tabela.
	 * 
	 * @param classe
	 * @return
	 */
	public static String getNomeTabela(Class classe) {
		Table tabela = (Table) classe.getAnnotation(Table.class);

		return tabela.name();
	}

	/**
	 * Recupera o nome da coluna.
	 * 
	 * @param campo
	 * @return
	 */
	public static String getNomeColuna(Field campo) {
		Column coluna = (Column) campo.getAnnotation(Column.class);
		
		if (coluna != null && coluna.name() != null && !coluna.name().equals("")) {
			return coluna.name();
		}

		return campo.getName();
	}

	/**
	 * Método de teste.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
