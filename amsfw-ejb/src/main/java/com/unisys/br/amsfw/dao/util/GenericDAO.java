package com.unisys.br.amsfw.dao.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.unisys.br.amsfw.dao.GenericDAOLocal;
import com.unisys.br.amsfw.dao.exception.MuitosResultadosException;
import com.unisys.br.amsfw.domain.DominioDiscreto;
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

	public static final Integer MAXRESULTS_DOMINIODISCRETOS = 15;

	public static final Integer SIZE_BATCH_UPDATE = 600;

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
		this.getEntityManager().remove(getEntityManager().getReference(getPersistenceClass(), id));
	}

	/**
	 * {@inheritDoc}
	 */
	public T salvar(T entidade) {

		T resultado = this.getEntityManager().merge(entidade);
		this.getEntityManager().flush();

		return resultado;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void salvarLista(List<T> lista) {

		int indiceAtual = 0;
		for (T objeto : lista) {
			getEntityManager().merge(objeto);
			if (indiceAtual % SIZE_BATCH_UPDATE == 0) {
				getEntityManager().flush();
				getEntityManager().clear();
			}
			indiceAtual = indiceAtual + 1;
		}

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
	 * Recupera entidade com atributos passados por string preenchidos utilize .
	 * para trazer até o segundo nível de fetch.
	 * 
	 * @param id
	 * @return
	 * @throws NoSuchFieldException
	 */
	public T recuperarComFetch(C id, String... campos) throws NoSuchFieldException {

		CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getPersistenceClass());
		Root<T> root = criteriaQuery.from(getPersistenceClass());
		for (String field : campos) {
			if (field.contains(".")) {
				realizarFetchAninhado(root, field);
			} else {
				Field campo = recuperarCampo(field, getPersistenceClass());
				root.fetch(campo.getName(), JoinType.LEFT);
			}
		}
		String nomeCampoId = getIdField(getPersistenceClass());
		criteriaQuery = criteriaQuery.select(root).where(root.get(nomeCampoId).in(id));
		TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);

		try {
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	/**
	 * Retorna o campo buscando nas classes superiores.
	 * 
	 * @param class1
	 * @throws NoSuchFieldException
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private Field recuperarCampo(String campoParametro, Class class1) throws NoSuchFieldException {
		Field campo = null;

		try {
			campo = getPersistenceClass().getDeclaredField(campoParametro);
			// Se conseguir retorna o campo
			return campo;

		} catch (NoSuchFieldException e) {
			List<Class<?>> hierarquia = recuperarHierarquiaClasses(class1);

			for (Class<?> clazz : hierarquia) {
				try {
					campo = clazz.getDeclaredField(campoParametro);
					// Se conseguir retorna o campo
					return campo;
				} catch (NoSuchFieldException e1) {
					continue;
				}
			}
		}
		throw new NoSuchFieldException();
	}

	/**
	 * Adiciona fetch aos campos aninhados passado no array de strings para 2
	 * níveis.
	 * 
	 * @param root
	 * @param aninhados
	 * @throws NoSuchFieldException
	 */
	private void realizarFetchAninhado(Root<T> root, String campoAninhado) throws NoSuchFieldException {
		String[] aninhados = campoAninhado.split("\\.");
		Field nivel1 = recuperarCampo(aninhados[0], getPersistenceClass());
		Field nivel2 = recuperarCampo(aninhados[1], nivel1.getType());
		root.fetch(nivel1.getName(), JoinType.LEFT).fetch(nivel2.getName(), JoinType.LEFT);

	}

	/**
	 * Retorna o nome do campo anotado com @id.
	 * 
	 * @param tipo
	 * @return
	 */
	private String getIdField(Class<?> tipo) {
		List<Class<?>> hierarquia = recuperarHierarquiaClasses(tipo);
		for (Class<?> clazz : hierarquia) {
			for (Field field : clazz.getDeclaredFields()) {
				if ((field.getAnnotation(Id.class)) != null || (field.getAnnotation(EmbeddedId.class)) != null) {
					return field.getName();
				}
			}

		}
		return null;
	}

	/**
	 * Recupera uma lista com a hierarquia de classes do objeto.
	 * 
	 * @param tipo
	 * @return
	 */
	private List<Class<?>> recuperarHierarquiaClasses(Class<?> tipo) {

		List<Class<?>> hierarquiaDeClasses = new ArrayList<Class<?>>();
		Class<?> classeNaHierarquia = tipo;
		while (classeNaHierarquia != Object.class) {
			hierarquiaDeClasses.add(classeNaHierarquia);
			classeNaHierarquia = classeNaHierarquia.getSuperclass();
		}
		return hierarquiaDeClasses;
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
				this.getEntityManager().createQuery(
						"select c from " + getPersistenceClass().getSimpleName() + " c "
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
				this.getEntityManager().createQuery(
						"SELECT c FROM " + getPersistenceClass().getSimpleName() + " c "
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
				this.getEntityManager()
						.createQuery("SELECT c FROM " + getPersistenceClass().getSimpleName() + " c",
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
	@SuppressWarnings("rawtypes")
	public void adicionaOrdenacao(EntityManager entityManager, String orderBy, CriteriaQuery<T> criteriaQuery,
		Root<T> root) {
		if (orderBy != null && !orderBy.equals("")) {
			String[] prop = orderBy.substring(0, orderBy.indexOf(" ")).replace(".", " ").split(" ");
			Path path = null;
			for (String campo : prop) {
				if (path != null) {
					path.get(campo);
				} else {
					path = root.get(campo);
				}
			}
			if (orderBy.endsWith("desc")) {

				if (prop.length > 1) {
					criteriaQuery.orderBy(this.getEntityManager().getCriteriaBuilder().desc(path));
				} else {
					criteriaQuery.orderBy(this.getEntityManager().getCriteriaBuilder()
							.desc(root.get(orderBy.substring(0, orderBy.indexOf(" ")))));
				}
			} else {
				if (prop.length > 1) {
					criteriaQuery.orderBy(this.getEntityManager().getCriteriaBuilder().asc(path));
				} else {
					criteriaQuery.orderBy(this.getEntityManager().getCriteriaBuilder()
							.asc(root.get(orderBy.substring(0, orderBy.indexOf(" ")))));
				}
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
	public List<T> listarParaTXT(T entidadeFiltro, Object... filtrosExtras) {

		CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getPersistenceClass());
		Root<T> root = criteriaQuery.from(getPersistenceClass());

		recuperaQuery(entidadeFiltro, criteriaBuilder, criteriaQuery, root, filtrosExtras);

		root.getFetches().clear();

		adicionarFetchParaConsultaTXT(root, filtrosExtras);
		
		criteriaQuery.distinct(isListarParaTXTdistinct());

		criteriaQuery = criteriaQuery.select(root);

		adicionaOrdenacao(this.getEntityManager(), null, criteriaQuery, root);

		TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}

	/**
	 * Em determinados casos quando existe orderBy por exemplo com essa propriedade setada como true não funciona.
	 * order by items must appear in the select list if select distinct is specified
	 * 
	 * @return
	 */
	protected boolean isListarParaTXTdistinct() {
		return true;
	}

	/**
	 * 
	 * @param root
	 * @param filtrosExtras
	 */
	protected void adicionarFetchParaConsultaTXT(Root<T> root, Object[] filtrosExtras) {

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
	protected void recuperaQuery(T entidadeFiltro, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery,
		Root<T> root, Object... filtrosExtras) {

	}

	@Override
	public List<? extends DominioDiscreto> recuperarTodosDominiosDiscretos(Class<? extends DominioDiscreto> object) {
		List<? extends DominioDiscreto> retorno = null;

		TypedQuery<? extends DominioDiscreto> q =
				this.getEntityManager().createQuery("select c from " + object.getSimpleName() + " c ", object);
		q.setMaxResults(MAXRESULTS_DOMINIODISCRETOS);
		retorno = q.getResultList();

		return retorno;
	}

}
