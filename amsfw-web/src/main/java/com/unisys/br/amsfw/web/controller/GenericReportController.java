package com.unisys.br.amsfw.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Controller genérico de Report.
 * 
 * @author ReisOtaL
 * 
 * @param <T>
 */
public abstract class GenericReportController<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<T> persistenceClass = null;

	private T objetoFiltro;

	private List<T> listaObjetos;

	private JasperPrint jasperPrint;

	private Boolean hasRegistrosDataSource = false;
	
	private String relatorio;

	public List<T> getListaObjetos() {
		return listaObjetos;
	}

	/**
	 * Retorna a lista de objetos de exemplo. Deverá retornar uma lista de
	 * objetos mock.
	 * 
	 * @return
	 */
	public void setListaObjetos(List<T> listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

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
	 * Tela de cadastro padrão do sistema.
	 * 
	 * @return
	 */
	protected abstract String getPaginaRelatorio();
	
	/**
	 * Chama o metodo que faz a limpeza da arvore de componentes.
	 * Deve ser sobrescrito pelas classes filhas e invocado.
	 * */
	public void limpar() {
		FacesUtils.limparComponentesForm(null);
	}

	/**
	 * Metodo que transforma o relatorio em HTML.
	 * */
	private StringBuffer exportReportToHtmlBuffer(JasperPrint print) throws JRException {

		HttpServletRequest request =
				(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();

		JRHtmlExporter exporter = new JRHtmlExporter();

		StringBuffer sb = new StringBuffer();
		exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRXmlExporterParameter.OUTPUT_STRING_BUFFER, sb);
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/sigro/report/image?image=");
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF8");
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");

		// A linha abaixo é para imprimir as imagens no HTML
		session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print);
		exporter.exportReport();
		return sb;
	}

	private ByteArrayOutputStream exportToXLS() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		JRXlsExporter exportaXLS = new JRXlsExporter();
		exportaXLS.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exportaXLS.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exportaXLS.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
		exportaXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		exportaXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exportaXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exportaXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exportaXLS.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exportaXLS.exportReport();
		return output;
	}

	private ByteArrayOutputStream exportToPDF() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		JRPdfExporter exportaPDF = new JRPdfExporter();
		exportaPDF.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exportaPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exportaPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
		exportaPDF.exportReport();
		return output;
	}

	protected String exportarRelatorioParaHtml() {
		try {
			return exportReportToHtmlBuffer(jasperPrint).toString();
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addErrorMessage("Erro ao gerar o HTML do relatório." + e.getMessage());
			return null;
		}
	}

	/**
	 * Classe a ser invocada do ManagedBean que implementa para gerar o
	 * relatorio. Passando como parametro um Map contendo variaveis caso
	 * necessario.
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void gerarRelatorio(Map parametros, JRBeanCollectionDataSource dataSource) {
		
		String diretorio =  FacesUtils.getServletContext().getRealPath(new File(getPaginaRelatorio()).getParent());
		
		try {			
			
			FacesContext facesContext = FacesContext.getCurrentInstance();

			if (parametros == null) {
				parametros = new HashMap();
			}

			parametros.put("SUBREPORT_DIR", diretorio + File.separator);
			parametros.put("TEMPLATE_DIR", diretorio + File.separator + ".." + File.separator + "template");
			parametros.put("logo", diretorio + File.separator + ".." + File.separator + "logo" + File.separator + "caixa_logo.gif");
			
			InputStream relatorio = facesContext.getExternalContext().getResourceAsStream(getPaginaRelatorio());
			jasperPrint =
					dataSource == null ? JasperFillManager.fillReport(relatorio, parametros) : JasperFillManager
							.fillReport(relatorio, parametros, dataSource);

		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addErrorMessage("Erro ao gerar o relatório." + e.getMessage());
		}

	}

	/**
	 * Gera o relatorio em Formato .xls. e disponibiliza o arquivo para
	 * download.
	 */
	public void exportarRelatorioParaXLS() {
		pesquisar();
		try {
			ByteArrayOutputStream outputStream = exportToXLS();
			FacesUtils.disponibilizarArquivoXlsParaDownload(outputStream);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}

	/**
	 * Gera o relatorio em Formato .xls. e disponibiliza o arquivo para
	 * download.
	 */
	public void exportarRelatorioParaPDF() {
		pesquisar();
		try {
			ByteArrayOutputStream outputStream = exportToPDF();
			FacesUtils.disponibilizarArquivoPdfParaDownload(outputStream);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}

	public Boolean getHasRegistrosDataSource() {
		return hasRegistrosDataSource;
	}

	public void setHasRegistrosDataSource(Boolean hasRegistrosDataSource) {
		this.hasRegistrosDataSource = hasRegistrosDataSource;
	}

	public String getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(String relatorio) {
		this.relatorio = relatorio;
	}
	

}
