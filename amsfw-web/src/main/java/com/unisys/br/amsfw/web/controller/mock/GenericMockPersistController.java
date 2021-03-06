package com.unisys.br.amsfw.web.controller.mock;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.unisys.br.amsfw.domain.Entidade;

/**
 * Controller genérico de CRUD.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public abstract class GenericMockPersistController<T extends Entidade, C extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<T> persistenceClass = null;

	private Class<C> keyClass = null;

	private T objetoSelecionado;

	private T objeto;

	private C id;

	private boolean readOnly;

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
	 * Construtor executado sempre que a página é carregada. Verifica se existe
	 * o parametro "id", caso exista, irá buscar no banco o objeto com esse
	 * valor.
	 */
	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();

		// Obtem o parametro com nome "id"
		String idObjeto = context.getExternalContext().getRequestParameterMap().get("id");

		// Verifica se o parametro é nulo ou vazio
		if (idObjeto != null && !idObjeto.isEmpty()) {
			C id = getIdObjetoSelecionado(idObjeto);
			objeto = recuperarPorId(id);
		}

		// Obtem o parametro com nome "id"
		String readOnly = context.getExternalContext().getRequestParameterMap().get("readOnly");

		// Verifica se o parametro é nulo ou vazio
		if (readOnly != null && !readOnly.isEmpty()) {
			this.readOnly = Boolean.valueOf(readOnly);
		}
		System.out.println("editavel: " + !getReadOnly());
	}

	/**
	 * Salvar o objeto.
	 * 
	 */
	public String salvar() {

		executaAntesSalvar();

		if (FacesContext.getCurrentInstance().getMessageList() != null
				&& FacesContext.getCurrentInstance().getMessageList().size() > 0) {
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Objeto salvo com sucesso.", null));
		return null;
	}

	protected void executaAntesSalvar() {

	}

	/**
	 * Deleta objeto.
	 * 
	 * @return
	 */
	public String remover() {

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Objeto removido com sucesso.", null));
		this.objeto = null;
		this.readOnly = false;

		return null;
	}

	/**
	 * Recupera o objeto pelo Id.
	 * 
	 * @param id
	 * @return
	 */
	public abstract T recuperarPorId(C id);
	

	/**
	 * Cria um novo objeto.
	 * 
	 * @return
	 */
	public String novo() {

		this.objeto = null;
		this.readOnly = false;

		System.out.println("Novo");

		return null;

	}

	/**
	 * Cria um novo objeto.
	 * 
	 * @return
	 */
	public String editar() {

		this.readOnly = false;
		System.out.println("Editar");

		return null;
	}

	/**
	 * Recupera o objeto para cadastro.
	 * 
	 * @return
	 */
	public T getObjeto() {

		if (objeto == null) {
			try {
				objeto = getPersistenceClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return objeto;
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

	public C getId() {
		return id;
	}

	public void setId(C id) {
		this.id = id;
	}

	public T getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(T objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	public void setObjeto(T objeto) {
		this.objeto = objeto;
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Tela de cadastro padrão do sistema.
	 * 
	 * @return
	 */
	public abstract String getTelaPesquisa();

	/**
	 * Tela de cadastro do sistema.
	 * 
	 * @return
	 */
	public String telaPesquisa() {
		return getTelaPesquisa();
	}

}
