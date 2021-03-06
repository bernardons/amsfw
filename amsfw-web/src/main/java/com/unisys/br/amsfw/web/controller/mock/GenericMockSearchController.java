package com.unisys.br.amsfw.web.controller.mock;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;

import com.unisys.br.amsfw.domain.Entidade;
import com.unisys.br.amsfw.web.datamodel.LazyMockGenericDataModel;

/**
 * Controller genérico de CRUD.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public abstract class GenericMockSearchController<T extends Entidade, C extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<T> persistenceClass = null;

	private Class<C> keyClass = null;

	private LazyMockGenericDataModel<T, C> dataModel;

	private T objetoSelecionado;

	private T objetoFiltro;

	private List<T> listaSelecionados;

	private boolean isSelecionados;

	/**
	 * Sobrescrever este método caso o parâmetro do crud não seja do tipo long.
	 * 
	 * @param parametro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected C getIdObjetoSelecionado(String parametro) {
		System.out.println("Nome classe chave: " + getKeyClass().getName());

		if (getKeyClass().getName().equals("java.lang.Integer")) {
			return (C) Integer.valueOf(parametro);
		}

		return (C) Long.valueOf(parametro);
	}

	/**
	 * Recupera o dataModel de usuários.
	 * 
	 * @return
	 */
	public LazyDataModel<T> getDataModel() {
		if (dataModel == null) {
			dataModel = new LazyMockGenericDataModel<T, C>(getListaObjetos());
		}
		return dataModel;
	}

	/**
	 * Retorna a lista de objetos de exemplo. Deverá retornar uma lista de
	 * objetos mock.
	 * 
	 * @return
	 */
	public abstract List<T> getListaObjetos();

	/**
	 * Pessquisar o usuário.
	 * 
	 * @param actionEvent
	 */
	public String pesquisar() {
		System.out.println("Pesquisar");
		return null;
	}

	/**
	 * Deleta objeto.
	 * 
	 * @return
	 */
	public String remover() {

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro excluído com sucesso.", null));
		return null;
	}

	/**
	 * Deleta todos os objetos da lista de selecionados.
	 */
	public void removerTodosSelecionados() {

		for (T obj : getListaSelecionados()) {
			System.out.println("Removendo objeto id: " + obj.getId());
		}

		getListaSelecionados().clear();
	}

	/**
	 * Remove da lista de objetos selecionados o objeto que foi desmarcado no
	 * dataTable.
	 */
	public void desmarcar() {
		getListaSelecionados().remove(getObjetoSelecionado());
	}

	/**
	 * Adiciona a lista de objetos selecionado o objeto selecionado no
	 * dataTable.
	 * 
	 * @return
	 */
	public String selecionar() {
		getListaSelecionados().remove(getObjetoSelecionado());
		getListaSelecionados().add(getObjetoSelecionado());
		return null;
	}

	/**
	 * Coloca todos os objetos que aparem no data table na lista de selecionados
	 * ou remove todos eles.
	 */
	public void desmarcarMarcarTodos() {

		if (isSelecionados) {
			getListaSelecionados().clear();
			if (this.dataModel != null && this.dataModel.getDatasource() != null) {
				for (T obj : this.dataModel.getDatasource()) {
					getListaSelecionados().add(obj);
				}
			}
		} else {
			getListaSelecionados().clear();
		}
	}

	/**
	 * Recupera o objeto de filtro.
	 * 
	 * @return
	 */
	public T getObjetoFiltro() {
		if (objetoFiltro == null) {
			try {
				objetoFiltro = getPersistenceClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return objetoFiltro;
	}

	public void setObjetoFiltro(T objetoFiltro) {
		this.objetoFiltro = objetoFiltro;
	}

	/**
	 * Método que retorna a classe persistente do tipo T.
	 * 
	 * @return classe do tipo T
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
	 * Método que retorna a classe persistente do tipo T.
	 * 
	 * @return classe do tipo T
	 */
	@SuppressWarnings("unchecked")
	protected final Class<C> getKeyClass() {
		if (keyClass == null) {
			this.keyClass =
					(Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		}
		return keyClass;
	}

	public T getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(T objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	/**
	 * Tela de cadastro padrão do sistema.
	 * 
	 * @return
	 */
	public abstract String getTelaCadastro();

	/**
	 * Tela de cadastro do sistema.
	 * 
	 * @return
	 */
	public String telaCadastro() {
		return getTelaCadastro();
	}

	/**
	 * Recupera a lista de objetos selecionados.
	 * 
	 * @return
	 */
	public List<T> getListaSelecionados() {

		if (listaSelecionados == null) {
			listaSelecionados = new ArrayList<T>();
		}

		return listaSelecionados;
	}

	public void setListaSelecionados(List<T> listaSelecionados) {
		this.listaSelecionados = listaSelecionados;
	}

	public boolean isSelecionados() {
		return isSelecionados;
	}

	public void setSelecionados(boolean isSelecionados) {
		this.isSelecionados = isSelecionados;
	}

}
