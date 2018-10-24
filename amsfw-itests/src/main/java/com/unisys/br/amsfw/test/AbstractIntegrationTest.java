package com.unisys.br.amsfw.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.dbunit.IOperationListener;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.IMetadataHandler;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unisys.br.amsfw.test.testbuilder.TestBuilder;
import com.unisys.br.amsfw.test.testbuilder.TestBuilderUtil;
import com.unisys.br.amsfw.test.util.ConfigUtil;

/**
 * <p>
 * Classe pai para testes integrados.
 * </p>
 * <p>
 * Carrega automaticamente EJBs, data sources, persistence units JPA e demais
 * dependencias em um container JEE (myContainer). Cria um banco de dados em
 * memória (H2) e popula dados com DBUnit.
 * </p>
 * <p>
 * Os dados podem ser colocados num arquivo base.xls na raiz do classpath (pasta
 * src/test/resources para projetos maven) ou através do uso de
 * {@link TestBuilder} para carregamento de busca dados específicos de cada
 * teste.
 * </p>
 * <p>
 * Orientações de uso:
 * <ul>
 * <li>Não utilize o arquivo import.sql (do hibernate) para carregar dados.
 * Utilize {@link TestBuilder}s.</li>
 * <li>Pode-se utilizar o import.sql para possíveis modificações (DDL) no banco
 * criado.</li>
 * <li>Configurações dos persistence.xml podem ser alteradas através do arquivo
 * itests-config.properties na raiz do classpath (para projetos maven, em
 * src/test/resources).<br/>
 * O padrão deve ser: jpa.[NOME DA PERSISTENCE UNIT]_[PROPRIEDADE]=[VALOR]<br/>
 * Por exemplo:
 * <ul>
 * <li>jpa.siaci_hibernate.cache.use_query_cache = false</li>
 * <li>jpa.siaci_hibernate.cache.use_second_level_cache = false</li>
 * </ul>
 * </ul>
 * </p>
 * 
 * @author Pedro Gontijo
 */
