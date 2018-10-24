package com.unisys.br.amsfw.web.faces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;

/**
 * Classe utilitária do Faces.
 * 
 * @author DelfimSM
 * 
 */
public class FacesUtils {

	private FacesUtils() {
	}

	/**
	 * Recupera a bundle key.
	 * 
	 * @param bundleName
	 * @param key
	 * @return
	 */
	public static String getBundleKey(String bundleName, String key) {
		return FacesContext.getCurrentInstance().getApplication()
				.getResourceBundle(FacesContext.getCurrentInstance(), bundleName).getString(key);
	}

	/**
	 * Recupera a mensagem de acordo com uma chave.
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "mensagens");

		return bundle.getString(key);
	}

	/**
	 * Cria uma lista de SelectItem com base em uma lista de entidades.
	 * 
	 * @param entities
	 *            lista de entidades para o qual se deseja a lista
	 * @param description
	 * @param value
	 * @return
	 */
	public static SelectItem[] getSelectItems(List<?> entities, ObjectDescription description) {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (Object x : entities) {
			items.add(new SelectItem(x, description.getDescription(x)));
		}

		return items.toArray(new SelectItem[items.size()]);
	}

	/**
	 * Retorna um arranjo de Longs a partir de uma lista de ids selecionados.
	 * 
	 * @param idsObjetosSelecionados
	 * @return
	 */
	public static Long[] getIdsItemsSelecionados(List<String> idsObjetosSelecionados) {

		Long[] ids = new Long[idsObjetosSelecionados.size()];

		int i = 0;
		for (String idObjetoAtual : idsObjetosSelecionados) {
			ids[i] = Long.parseLong(idObjetoAtual);
			i++;
		}

		return ids;

	}

	/**
	 * Adiciona uma mensagem de erro.
	 * 
	 * @param ex
	 * @param defaultMsg
	 */
	public static void addErrorMessage(Exception ex, String defaultMsg) {
		String msg = ex.getLocalizedMessage();
		if (msg != null && msg.length() > 0) {
			addErrorMessage(msg);
		} else {
			addErrorMessage(defaultMsg);
		}
	}

	/**
	 * Adiciona uma mensagem de erro com uma lista de mensagens.
	 * 
	 * @param messages
	 */
	public static void addErrorMessages(List<String> messages) {
		for (String message : messages) {
			addErrorMessage(message);
		}
	}

