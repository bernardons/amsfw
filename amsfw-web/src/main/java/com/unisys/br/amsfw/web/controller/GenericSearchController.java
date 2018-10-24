package com.unisys.br.amsfw.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;

import com.unisys.br.amsfw.dao.GenericDAOLocal;
import com.unisys.br.amsfw.domain.Entidade;
import com.unisys.br.amsfw.web.datamodel.LazyGenericDataModel;
import com.unisys.br.amsfw.web.exporter.PDFExporterSigro;
import com.unisys.br.amsfw.web.exporter.TXTExporterSigro;
import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Controller genérico de CRUD.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public abstract class GenericSearchController<T extends Entidade, C extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<T> persistenceClass = null;

	private Class<C> keyClass = null;

	private LazyGenericDataModel<T, C> dataModel;

	private T objetoSelecionado;

	private T objetoFiltro;

	private List<T> listaSelecionados;

	private boolean isSelecionados;

	protected abstract GenericDAOLocal<T, C> getDAO();

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
	 * Caso seja necessário filtros adicionais além do objeto de pesquisa
	 * devemos sobrescrever este campo.
	 * 
	 * @return
	 */
	protected Object[] getFiltrosAdicionais() {
		return null;
	}

	/**
	 * Recupera o dataModel de usuários.
	 * 
	 * @return
	 */
	public LazyDataModel<T> getDataModel() {
		if (dataModel == null) {
			dataModel = new LazyGenericDataModel<T, C>(getDAO());
			dataModel.setObjetoFiltro(objetoFiltro);
			dataModel.setFiltrosAdicionais(getFiltrosAdicionais());
		}

		return dataModel;
	}

	/**
	 * Pessquisar o usuário.
	 * 
	 * @param actionEvent
	 */
	public String pesquisar() {
		executaAntesPesquisar();

		if (FacesContext.getCurrentInstance().getMessageList() != null
				&& FacesContext.getCurrentInstance().getMessageList().size() > 0) {
			return null;
		}

		// Seta o objeto filtro e os filtros adicionais.
		dataModel.setObjetoFiltro(objetoFiltro);
		dataModel.setFiltrosAdicionais(getFiltrosAdicionais());

		FacesUtils.reiniciaNumeracaoDataTable(null, null);

		System.out.println("Pesquisar");
		return null;
	}

	protected void executaAntesPesquisar() {
	}

	/**
	 * Associado ao botao Limpar da tela.
	 * */
	public void limpar() {
		setObjetoFiltro(null);
		dataModel.setObjetoFiltro(objetoFiltro);
		FacesUtils.limparComponentesForm(null);

	}

	/**
	 * Deleta objeto.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String remover() {
		boolean ocorreuProblema = false;
		boolean problemaTratado = false;

		try {
			getDAO().removerPorId((C) objetoSelecionado.getId());
		} catch (Throwable e) {
			ocorreuProblema = true;
			while (e != null) {
				problemaTratado = trataProblemasRemover(e);
				if (problemaTratado) {
					break;
				}
				e = e.getCause();
			}
		}

		if (!ocorreuProblema) {
			FacesUtils.addSuccessMessageBundle("registro.excluido.sucesso");
		} else if (!problemaTratado) {
			FacesUtils.addErrorMessageBundle("problema.desconhecido.remover.registro");
		}

		return null;
	}

	protected boolean trataProblemasRemover(Throwable e) {
		if (e.getClass().getName() == "org.hibernate.exception.ConstraintViolationException") {
			FacesUtils.addErrorMessageBundle("existem.referencias.entidade");
			return true;
		}
		return false;
	}

	/**
	 * Deleta todos os objetos da lista de selecionados.
	 */
	public void removerTodosSelecionados() {

		for (T obj : getListaSelecionados()) {
			System.out.println("Removendo objeto id: " + obj.getId());
			// getDAO().removerPorId((C) obj.getId());
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
	 * Metodo utilizado para exportar os dados da tabela em PDF.
	 */
	public void exportarPDF() {
		try {
			PDFExporterSigro exporter = new PDFExporterSigro();

			UIComponent formulario = FacesContext.getCurrentInstance().getViewRoot().findComponent("formPesquisa");
			UIComponent component = FacesUtils.findComponent(formulario, "dataTableObjetos");

			DataTable table = (DataTable) component;

			exporter.export(table, "arquivo");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo utilizado para exportar os dados da tabela em PDF.
	 */
	public void exportarTXT() {
		try {
			TXTExporterSigro exporter = new TXTExporterSigro();

			UIComponent formulario = FacesContext.getCurrentInstance().getViewRoot().findComponent("formPesquisa");
			UIComponent component = FacesUtils.findComponent(formulario, "dataTableObjetos");

			DataTable table = (DataTable) component;

			exporter.export(table, "arquivo");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
				inicializarObjetoPesquisa();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return objetoFiltro;
	}

	protected void inicializarObjetoPesquisa() {

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

	/**
	 * Retorna a instancia do dataModel com o tipo especifico do AMSFW ao
	 * contrario do metodo getDataModel que retorna a instancia do LazyDataModel
	 * do Primefaces. Necessario para acesso aos metodos especificos como por
	 * exemplo: setObjetoFiltro.
	 * 
	 * @return
	 */
	public LazyGenericDataModel<T, C> getGenericDataModel() {
		if (dataModel == null) {
			dataModel = new LazyGenericDataModel<T, C>(getDAO());
			dataModel.setObjetoFiltro(objetoFiltro);
			dataModel.setFiltrosAdicionais(getFiltrosAdicionais());
		}

		return dataModel;
	}

}
