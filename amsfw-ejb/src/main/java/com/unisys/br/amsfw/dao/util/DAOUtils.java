package com.unisys.br.amsfw.dao.util;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.unisys.br.amsfw.domain.Entidade;

/**
 * Classe utilitária de acesso a banco de dados.
 * 
 * @author delfimsm
 * 
 */
public class DAOUtils {

	private DAOUtils() {
	}

	/**
	 * Adiciona a ordenaçao de acordo com os parâmetros recebidos.
	 * 
	 * @param entityManager
	 * @param orderBy
	 * @param criteriaQuery
	 * @param root
	 */
	public static void adicionaOrdenacao(
		EntityManager entityManager,
		String orderBy,
		CriteriaQuery<?> criteriaQuery,
		Root<? extends Entidade> root) {
		if (orderBy != null && !orderBy.equals("")) {
			if (orderBy.endsWith("desc")) {
				criteriaQuery.orderBy(entityManager.getCriteriaBuilder().desc(
						root.get(orderBy.substring(0, orderBy.indexOf(" ")))));
			} else {
				criteriaQuery.orderBy(entityManager.getCriteriaBuilder().asc(
						root.get(orderBy.substring(0, orderBy.indexOf(" ")))));
			}
		}
	}

}
