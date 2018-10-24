package com.unisys.br.amsfw.test.hibernatefix;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.LoadState;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.ejb.util.PersistenceUtilHelper;

/**
 * Classe Hibernate Persistence 2.
 * 
 * @author DelfimSM
 *
 */
public class HibernatePersistence2 extends HibernatePersistence {

	/**
	 * Construtor padrão.
	 * 
	 */
	public HibernatePersistence2() {
		super();
	}

	/**
	 * Cria o entity manager factory.
	 */
	@SuppressWarnings("rawtypes")
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
		return super.createEntityManagerFactory(persistenceUnitName, properties);
	}

	/**
	 * Cria o container do entity manager factory.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {

		Ejb3Configuration cfg = new Ejb3Configuration();

		if (properties == null) {
			properties = new HashMap();
		}

		// to avoid calling
		// javax.persistence.spi.PersistenceUnitInfo.getValidationMode() in
		// Ejb3Configuration.configure(PersistenceUnitInfo info, Map
		// integration)
		// that causes: java.lang.NoSuchMethodError:
		// javax.persistence.spi.PersistenceUnitInfo.getValidationMode
		// with JBoss version of Java EE 6.0
		// estamos forçando AvailableSettings.VALIDATION_MODE
		properties.put(AvailableSettings.VALIDATION_MODE, "auto"); // javax.persistence.validation.mode

		// avoid calling
		// javax.persistence.spi.PersistenceUnitInfo.getSharedCacheMode()
		properties.put(AvailableSettings.SHARED_CACHE_MODE, "all");

		Ejb3Configuration configured = cfg.configure(info, properties);
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	/**
	 * Cria o entity manager factory. 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public EntityManagerFactory createEntityManagerFactory(Map properties) {
		return super.createEntityManagerFactory(properties);
	}

	private final ProviderUtil providerUtil = new ProviderUtil() {
		public LoadState isLoadedWithoutReference(Object proxy, String property) {
			return PersistenceUtilHelper.isLoadedWithoutReference(proxy, property);
		}

		public LoadState isLoadedWithReference(Object proxy, String property) {
			return PersistenceUtilHelper.isLoadedWithReference(proxy, property);
		}

		public LoadState isLoaded(Object o) {
			return PersistenceUtilHelper.isLoaded(o);
		}
	};

	/**
	 * {@inheritDoc}
	 */
	public ProviderUtil getProviderUtil() {
		return providerUtil;
	}

}
