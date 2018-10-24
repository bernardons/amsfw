package com.unisys.br.amsfw.web.datamodel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.unisys.br.amsfw.domain.Entidade;

/**
 * Data Model genérico com Mock.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public class LazyMockGenericDataModel<T extends Entidade, C extends Serializable> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	private List<T> datasource;

	/**
	 * Construtor do data model de Usuário.
	 * 
	 * @param usuarioService
	 */
	public LazyMockGenericDataModel(List<T> lista) {
		// Busca o usuário no banco
		setDatasource(lista);

		// Busca a quantidade de usuários no banco.
		long valor = lista.size();
		this.setRowCount((int) valor);

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

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

		System.out.println("\nTHE INPUT PARAMETER VALUE OF LOAD METHOD : \t" + "first=" + first + ", pagesize="
				+ pageSize + ", sortField=" + sortField + ", sortOrder=" + sortOrder + " filter:" + filters);

		return getDatasource();
	}

	public List<T> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<T> datasource) {
		this.datasource = datasource;
	}

}