public abstract class AbstractIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationTest.class);

	private static final H2DataTypeFactory DB_DATA_TYPE_FACTORY = new org.dbunit.ext.h2.H2DataTypeFactory();
	private static final String DB_PWD = "";
	private static final String DB_USER = "sa";
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_DIALECT = "org.hibernate.dialect.H2Dialect";
	private static JdbcDatabaseTester databaseTester;
	private static DatabaseSequenceFilter databaseSequenceFilter;
	private static TestContainer container;

	static {
		configureLog4J();
	}

	private static String getDbURL() {
		StringBuffer dbURL = new StringBuffer("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL;");

		String[] dbURLs = ConfigUtil.getDBSchemas();
		dbURL.append("INIT=CREATE TYPE IF NOT EXISTS bpchar AS VARCHAR\\;");

		if (dbURLs.length > 0) {
			
			for (int i = 0; i < dbURLs.length; i++) {
				dbURL.append("CREATE SCHEMA IF NOT EXISTS ");
				dbURL.append(dbURLs[i]);
				dbURL.append("\\;");
			}
			
			dbURL.append("SET SCHEMA ");
			dbURL.append(dbURLs[0]);
			dbURL.append(";");
		}
		return dbURL.toString();
	}
	
	/**
	 * Antes de iniciar os testes carrega o container e configura o banco.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void boot() throws Exception {
		loadContainer();
		configureDBUnit();
	}

	/**
	 * Executa o shutdow do container assim que acabar todos os testes.
	 * 
	 */
	@AfterClass
	public static void shutdown() {
		container.stop();
	}

	/**
	 * Carrega os dados de teste.
	 * 
	 * @throws Exception
	 */
	@Before
	public void loadTestData() throws Exception {
		IDataSet testDataSet = getDataSets();

		databaseTester.setDataSet(testDataSet);

		if (testDataSet != null) {
			databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			databaseTester.setTearDownOperation(DatabaseOperation.NONE);
		} else {
			databaseTester.setSetUpOperation(DatabaseOperation.NONE);
			databaseTester.setTearDownOperation(DatabaseOperation.NONE);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Executando DBUnit...");
		}

		databaseTester.onSetup();

		LOGGER.info("Dados carregados com sucesso. Executando teste...");
	}

	/**
	 * Limpa o banco depois de rodar o teste.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		LOGGER.info("Teste executado!");

		Connection conn = null;
		try {
			conn = databaseTester.getConnection().getConnection();
			conn.createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE;");
			databaseTester.onTearDown();
			conn.createStatement().execute("SET REFERENTIAL_INTEGRITY TRUE;");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Carrega o container do framework Mycontainer.
	 * 
	 * @throws NamingException
	 * @throws ParserException
	 * @throws IOException
	 */
	private static void loadContainer() throws NamingException, IOException {
		if (container != null && container.isStarted()) {
			return;
		}

		LOGGER.info("Iniciando container para teste.");

		container = TestContainer.getInstance();
		container.setDatabaseURL(getDbURL());
		container.setDatabaseUser(DB_USER);
		container.setDatabasePwd(DB_PWD);
		container.setDatabaseDriver(DB_DRIVER);
		container.setDatabaseDialect(DB_DIALECT);
		container.start();
	}

	private static void configureDBUnit() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Configurando DBUnit");
		}

		// Configura DBUnit
		databaseTester = new JdbcDatabaseTester(DB_DRIVER, getDbURL(), DB_USER, DB_PWD);
		databaseTester.setOperationListener(new IOperationListener() {
			public void operationTearDownFinished(IDatabaseConnection connection) {
			}

			public void operationSetUpFinished(IDatabaseConnection connection) {
			}

			public void connectionRetrieved(IDatabaseConnection connection) {
				connection.getConfig().setProperty(
						"http://www.dbunit.org/properties/datatypeFactory",
						DB_DATA_TYPE_FACTORY);
				connection.getConfig().setProperty(
						"http://www.dbunit.org/features/qualifiedTableNames",
						true);
			}
		});

		Collection<String> nomesTabelas = recuperaTodasTabelasBD();
		IDatabaseConnection connection = null;
		try {
			connection = databaseTester.getConnection();
			databaseSequenceFilter = new DatabaseSequenceFilter(connection, nomesTabelas.toArray(new String[] {}));
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * Cria um {@link IDataSet} com as tabelas e dados do arquivo
	 * <code>/base.xls</code>.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static IDataSet getBasicDataSet() throws Exception {
		return getXlsDataSet("/base.xls");
	}

	/**
	 * Cria um {@link IDataSet} com as tabelas e dados do arquivo.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static IDataSet getDataSet(String fileName) throws Exception {
		if (fileName.substring(fileName.length() - 3).toUpperCase().equals("XLS")) {
			return getXlsDataSet(fileName);
		} else {
			return getFlatXmlDataSet(fileName);
		}
	}

	/**
	 * Cria um {@link IDataSet} do tipo {@link FlatXmlDataSet} com o nome do arquivo
	 * informado.
	 * 
	 * @param fileName
	 *            Arquivo xml
	 * @return
	 * @throws Exception
	 */
	protected static IDataSet getFlatXmlDataSet(String fileName) throws Exception {
		InputStream resource = AbstractIntegrationTest.class.getResourceAsStream(fileName);

		if (resource == null) {
			LOGGER.info(fileName + " - Arquivo informado para carregamento de dados não existe !");
			return null;
		}

		FlatXmlDataSet xmlDataSet = new FlatXmlDataSetBuilder().build(resource);
		
		ReplacementDataSet dataSet =
				new ReplacementDataSet(new FilteredDataSet(databaseSequenceFilter, xmlDataSet));
		dataSet.addReplacementObject("", null);

		return dataSet;
	}
	
	/**
	 * Cria um {@link IDataSet} do tipo {@link XlsDataSet} com o nome do arquivo
	 * informado.
	 * 
	 * @param fileName
	 *            Arquivo xls
	 * @return
	 * @throws Exception
	 */
	protected static IDataSet getXlsDataSet(String fileName) throws Exception {
		InputStream resource = AbstractIntegrationTest.class.getResourceAsStream(fileName);

		if (resource == null) {
			LOGGER.info(fileName + " - Arquivo informado para carregamento de dados não existe !");
			return null;
		}

		ReplacementDataSet dataSet =
				new ReplacementDataSet(new FilteredDataSet(databaseSequenceFilter, new XlsDataSet(resource)));
		dataSet.addReplacementObject("", null);

		return dataSet;
	}

	/**
	 * Retorna uma nova conexão para o banco de dados criado para o teste.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected Connection getConnection() throws Exception {
		return databaseTester.getConnection().getConnection();
	}

	/**
	 * Retorna o {@link TransactionManager} para que se possa manipular
	 * transações manualmente.
	 * 
	 * @return
	 * @throws NamingException
	 */
	protected TransactionManager getTransactionManager() throws NamingException {
		return (TransactionManager) getContainerContext().lookup("TransactionManager");
	}

	protected IDataSet getDataSets() throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Pesquisando datasets...");
		}

		Collection<String> xlsBuilders = TestBuilderUtil.getAllBuilders(null, getTestBuilderList());
		List<String> tabelasBD = recuperaTodasTabelasBD();

		IDataSet basicDataSet = getBasicDataSet();

		IDataSet[] arranjo = null;
		int i = 0;

		if (basicDataSet != null) {
			arranjo = new IDataSet[xlsBuilders.size() + tabelasBD.size() + 1];
			arranjo[i++] = basicDataSet;
		} else {
			arranjo = new IDataSet[xlsBuilders.size() + tabelasBD.size()];
		}

		for (String fileBuilder : xlsBuilders) {
			arranjo[i++] = getDataSet(fileBuilder);
		}

		for (String tabela : tabelasBD) {
			arranjo[i++] = new DefaultDataSet(new DefaultTable(tabela));
		}

		if (LOGGER.isDebugEnabled()) {
			for (IDataSet dataSet : arranjo) {
				LOGGER.debug("Dataset [" + dataSet + "]");
			}
		}

		return new FilteredDataSet(databaseSequenceFilter, new CompositeDataSet(arranjo));
	}

	private static List<String> recuperaTodasTabelasBD() throws Exception {
		IDatabaseConnection connection = null;
		List<String> stringNomes = null;

		try {
			connection = databaseTester.getConnection();
            DatabaseMetaData databaseMetaData = connection.getConnection().getMetaData();
            DatabaseConfig config = connection.getConfig();
            String[] tableType = (String[]) config.getProperty(DatabaseConfig.PROPERTY_TABLE_TYPE);
            IMetadataHandler metadataHandler = (IMetadataHandler) config.getProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER);

            ResultSet resultSet = metadataHandler.getTables(databaseMetaData, null, tableType);
            stringNomes = new ArrayList<String>();

            while (resultSet.next()) {
                String schemaName = metadataHandler.getSchema(resultSet);
                String tableName = resultSet.getString(3);
                stringNomes.add(schemaName + "." + tableName);
            }

			String excludeTables = ConfigUtil.getProperty("exclude_table_names");

			if (excludeTables != null && !excludeTables.trim().equals("")) {
				for (String tabelaAtual : excludeTables.split(",")) {
					stringNomes.remove(tabelaAtual.trim());
				}
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return stringNomes;
	}

	/**
	 * Retorna o contexto JNDI do container JEE usado para o teste.
	 * 
	 * @return Contexto JNDI do container de teste.
	 */
	protected Context getContainerContext() {
		return container.getContext();
	}

	/**
	 * Configura o log4j pesquisando no classpath por arquivos log4j.xml e
	 * log4j.properties. Caso não sejam encontrados o {@link BasicConfigurator}
	 * é utilizado.
	 */
	private static void configureLog4J() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		URL log4jConfig = contextClassLoader.getResource("log4j.xml");

		if (log4jConfig != null) {
			DOMConfigurator.configure(log4jConfig);
			LOGGER.warn("Arquivo de configuração do log4j sendo utilizado [{}]", log4jConfig.toString());
			return;
		}

		log4jConfig = contextClassLoader.getResource("log4j.properties");

		if (log4jConfig != null) {
			PropertyConfigurator.configure(log4jConfig);
			LOGGER.warn("Arquivo de configuração do log4j sendo utilizado [{}]", log4jConfig.toString());
			return;
		}

		BasicConfigurator.configure();
		LOGGER.warn("Nenhum arquivo de configuração do Log4j foi encontrado. Usando configuração padrão.");
	}

	/**
	 * Retorna uma coleção de {@link Class}es do tipo {@link TestBuilder} que
	 * especificam os arquivos *.xls com os dados necessários para a execução do
	 * teste.
	 * 
	 * @return
	 * @see TestBuilderUtil
	 */
	protected abstract Collection<Class<? extends TestBuilder>> getTestBuilderList();

}