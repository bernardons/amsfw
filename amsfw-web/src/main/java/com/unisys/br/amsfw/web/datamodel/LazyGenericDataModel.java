package com.unisys.br.amsfw.web.datamodel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.unisys.br.amsfw.dao.GenericDAOLocal;
import com.unisys.br.amsfw.domain.Entidade;

/**
 * Data Model genérico.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public class LazyGenericDataModel<T extends Entidade, C extends Serializable> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	private List<T> datasource;

	private GenericDAOLocal<T, C> genericDao;

	private T objetoFiltro;

	private Object[] filtrosAdicionais;

	/**
	 * Construtor do data model de Usuário.
	 * 
	 * @param usuarioService
	 */
	public LazyGenericDataModel(GenericDAOLocal<T, C> genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public T getRowData(String rowKey) {
		for (T entidade : getDatasource()) {
			if (((Entidade) entidade).getId().equals(rowKey)) {
				return entidade;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(T entidade) {
		return ((Entidade) entidade).getId();
	}

	/**
	 * Sobrescrito para funcionar corretamente a paginação.
	 * 
	 */
	@Override
	public void setRowIndex(int rowIndex) {
		/*
		 * The following is in ancestor (LazyDataModel): this.rowIndex =
		 * rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
		 */
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else {
			super.setRowIndex(rowIndex % getPageSize());
		}
	}

	private String getOrder(String sortField, SortOrder sortOrder) {

		if (StringUtils.isEmpty(sortField)) {
			return null;
		}
		if (sortOrder.compareTo(SortOrder.ASCENDING) == 0) {
			return sortField + " asc";
		}
		return sortField + " desc";

	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

		System.out.println("\nTHE INPUT PARAMETER VALUE OF LOAD METHOD : \t" + "first=" + first + ", pagesize="
				+ pageSize + ", sortField=" + sortField + ", sortOrder=" + sortOrder + " filter:" + filters);

		// Busca o usuário no banco
		setDatasource(genericDao.listar(
				objetoFiltro,
				first,
				pageSize,
				getOrder(sortField, sortOrder),
				filtrosAdicionais));

		// Busca a quantidade de usuários no banco.
		long valor = genericDao.contar(objetoFiltro, filtrosAdicionais);
		this.setRowCount((int) valor);

		return getDatasource();
	}

	/**
	 * Seta o objeto filtro. Clona o objeto para evitar efeitos colaterais.
	 * 
	 * @param objetoFiltro
	 */
	@SuppressWarnings("unchecked")
	public void setObjetoFiltro(T objetoFiltro) {
		if (objetoFiltro != null) {
			this.objetoFiltro = (T) SerializationUtils.clone(objetoFiltro);
		} else {
			this.objetoFiltro = null;
		}
	}

	public List<T> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<T> datasource) {
		this.datasource = datasource;
	}

	public Object[] getFiltrosAdicionais() {
		return filtrosAdicionais;
	}

	public void setFiltrosAdicionais(Object[] filtrosAdicionais) {
		this.filtrosAdicionais = filtrosAdicionais;
	}

}
