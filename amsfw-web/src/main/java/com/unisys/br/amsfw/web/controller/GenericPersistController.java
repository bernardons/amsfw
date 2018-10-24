package com.unisys.br.amsfw.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import com.unisys.br.amsfw.dao.GenericDAOLocal;
import com.unisys.br.amsfw.domain.Entidade;
import com.unisys.br.amsfw.util.DateUtil;
import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Controller genérico de CRUD.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
public abstract class GenericPersistController<T extends Entidade, C extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<T> persistenceClass = null;

	private Class<C> keyClass = null;

	private T objetoSelecionado;

	private T objeto;

	private C id;

	private boolean readOnly;
	
	private JasperPrint jasperPrint;

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

	protected void inicializaObjetoNovo() {

	}

	/**
	 * Salvar o objeto.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String salvar() {

		executaAntesSalvar();

		if (FacesContext.getCurrentInstance().getMessageList() != null
				&& FacesContext.getCurrentInstance().getMessageList().size() > 0) {
			return null;
		}

		objeto = getDAO().merge(objeto);
		objeto = recuperarPorId((C) objeto.getId());
		FacesUtils.addSuccessMessage(FacesUtils.getMessage("registro.gravado.sucesso"));

		executaPosSalvar();

		return null;
	}

	protected void executaPosSalvar() {

	}

	protected void executaAntesSalvar() {

	}

	/**
	 * Deleta objeto.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String remover() {

		if (objeto != null && objeto.getId() != null) {
			getDAO().removerPorId((C) objeto.getId());
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, FacesUtils.getMessage("registro.excluido.sucesso"), null));
		FacesUtils.limparComponentesForm(null);
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
	protected T recuperarPorId(C id) {
		return (T) getDAO().recuperarPorId(id);
	}

	/**
	 * Recupera com fetch objeto pelo Id.
	 * 
	 * @param id
	 * @return
	 * @throws NoSuchFieldException
	 */
	protected T recuperarPorIdComFetch(C id, String... campos) throws NoSuchFieldException {
		return (T) getDAO().recuperarComFetch(id, campos);
	}

	/**
	 * Cria um novo objeto.
	 * 
	 * @return
	 */
	public String novo() {

		this.objeto = null;
		this.readOnly = false;
		// Caso padrao de form padrao
		FacesUtils.limparComponentesForm(null);
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
	 * Exporta a tela de cadastro.
	 */
	public void exportar() {
		System.out.println("Exportar");
	}
	
	/**
	 * Gera o relatorio de cadastro.
	 * @param lista
	 */
	@SuppressWarnings("rawtypes")
	public void gerarRelatorio(List lista, Map<String, Object> parametros) {
		String diretorio = FacesUtils.getServletContext().getRealPath(new File(getPaginaRelatorio()).getParent());

		try {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

			FacesContext facesContext = FacesContext.getCurrentInstance();
			
			if (parametros == null) {
				parametros = new HashMap<String, Object>();
			}

			parametros.put("SUBREPORT_DIR", diretorio + File.separator);
			parametros.put("TEMPLATE_DIR", diretorio + File.separator + ".." + File.separator + "template");
			parametros.put("logo", diretorio + File.separator + ".." + File.separator + "logo" + File.separator
					+ "caixa_logo.gif");
			parametros.put("rodape_right", DateUtil.getDataHoraAtual());
			parametros.put("rodape_left", FacesUtils.getBundleKey("menu", "sigro.sistema.gestao.risco"));
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

			InputStream relatorio = facesContext.getExternalContext().getResourceAsStream(getPaginaRelatorio());
			jasperPrint =
					dataSource == null ? JasperFillManager.fillReport(relatorio, parametros) : JasperFillManager
							.fillReport(relatorio, parametros, dataSource);

			alterarPropriedadesJasperReport();

			ByteArrayOutputStream output = new ByteArrayOutputStream();

			JRPdfExporter exportaPDF = new JRPdfExporter();
			exportaPDF.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
			exportaPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exportaPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exportaPDF.exportReport();
			
			FacesUtils.disponibilizarArquivoPdfParaDownload(output);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addErrorMessage("Erro ao gerar o relatório." + e.getMessage());
		}
	}
	
	/**
	 * Gera o relatorio de cadastro.
	 * @param lista
	 */
	@SuppressWarnings("rawtypes")
	public void gerarRelatorio(List lista) {
		this.gerarRelatorio(lista, null);
	}
	
	/**
	 * Método responsável por alterar as propriedades do Jasper Report antes de
	 * ser gerado o PDF.
	 */
	protected void alterarPropriedadesJasperReport() {
	}
	
	public String getPaginaRelatorio() {
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

				inicializaObjetoNovo();
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
	 * 
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
	

	/**
	 * Metodo invocado nos botoes NOVO, EDITAR, VOLTAR, EDITAR. Por padrão não realiza função. Podendo ser 
	 * sobrescrito pelo controller.
	 * 
	 * @param e
	 */
	public void actionListener(ActionEvent e) {
	}

}