	/**
	 * Adiona uma mensagem de erro, o parametro e a chave da mensagem no Bundle.
	 * 
	 * @param chave
	 * */
	public static void addErrorMessageBundle(String chave) {
		addErrorMessage(getMessage(chave));
	}
	
	
	/**
	 * Adicona uma mensagem de erro com somente uma mensagem.
	 * 
	 * @param msg
	 */
	public static void addErrorMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "");
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Adiciona uma mensagem de acordo com a mensagem e os detalhes.
	 * 
	 * @param msg
	 * @param detail
	 */
	public static void addErrorMessage(String msg, String detail) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, detail);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Adiciona uma mensagem de erro retornando uma mensagem.
	 * 
	 * @param key
	 * @return
	 */
	public static FacesMessage addErrorMessageReturningMessage(String key) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, key, "");
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);

		return facesMsg;
	}

	/**
	 * Adiciona uma mensagem de erro retornando uma mensagem com chave e
	 * detalhes.
	 * 
	 * @param key
	 * @param detail
	 * @return
	 */
	public static FacesMessage addErrorMessageReturningMessage(String key, String detail) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, key, detail);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);

		return facesMsg;
	}

	/**
	 * Adiciona uma mensagem de sucesso.
	 * 
	 * @param msg
	 */
	public static void addSuccessMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Adiciona uma mensagem de sucesso com mensagem e detalhes.
	 * 
	 * @param msg
	 * @param detail
	 */
	public static void addSuccessMessage(String msg, String detail) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, detail);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Adiciona uma mensagem de alerta.
	 * 
	 * @param msg
	 */
	public static void addWarnMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, "");
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Adicionar uma mensagem de alerta com mensagem e detalhe.
	 * 
	 * @param msg
	 * @param detail
	 */
	public static void addWarnMessage(String msg, String detail) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, detail);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Recupera o parâmetro de requisição de acordo com a chave.
	 * 
	 * @param key
	 * @return
	 */
	public static String getRequestParameter(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
	}

	/**
	 * Recupera o objeto como parâmetro de requisição.
	 * 
	 * @param requestParameterName
	 * @param converter
	 * @param component
	 * @return
	 */
	public static Object getObjectFromRequestParameter(
		String requestParameterName,
		Converter converter,
		UIComponent component) {
		String theId = FacesUtils.getRequestParameter(requestParameterName);
		return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
	}

	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	/**
	 * Verifica se possui uma mensagem.
	 * 
	 * @return
	 */
	public static boolean hasMessages() {
		return FacesContext.getCurrentInstance().getMessageList().size() > 0;
	}

	/**
	 * Retorna se o usuário logado contém a funcionalidade.
	 * 
	 * @param converter
	 * @return
	 */
	public static boolean getIsUserInRole(String funcionalidade) {
		Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletRequest obj = (HttpServletRequest) request;
		return obj.isUserInRole(funcionalidade);

	}

	/**
	 * Salva o objeto passado como parâmetro na sessão.
	 * 
	 * @param key
	 * @param obj
	 */
	public static void saveObjectInSession(String key, Object obj) {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).setAttribute(key, obj);
	}

	/**
	 * Recupera o objeto passado como parâmetro da sesssão.
	 * 
	 * @param key
	 * @return
	 */
	public static Object getOjectInSession(String key) {
		return ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true))
				.getAttribute(key);
	}

	/**
	 * Remove o objeto com a chave passada como parâmetro da sesssão.
	 * 
	 * @param key
	 * @return
	 */
	public static void removeOjectFromSession(String key) {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).removeAttribute(key);
	}

	/**
	 * Disponibiliza o arquivo excel para download.
	 * 
	 * @param outputStream
	 * @param file
	 * @param mimeType
	 * @throws IOException
	 */
	public static void disponibilizarArquivoXlsParaDownload(ByteArrayOutputStream outputStream) throws IOException {
		HttpServletResponse response =
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		response.setContentType("application/download");
		response.setHeader("Content-Disposition", "inline; filename=\"arquivo.xls\"");
		outputStream.flush();
		outputStream.close();
		response.setContentLength(outputStream.toByteArray().length);
		response.getOutputStream().write(outputStream.toByteArray(), 0, outputStream.toByteArray().length);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Disponibiliza o arquivo para download.
	 * 
	 * @param outputStream
	 * @param file
	 * @param mimeType
	 * @throws IOException
	 */
	public static void disponibilizarArquivoPdfParaDownload(ByteArrayOutputStream outputStream) throws IOException {
		HttpServletResponse response =
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		response.setContentType("application/download");
		response.setHeader("Content-Disposition", "attachment; filename=\"arquivo.pdf\"");
		outputStream.flush();
		outputStream.close();
		response.setContentLength(outputStream.toByteArray().length);
		response.getOutputStream().write(outputStream.toByteArray(), 0, outputStream.toByteArray().length);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}

	/***/
	public static void limparComponentesForm(String formTela) {
		// O padrao e formPesquisa entao null e passado do controller. Caso o
		// form da tela seja diferente deve ser passado o id.
		UIComponent form =
				FacesContext.getCurrentInstance().getViewRoot()
						.findComponent(formTela == null ? "formPesquisa" : formTela);
		if (form != null) {
			limparForm(form);
		}
	}

	/**
	 * Limpa os dados dos componentes de edição e de seus filhos,
	 * recursivamente. Checa se o componente é instância de EditableValueHolder
	 * e 'reseta' suas propriedades.
	 * 
	 */
	private static void limparForm(UIComponent component) {
		if (component instanceof EditableValueHolder) {
			EditableValueHolder evh = (EditableValueHolder) component;
			evh.resetValue();
		}
		// Dependendo de como se implementa um Composite Component, ele retorna
		// ZERO
		// na busca por filhos. Nesse caso devemos iterar sobre os componentes
		// que o
		// compõe de forma diferente.
		if (UIComponent.isCompositeComponent(component)) {
			@SuppressWarnings("rawtypes")
			Iterator i = component.getFacetsAndChildren();
			while (i.hasNext()) {
				UIComponent comp = (UIComponent) i.next();

				if (comp.getChildCount() > 0) {
					for (UIComponent child : comp.getChildren()) {
						limparForm(child);
					}
				}
			}
		}
		if (component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				limparForm(child);
			}
		}
	}

	/**
	 * Encontra o componente na arvore que contem o id passado como parâmetro.
	 */
	public static void reiniciaNumeracaoDataTable(String formTabela, String idDataTable) {
		if (formTabela == null || formTabela.equals("")) {
			formTabela = "formPesquisa";
		}
		if (idDataTable == null || idDataTable.equals("")) {
			idDataTable = "dataTableObjetos";
		}

		UIComponent root = FacesContext.getCurrentInstance().getViewRoot().findComponent(formTabela);
		final String componentId = idDataTable;
		DataTable dataTable = (DataTable) FacesUtils.findComponent(root, componentId);
		if (dataTable != null) {
			dataTable.setFirst(0);
		}
	}

	/**
	 * Encontra o componente na arvore que contem o id passado como parâmetro.
	 */
	public static UIComponent findComponent(UIComponent c, String id) {
		if (c.getId().contains(id)) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

}
