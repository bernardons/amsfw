package com.unisys.br.amsfw.dao.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.unisys.br.amsfw.dao.GenericDAOLocal;
import com.unisys.br.amsfw.dao.exception.MuitosResultadosException;
import com.unisys.br.amsfw.domain.Entidade;

/**
 * DAO Genérico do sistema. Possui os métodos genéricos de acesso a banco.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public abstract class GenericDAO<T extends Entidade, C extends Serializable> implements GenericDAOLocal<T, C> {

	public static final Integer MAXRESULTS = 50;

	private Class<T> persistenceClass = null;

	/**
	 * Método que recupera o Entity Manager.
	 * 
	 * @return
	 */
	public abstract EntityManager getEntityManager();

	/**
	 * {@inheritDoc}
	 */
	public void remover(T entidade) {
		this.getEntityManager().remove(entidade);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removerPorId(C id) {
		String hql = "delete from " + getPersistenceClass().getName() + " en where en.id = ?";
		Query query = this.getEntityManager().createQuery(hql);
		query.setParameter(1, id);
		query.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	public T salvar(T entidade) {

		this.getEntityManager().merge(entidade);
		this.getEntityManager().flush();

		return entidade;
	}

	/**
	 * {@inheritDoc}
	 */
	public T merge(T entidade) {
		T resultado = this.getEntityManager().merge(entidade);

		return resultado;
	}

	/**
	 * {@inheritDoc}
	 */
	public T recuperarPorId(C id) {
		return this.getEntityManager().find(getPersistenceClass(), id);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> recuperarPorIds(String nomePropriedadeId, C... ids) {

		CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getPersistenceClass());
		Root<T> root = criteriaQuery.from(getPersistenceClass());

		Object[] idsObjetos = ids;
		criteriaQuery = criteriaQuery.select(root).where(root.get(nomePropriedadeId).in(idsObjetos));

		TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);

		return typedQuery.getResultList();

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws MuitosResultadosException
	 */
	public List<T> recuperarTodos(Order... ordenacoes) throws MuitosResultadosException {
		List<T> retorno = null;

		TypedQuery<T> q =
				this.getEntityManager().createQuery("select c from " + getPersistenceClass().getSimpleName() + " c "
						+ adicionaOrderByHql(ordenacoes), getPersistenceClass());
		q.setMaxResults(MAXRESULTS);
		retorno = q.getResultList();

		if (retorno.size() >= MAXRESULTS) {
			throw new MuitosResultadosException();
		}

		return retorno;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws MuitosResultadosException
	 */
	public List<T> recuperarTodosOrdenados(String... ordenacoes) throws MuitosResultadosException {
		List<T> retorno = null;

		TypedQuery<T> q =
				this.getEntityManager().createQuery("SELECT c FROM " + getPersistenceClass().getSimpleName() + " c "
						+ adicionaOrderByHql(ordenacoes), getPersistenceClass());
		q.setMaxResults(MAXRESULTS);
		retorno = q.getResultList();

		if (retorno.size() >= MAXRESULTS) {
			throw new MuitosResultadosException();
		}
		return retorno;
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<T> recuperarTodosSemLimite() {
		List<T> retorno =
				this.getEntityManager().createQuery(
						"SELECT c FROM " + getPersistenceClass().getSimpleName() + " c",
						getPersistenceClass()).getResultList();
		return retorno;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	protected final Class<T> getPersistenceClass() {
		if (persistenceClass == null) {
			this.persistenceClass =
					(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return persistenceClass;
	}

	/**
	 * {@inheritDoc}.
	 */
	protected static final String adicionaOrderByHql(Order... ordenacoes) {
		StringBuilder builder = new StringBuilder();
		if (ordenacoes != null && ordenacoes.length > 0) {
			builder.append(" order by ");
			for (int i = 0; i < ordenacoes.length - 1; i++) {
				builder.append(ordenacoes[i].toString());
				builder.append(", ");
			}
			builder.append(ordenacoes[ordenacoes.length - 1]);
			builder.toString();
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}.
	 */
	protected static final String adicionaOrderByHql(String... ordenacoes) {
		StringBuilder builder = new StringBuilder();
		if (ordenacoes != null && ordenacoes.length > 0) {
			builder.append(" order by ");
			for (int i = 0; i < ordenacoes.length - 1; i++) {
				builder.append(ordenacoes[i]);
				builder.append(", ");
			}
			builder.append(ordenacoes[ordenacoes.length - 1]);
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void adicionaOrdenacao(
		EntityManager entityManager,
		String orderBy,
		CriteriaQuery<T> criteriaQuery,
		Root<T> root) {
		if (orderBy != null && !orderBy.equals("")) {
			if (orderBy.endsWith("desc")) {
				criteriaQuery.orderBy(this.getEntityManager().getCriteriaBuilder().desc(
						root.get(orderBy.substring(0, orderBy.indexOf(" ")))));
			} else {
				criteriaQuery.orderBy(this.getEntityManager().getCriteriaBuilder().asc(
						root.get(orderBy.substring(0, orderBy.indexOf(" ")))));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> listar(T entidadeFiltro, int indiceInicial, int quantidade, String orderBy, Object... filtrosExtras) {

		CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getPersistenceClass());
		Root<T> root = criteriaQuery.from(getPersistenceClass());

		recuperaQuery(entidadeFiltro, criteriaBuilder, criteriaQuery, root, filtrosExtras);

		criteriaQuery = criteriaQuery.select(root);

		adicionaOrdenacao(this.getEntityManager(), orderBy, criteriaQuery, root);

		TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
		typedQuery.setFirstResult(indiceInicial);
		typedQuery.setMaxResults(quantidade);

		return typedQuery.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	public Long contar(T entidade, Object... filtrosExtras) {

		CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(getPersistenceClass());

		recuperaQuery(entidade, criteriaBuilder, criteriaQuery, root, filtrosExtras);

		criteriaQuery.select(criteriaBuilder.count(root));
		TypedQuery<Long> q = this.getEntityManager().createQuery(criteriaQuery);
		return q.getSingleResult();
	}

	/**
	 * Sobrescrever caso a query tenha que recuperar os objetos segundo algum
	 * filtro específico.
	 * 
	 * @param entidadeFiltro
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 */
	protected void recuperaQuery(
		T entidadeFiltro,
		CriteriaBuilder criteriaBuilder,
		CriteriaQuery<?> criteriaQuery,
		Root<T> root, Object... filtrosExtras) {

	}

}
