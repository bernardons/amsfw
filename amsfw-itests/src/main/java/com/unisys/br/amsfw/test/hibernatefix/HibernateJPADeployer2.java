package com.unisys.br.amsfw.test.hibernatefix;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.transaction.TransactionManager;

import com.googlecode.mycontainer.jpa.HibernateJPADeployer;

/**
 * Classe Hibernate JPA Deployer para recuperar automaticamente os dados do Hibernate.
 * 
 * @author DelfimSM
 *
 */
public class HibernateJPADeployer2 extends HibernateJPADeployer {
	
	private static final long serialVersionUID = -3371095595629140262L;
	
	/**
	 * Construtor.
	 * 
	 */
	public HibernateJPADeployer2() {
		super();
	}
	
	@Override
	protected PersistenceProvider getPersistenceProvider() {
		return new HibernatePersistence2(); 
		// need to avoid call for org.hibernate.ejb.HibernatePersistence.createContainerEntityManagerFactory
	}
	
	@Override
	protected void overrrideConfig(PersistenceUnitInfo info) {
		super.overrrideConfig(info);
	}
	
	@Override
	protected TransactionManager getTransactionManager(PersistenceUnitInfo info) {
		return super.getTransactionManager(info);
	}
	
}
