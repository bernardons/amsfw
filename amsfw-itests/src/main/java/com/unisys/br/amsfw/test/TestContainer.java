package com.unisys.br.amsfw.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.packaging.PersistenceMetadata;
import org.hibernate.ejb.packaging.PersistenceXmlLoader;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;

import com.googlecode.mycontainer.datasource.DataSourceDeployer;
import com.googlecode.mycontainer.ejb.SessionInterceptorDeployer;
import com.googlecode.mycontainer.ejb.StatelessDeployer;
import com.googlecode.mycontainer.jpa.JPADeployer;
import com.googlecode.mycontainer.jpa.JPAInfoBuilder;
import com.googlecode.mycontainer.jta.MyTransactionManagerDeployer;
import com.googlecode.mycontainer.kernel.ShutdownCommand;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.kernel.deploy.DeployException;
import com.unisys.br.amsfw.test.hibernatefix.HibernateJPADeployer2;
import com.unisys.br.amsfw.test.util.ConfigUtil;

/**
 * Representa um container JEE para o ambiente de teste/desenvolvimento.
 * Atualmente usando o mycontainer.
 * 
 * @author Pedro Gontijo
 */
public class TestContainer {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestContainer.class);

	/*
	 * Singleton para forçar que só tenha uma instância do container para todos
	 * os teste. É importante que os teste rodem no mesmo classloader para tal.
	 */
	private static TestContainer instance = new TestContainer();

	private ContainerBuilder builder;

	private String databaseURL;
	private String databaseUser;
	private String databasePwd;
	private String databaseDriver;
	private String databaseDialect;

	private boolean started;

	private List<JPADeployer> jpaDeployers = new ArrayList<JPADeployer>();

	private TestContainer() {
	}

	public static TestContainer getInstance() {
		return instance;
	}

	public String getDatabaseURL() {
		return databaseURL;
	}

	public void setDatabaseURL(String databaseURL) {
		this.databaseURL = databaseURL;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	public String getDatabasePwd() {
		return databasePwd;
	}

	public void setDatabasePwd(String databasePwd) {
		this.databasePwd = databasePwd;
	}

	public String getDatabaseDriver() {
		return databaseDriver;
	}

	public void setDatabaseDriver(String databaseDriver) {
		this.databaseDriver = databaseDriver;
	}

	public String getDatabaseDialect() {
		return databaseDialect;
	}

	public void setDatabaseDialect(String databaseDialect) {
		this.databaseDialect = databaseDialect;
	}

	/**
	 * Inicializa o container de teste.
	 * 
	 * @throws NamingException
	 * @throws IOException
	 */
	public void start() throws NamingException, IOException {
		if (started) {
			return;
		}

		LOGGER.debug("Iniciando myContainer.");

		Properties properties = new Properties();
		properties.setProperty(
				"java.naming.factory.initial",
				"com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory");

		try {
			builder = new ContainerBuilder(properties);
			builder.deployVMShutdownHook();

			SessionInterceptorDeployer sessionInterceptorDeployer =
					builder.createDeployer(SessionInterceptorDeployer.class);
			// sessionInterceptorDeployer.setName("TransactionManager");
			sessionInterceptorDeployer.deploy();
			// builder.deployJTA(); // deprecated com mycontainer 1.4.0

			// new
			MyTransactionManagerDeployer myTransactionManagerDeployer =
					builder.createDeployer(MyTransactionManagerDeployer.class);
			myTransactionManagerDeployer.setName("TransactionManager");
			myTransactionManagerDeployer.deploy();

			// StatelessDeployer statelessDeployer =
			// builder.createDeployer(StatelessDeployer.class);
			// statelessDeployer.deploy(YourServiceBean.class);
			// InitialContext ctx = builder.getContext();

			for (PersistenceMetadata persistenceUnit : getPersistences()) {
				LOGGER.info("Deploy do persistenceUnit [" + persistenceUnit.getName() + "]");
				deployJPAPersistenceUnits(persistenceUnit);
			}

			deploySessionBeans();

			started = true;
			LOGGER.info("myContainer foi iniciado com sucesso.");
		} catch (RuntimeException e) {
			LOGGER.error("Ocorreu um erro durante o start do myContainer. Invocando shutdown do servidor.", e);
			this.stop();
			throw e;
		}
	}

	private List<PersistenceMetadata> getPersistences() throws IOException {
		LOGGER.debug("Pesquisando persistence.xml do classpath.");

		List<PersistenceMetadata> persistences = new ArrayList<PersistenceMetadata>();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> persistenceURLs;

		try {
			persistenceURLs = classLoader.getResources("META-INF/persistence.xml");
		} catch (IOException e) {
			LOGGER.error("Ocorreu um erro ao tentar pesquisar arquivos persistence.xml no classpath.", e);
			throw e;
		}

		EntityResolver entityResolver = new Ejb3Configuration().getHibernateConfiguration().getEntityResolver();

		URL persistenceURL = null;

		try {
			if (persistenceURLs != null) {
				while (persistenceURLs.hasMoreElements()) {
					persistenceURL = persistenceURLs.nextElement();
					LOGGER.debug("Localizado persistence.xml [{}]", persistenceURL.toString());
					persistences.addAll(PersistenceXmlLoader.deploy(
							persistenceURL,
							Collections.EMPTY_MAP,
							entityResolver));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Ocorreu um erro ao tentar carregar o persistence [" + persistenceURL + "].", e);
			throw new RuntimeException(e);
		}

		return persistences;
	}

	private void deployDataSource(String dataSourceName) {
		LOGGER.debug("Criando data source [{}]", dataSourceName);

		DataSourceDeployer ds = builder.createDeployer(DataSourceDeployer.class);
		ds.setName(dataSourceName);
		ds.setDriver(databaseDriver);
		ds.setUrl(databaseURL);
		ds.setUser(databaseUser);
		ds.setPass(databasePwd);
		try {
			ds.deploy();
		} catch (DeployException e) {
			if (!(e.getCause() instanceof NameAlreadyBoundException)) { // ignora
				throw e;
			}
		}
	}

	private void deployJPAPersistenceUnits(PersistenceMetadata persistenceUnit) {
		LOGGER.debug("Criando persistence unit [{}].", persistenceUnit.getName());

		boolean usesDataSource =
				persistenceUnit.getJtaDatasource() != null && persistenceUnit.getJtaDatasource().length() > 0;

		// JPADeployer jpa = builder.createDeployer(HibernateJPADeployer.class);
		// // JPADeployer é abtract
		JPADeployer jpa = builder.createDeployer(HibernateJPADeployer2.class); //
		JPAInfoBuilder info = (JPAInfoBuilder) jpa.getInfo();

		info.setPersistenceUnitName(persistenceUnit.getName());

		if (usesDataSource) {
			deployDataSource(persistenceUnit.getJtaDatasource());
			info.setJtaDataSourceName(persistenceUnit.getJtaDatasource());
		}

		// Copia as propriedas carregadas da persistenceUnit (do xml) para o
		// myContainer
		if (persistenceUnit.getProps() != null) {
			for (Object property : persistenceUnit.getProps().keySet()) {
				LOGGER.debug("Setando propriedade [" + property + "] com valor ["
						+ persistenceUnit.getProps().getProperty(property.toString()) + "] na persistence unit ["
						+ persistenceUnit.getName() + "]");
				info.getProperties().put(
						property.toString(),
						persistenceUnit.getProps().getProperty(property.toString()));
			}
		}

		overrideJPAProperties(persistenceUnit.getName(), info.getProperties(), usesDataSource);

		addPersistenceClasses(persistenceUnit, info);

		// Para poder apagar todo o cache L2
		jpaDeployers.add(jpa);

		jpa.deploy();
	}

	/**
	 * Adiciona as classes de persistência no sistema. As classes a serem
	 * realizado o deploy estão definidas no arquivo de configuração e tem o
	 * profixo da chave como entidade.
	 * 
	 * @param info
	 */
	protected void addPersistenceClasses(PersistenceMetadata persistenceUnit, JPAInfoBuilder info) {
		Class<?> entityClass = null;

		for (String classeAtual : persistenceUnit.getClasses()) {
			LOGGER.debug("Adicionando JPA entity: " + classeAtual);

			try {
				entityClass = Thread.currentThread().getContextClassLoader().loadClass(classeAtual);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Classe de entidade [" + classeAtual + "] não encontrada.", e);
				continue;
			}

			if (!info.getJarFileUrls().contains(entityClass.getProtectionDomain().getCodeSource().getLocation())) {
				info.addJarFileUrl(entityClass);
			}
		}
	}

	/**
	 * Adiciona as configurações do JPA.
	 * 
	 * @param props
	 * @param usesDataSource
	 */
	private void overrideJPAProperties(String persistenceUnit, Properties props, boolean usesDataSource) {
		// Se o persistence.xml não usa datasource substitui os valores de
		// conexão do persistence unit para usar o banco de teste
		if (!usesDataSource) {
			props.setProperty("hibernate.connection.driver_class", databaseDriver);
			props.setProperty("hibernate.connection.url", databaseURL);
			props.setProperty("hibernate.connection.username", databaseUser);
			props.setProperty("hibernate.connection.password", databasePwd);
		} else {
			props.remove("hibernate.connection.driver_class");
			props.remove("hibernate.connection.url");
			props.remove("hibernate.connection.username");
			props.remove("hibernate.connection.password");
		}

		// Força o create-drop, caso contrário não irá criar as tabelas
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		// Força o dialect para ser compatível com o banco de teste
		props.setProperty("hibernate.dialect", databaseDialect);

		// Devido a um bug do myContainer, caso seja especificado o JNDI da
		// session factory, um bind é feito aqui.
		String sessionFactoryJndi = props.getProperty("hibernate.session_factory_name");
		if (sessionFactoryJndi != null) {
			try {
				getContext().bind(sessionFactoryJndi, "");
			} catch (NamingException e) {
				LOGGER.warn("Não foi possível bindar no JNDI o elemento [" + sessionFactoryJndi
						+ "]. Podem ocorrem problemas na inicialização do myContainer. "
						+ "Neste caso, inclua no arquivo " + ConfigUtil.ITESTS_CONFIG_PROPERTIES + " a propriedade "
						+ ConfigUtil.PREFIX_JPA_PROPERTIES + persistenceUnit
						+ "_hibernate.session_factory_name com valor vazio.");
			}
		}

		// Substitui as propriedades que foram setadas no arquivo
		// itests-config.properties
		Map<String, String> configProperties = ConfigUtil.getJPAProperties(persistenceUnit);

		for (String key : configProperties.keySet()) {
			String value = configProperties.get(key);
			LOGGER.debug("Substituindo propriedade [{}] na persistence unit [{}] por [{}]", new Object[] {
					key,
					persistenceUnit,
					value });
			props.setProperty(key, value);
		}
	}

	private void deploySessionBeans() {
		LOGGER.debug("Pesquisando stateless session beans.");
		
		Collection<String> contextPackages = ConfigUtil.getEJBContextPackages();
		Collection<String> excludeStatelessClasses = ConfigUtil.getExcludeStatelessClasses();

		Set<Class<?>> annotatedClasses = new HashSet<Class<?>>();
		
		for (String contextPackage : contextPackages) {
			LOGGER.debug("Carregando beans do pacote [{}].", contextPackage);
			Reflections reflections = new Reflections(contextPackage.trim());
			annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Stateless.class));
		}
		
		for (Class<?> statelessClass : annotatedClasses) {
			LOGGER.debug("Fazendo deploy do stateless [{}].", statelessClass);
			
			if (!excludeStatelessClasses.contains(statelessClass.getName())) {
				StatelessDeployer deployer = builder.createDeployer(StatelessDeployer.class);
				deployer.deploy(statelessClass);
			} else {
				LOGGER.debug("  Classe consta na lista de classes stateless que deve ser ignorada[{}].", statelessClass);
			}
		}
	}

	/**
	 * Para o Mycontainer.
	 * 
	 */
	public void stop() {
		LOGGER.debug("Parando myContainer.");
		ShutdownCommand shutdownCommand = new ShutdownCommand();
		shutdownCommand.setContext(builder.getContext());
		shutdownCommand.shutdown();
		started = false;
		LOGGER.info("myContainer foi parado com sucesso.");
	}

	public Context getContext() {
		return builder.getContext();
	}

	public boolean isStarted() {
		return started;
	}

}
