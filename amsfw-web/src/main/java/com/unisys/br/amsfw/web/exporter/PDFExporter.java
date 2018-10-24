package com.unisys.br.amsfw.web.exporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Classe gerada para disponibilizar o PDF do datatable, utilizando um jrxml
 * como template. Esta classe cria a tabela e preenche o titulo conforme
 * resultado da tela de pesquisa. Para usar este freature utilizar o componente
 * exporterPDF .
 * 
 * 
 * 
 * @author EgidioRR
 * 
 */
public abstract class PDFExporter {

	private JasperPrint jasperPrint;
	private Integer numeroColunas;
	private int[] columnWidths;
	private int somatorioTabelaTela;
	private Boolean retrato;
	private Integer tamanhoTabelaPdf;
	private String nomeArquivoTemp;

	public PDFExporter(Boolean retrato) {
		this.retrato = retrato;
	}

	/**
	 * Informar o height que cada campo da tabela terá no pdf.
	 * 
	 * @return
	 */
	protected abstract Integer getHeightCampo();

	/**
	 * Informar o height que o campo do cabeçalho da tabela terá no pdf.
	 * 
	 * @return
	 */
	protected abstract Integer getHeightCabecalho();

	/**
	 * Informar o tamanho da fonte do cabeçalho.
	 * 
	 * @return
	 */
	protected abstract Integer getTamanhoFonteCabecalho();

	/**
	 * Informar o tamanho da fonte da tabela.
	 */
	protected abstract Integer getTamanhoFonteRelatorio();

	/**
	 * Informar a fonte da tabela .
	 * 
	 * @return
	 */
	protected abstract String getFonteRelatorio();

	/**
	 * Informar a fonte do cabecalho.
	 * 
	 * @return
	 */
	protected abstract String getFonteCabecalho();

	/**
	 * Informar o arquivo jrxml que servira de template Retrato.
	 * 
	 * @return
	 */
	protected abstract String getJrxmlTemplateRelatorioRetrato();

	/**
	 * Informar o arquivo jrxml que servira de template Retrato.
	 * 
	 * @return
	 */
	protected abstract String getJrxmlTemplateRelatorioPaisagem();

	/**
	 * Informar os parametros a serem usados no relatório.
	 * 
	 * @return
	 */
	protected abstract Map<String, Object> parametrosUtilizadosRelatorio();

	/**
	 * Informar o diretório onde está localizado o template do relatório.
	 * 
	 * @return
	 */
	protected abstract String getDiretorioRelatorioGenerico();

	/**
	 * Informar bordas do cabecalho no formato 0.0,1.0,1.1,0.2 onde ex. 0.0 down
	 * 1.0 left 1.1 up 0.2 right
	 * 
	 * @return
	 */
	@Deprecated
	protected String getBordaCabecalho() {
		return null;
	}

	/**
	 * Informar as bordas da tabela no formato 0.0,1.0,1.1,0.2 onde ex. 0.0 down
	 * 1.0 left 1.1 up 0.2 right
	 * 
	 * Metodo nao esta mais sendo utilizado
	 * 
	 * @return
	 */
	@Deprecated
	protected String getBordaTabela() {
		return null;
	}

	
	/**
	 * Size das bordas da tabela e cabecalho.
	 */
	protected String getBordaSize() {
		return "0.5";
	}

	/**
	 * Metodo utilizado para exportar para PDF a tabela passada por parametro.
	 * 
	 * @param table
	 *            - tabela
	 * @param filename
	 *            - nome do arquivo a ser salvo
	 * @throws IOException
	 */

	/**
	 * @param table
	 * @param filename
	 * @throws IOException
	 */
	public final void export(DataTable table, String filename, String titulo, String colunasTela) throws IOException {
		try {

			File xmlFile = null;
			xmlFile = recuperaXmlTemplate();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlFile);

			Element jasperReportElement = (Element) doc.getFirstChild();
			calcularTamanhoTabelaPDF(jasperReportElement, table, colunasTela);
			Node columnHeader = doc.getElementsByTagName("columnHeader").item(0);

			Element columnHeaderElement = (Element) columnHeader;
			columnHeaderElement.appendChild(criarCabecalho(doc, columnHeaderElement, table));

			Node detail = doc.getElementsByTagName("detail").item(0);

			Element detailElement = (Element) detail;
			detailElement.appendChild(criarTabelaPesquisa(doc, detailElement, table));
			alterarPropriedadesXml(doc);
			gerarJRXML(doc);

			JasperReport jasperReport = JasperCompileManager.compileReport(getArquivoTemporario());

			Map<String, Object> parametros = parametrosUtilizadosRelatorio();
			parametros.put("titulo", titulo);

			List<EntidadeRelatorioGenerico> conteudoTabela = popularListaDataSource(table);

			jasperPrint =
					JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(
							conteudoTabela));
			alterarPropriedadesJasperReport();
			exportarRelatorioParaPDF();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por alterar as propriedades do Jasper Report antes de
	 * ser gerado o PDF.
	 */
	protected void alterarPropriedadesJasperReport() {
	}

	protected void alterarPropriedadesXml(Document doc) {
	}

	private File recuperaXmlTemplate() {
		ServletContext servletContext =
				(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File xmlFile;
		if (retrato) {
			xmlFile =
					new File(servletContext.getRealPath("") + File.separator + getDiretorioRelatorioGenerico()
							+ getJrxmlTemplateRelatorioRetrato());
		} else {
			xmlFile =
					new File(servletContext.getRealPath("") + File.separator + getDiretorioRelatorioGenerico()
							+ getJrxmlTemplateRelatorioPaisagem());
		}
		return xmlFile;
	}

	private void calcularTamanhoTabelaPDF(Element jasperReportElement, DataTable table, String colunasTela) {
		Integer tamanhoPagina = Integer.parseInt(jasperReportElement.getAttribute("pageWidth"));
		Integer margemDireita = Integer.parseInt(jasperReportElement.getAttribute("rightMargin"));
		Integer margemEsquerda = Integer.parseInt(jasperReportElement.getAttribute("leftMargin"));
		tamanhoTabelaPdf = tamanhoPagina - margemDireita - margemEsquerda;
		numeroColunas = getColumnsCount(table);
		columnWidths = recuperarColumnWidths(colunasTela);

	}

	/**
	 * Gera o relatorio em Formato .pdf. e disponibiliza o arquivo para
	 * download.
	 */
	public void exportarRelatorioParaPDF() {
		try {
			ByteArrayOutputStream outputStream = exportToPDF();
			FacesUtils.disponibilizarArquivoPdfParaDownload(outputStream);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}

	protected final ByteArrayOutputStream exportToPDF() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		JRPdfExporter exportaPDF = new JRPdfExporter();
		exportaPDF.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exportaPDF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exportaPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
		exportaPDF.exportReport();
		return output;
	}

	final void gerarJRXML(Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result = new StreamResult(getArquivoTemporario());

		tf.transform(new DOMSource(xml), result);
	}

	private String getArquivoTemporario() {
		if (nomeArquivoTemp == null) {
			nomeArquivoTemp = UUID.randomUUID().toString().concat(".jrxml");

		}
		return System.getProperty("java.io.tmpdir") + File.separator + nomeArquivoTemp;
	}

	public String getNomeArquivoTemp() {
		return nomeArquivoTemp;
	}

	public void setNomeArquivoTemp(String nomeArquivoTemp) {
		this.nomeArquivoTemp = nomeArquivoTemp;
	}

	private int getColumnsCount(DataTable table) {
		int count = 0;

		for (UIComponent child : table.getChildren()) {
			if (child instanceof Column) {
				Column column = (Column) child;

				if (column.isExportable()) {
					count++;
				}
			} else if (child instanceof Columns) {
				Columns columns = (Columns) child;

				if (columns.isExportable()) {
					count += columns.getRowCount();
				}
			}
		}

		return count;
	}

	private Node criarCabecalho(Document doc, Element columnHeaderElement, DataTable table) {
		Element band;
		NodeList list = columnHeaderElement.getChildNodes();
		Node saida = null;
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeName().equals("band")) {
				saida = node;
				break;
			}
		}
		if (saida != null) {
			band = (Element) saida;
		} else {
			band = doc.createElement("band");
		}

		band.setAttribute("height", getHeightCampo().toString());

		adicionarColunas(table, band, doc);

		return band;
	}

	private Node criarTabelaPesquisa(Document doc, Element detailElement, DataTable table) {
		Element band = doc.createElement("band");
		band.setAttribute("height", getHeightCampo().toString());
		Element retangulo = doc.createElement("rectangle");

		Element reportElement = doc.createElement("reportElement");
		reportElement.setAttribute("uuid", "978a02e1-172d-4624-975d-40d22bae9efe");
		reportElement.setAttribute("x", "0");
		reportElement.setAttribute("y", "0");
		reportElement.setAttribute("width", recuperarTotalWidthColunas());
		reportElement.setAttribute("height", getHeightCampo().toString());
		reportElement.setAttribute("stretchType", "RelativeToTallestObject");
		reportElement.setAttribute("isPrintWhenDetailOverflows", "true");
		retangulo.appendChild(reportElement);
		band.appendChild(retangulo);
		criarCamposXML(table, band, doc);
		return band;
	}

	private String recuperarTotalWidthColunas() {
		int sum = 0;
		for (int x : columnWidths) {
			sum += x;
		}
		return String.valueOf(sum);
	}

	private void adicionarColunas(DataTable table, Element band, Document doc) {
		FacesContext context = FacesContext.getCurrentInstance();
		int i = 0;
		int posicaoX = 0;
		for (UIColumn col : table.getColumns()) {
			if (!col.isRendered() && !col.isExportable()) {
				continue;
			}

			if (col instanceof DynamicColumn) {
				((DynamicColumn) col).applyModel();
			}

			if (col.isExportable()) {
				band.appendChild(criarCampoTextoOuField(posicaoX, columnWidths[i],
						exportarValor(context, col.getFacet("header")), doc, true, getBordaSize(),
						retornaAlinhamento(col)));
				posicaoX += columnWidths[i];
				i++;
			}

		}
	}

	protected String retornaAlinhamento(UIColumn col) {
		return col.getStyle();
	}

	private void criarCamposXML(DataTable table, Element band, Document doc) {
		int i = 0;
		int posicaoX = 0;
		for (UIColumn col : table.getColumns()) {
			if (!col.isRendered() && !col.isExportable()) {
				continue;
			}

			if (col instanceof DynamicColumn) {
				((DynamicColumn) col).applyModel();
			}

			if (col.isExportable()) {
				band.appendChild(criarCampoTextoOuField(posicaoX, columnWidths[i], String.valueOf(i), doc, false,
						getBordaSize(), retornaAlinhamento(col)));
				posicaoX += columnWidths[i];
				i++;
			}

		}
	}

	// Muda o tamanho das colunas de acordo com o que esta na tela
	private int[] recuperarColumnWidths(String colunasTela) {
		int[] columnWidths = new int[numeroColunas];
		String value = colunasTela;
		String[] colunas = null;
		if (value.contains(",")) {
			colunas = value.split(",");
		} else {
			colunas = new String[1];
			colunas[0] = value;
		}
		for (int i = 0; i < colunas.length; i++) {
			columnWidths[i] = convertePorcentagem(i, colunas, tamanhoTabelaPdf);
		}
		return columnWidths;
	}

	/**
	 * Regra de três Composta.
	 * 
	 * @param indice
	 * @param colunas
	 * @param referencia
	 * @return
	 */
	private int convertePorcentagem(int indice, String[] colunas, Integer referencia) {
		somatorioTabelaTela = calculaSomatorioTela(colunas);
		double param = Integer.parseInt(colunas[indice]);

		Double x = param / somatorioTabelaTela;
		Double retorno = (x * referencia);

		return retorno.intValue();
	}

	private int calculaSomatorioTela(String[] columnWidths) {
		Integer retorno = 0;
		for (int i = 0; i < columnWidths.length; i++) {
			retorno += Integer.parseInt(columnWidths[i]);
		}
		return retorno;
	}

	private EntidadeRelatorioGenerico exportarConteudo(DataTable table) {
		FacesContext context = FacesContext.getCurrentInstance();
		EntidadeRelatorioGenerico retorno = new EntidadeRelatorioGenerico();
		for (UIColumn col : table.getColumns()) {
			if (!col.isRendered() && !col.isExportable()) {
				continue;
			}

			if (col instanceof DynamicColumn) {
				((DynamicColumn) col).applyModel();
			}

			if (col.isExportable()) {
				retorno.popularProximo(exportarValor(context, recuperarConteudoColuna(col.getChildren(), context)));
			}
		}

		return retorno;
	}

	private UIComponent recuperarConteudoColuna(List<UIComponent> children, FacesContext context) {

		HtmlOutputText componentFinal = new HtmlOutputText();
		StringBuilder texto = new StringBuilder();
		for (UIComponent component : children) {
			if (component.isRendered()) {

				if (component instanceof HtmlOutputText) {

					if (((HtmlOutputText) component).getConverter() != null) {
						texto.append(((HtmlOutputText) component).getConverter().getAsString(context, component,
								((HtmlOutputText) component).getValue()));
					} else {
						texto.append(((HtmlOutputText) component).getValue());
					}

				}

				if (component instanceof HtmlCommandLink) {
					texto.append(((HtmlCommandLink) component).getValue());
				}
			}
		}
		componentFinal.setValue(texto);

		return componentFinal;
	}

	private List<EntidadeRelatorioGenerico> popularListaDataSource(DataTable table) {
		int first = table.getFirst();
		int rowCount = table.getRowCount();
		boolean lazy = table.isLazy();

		List<EntidadeRelatorioGenerico> retorno = new ArrayList<EntidadeRelatorioGenerico>();

		if (lazy) {
			table.setRows(rowCount);
			table.setFirst(0);
			table.loadLazyData();
		}

		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			table.setRowIndex(rowIndex);
			if (table.isRowAvailable()) {
				retorno.add(exportarConteudo(table));
			}
		}
		// restore
		table.setFirst(first);

		return retorno;
	}

	private Node criarCampoTextoOuField(Integer posicaoX, Integer columnWidths, String texto, Document doc,
		Boolean cabecalho, String widhtBorda, String style) {

		Element tipoCampo;
		if (cabecalho) {
			tipoCampo = doc.createElement("staticText");
		} else {
			tipoCampo = doc.createElement("textField");
			tipoCampo.setAttribute("isStretchWithOverflow", "true");
			tipoCampo.setAttribute("isBlankWhenNull", "true");
		}
		Element reportElement = doc.createElement("reportElement");
		reportElement.setAttribute("uuid", "77cf67c3-94fe-4f89-9926-b497197e39e9");
		reportElement.setAttribute("x", posicaoX.toString());
		reportElement.setAttribute("y", "0");
		reportElement.setAttribute("width", columnWidths.toString());
		reportElement.setAttribute("stretchType", "RelativeToTallestObject");
		reportElement.setAttribute("isPrintWhenDetailOverflows", "true");
		tipoCampo.appendChild(reportElement);

		if (widhtBorda != null) {
			criarBorda(doc, widhtBorda, tipoCampo);
		}

		Element textElement = doc.createElement("textElement");
		criarAlinhamento(style, textElement);
		Element font = doc.createElement("font");
		Element paragraph = doc.createElement("paragraph");
		paragraph.setAttribute("leftIndent", "5");
		paragraph.setAttribute("rightIndent", "5");
		textElement.appendChild(font);
		textElement.appendChild(paragraph);
		tipoCampo.appendChild(textElement);

		Element conteudo;
		if (cabecalho) {
			configurarTexto(reportElement, font, getHeightCabecalho().toString(),
					getTamanhoFonteCabecalho().toString(), getFonteCabecalho());
			conteudo = doc.createElement("text");
			conteudo.setTextContent(texto);
		} else {
			configurarTexto(reportElement, font, getHeightCampo().toString(), getTamanhoFonteRelatorio().toString(),
					getFonteRelatorio());
			conteudo = doc.createElement("textFieldExpression");
			conteudo.setTextContent("$F{campo" + texto + "} == null ? null : " + "$F{campo" + texto + "}");
		}
		tipoCampo.appendChild(conteudo);

		return tipoCampo;

	}

	private void configurarTexto(Element reportElement, Element font, String height, String tamanhoFonte, String fonte) {
		reportElement.setAttribute("height", height);
		font.setAttribute("size", tamanhoFonte);
		font.setAttribute("fontName", fonte);
	}

	private void criarAlinhamento(String style, Element textElement) {
		if (style.contains("text-align")) {
			if (style.contains(";")) {
				String[] estilos = style.split(";");
				for (String valor : estilos) {
					if (valor.contains("text-align")) {
						String s = valor.split(":")[1].trim();
						// Primeira Letra Maiscula
						textElement.setAttribute("textAlignment", s.substring(0, 1).toUpperCase() + s.substring(1));
						break;
					}
				}
			} else {
				String s = style.split(":")[1].trim();
				textElement.setAttribute("textAlignment", s.substring(0, 1).toUpperCase() + s.substring(1));
			}
		}
		textElement.setAttribute("verticalAlignment", "Middle");
	}

	private void criarBorda(Document doc, String borda, Element tipoCampo) {
		Element box = doc.createElement("box");
		Element bordaUp = doc.createElement("topPen");

		Element bordaLeft = doc.createElement("leftPen");

		Element bordaRight = doc.createElement("rightPen");

		box.appendChild(bordaUp);
		box.appendChild(bordaLeft);
		box.appendChild(bordaRight);

		bordaLeft.setAttribute("lineWidth", borda);
		bordaUp.setAttribute("lineWidth", borda);
		bordaRight.setAttribute("lineWidth", borda);
		tipoCampo.appendChild(box);
	}

	private String exportarValor(FacesContext context, UIComponent component) {
		if (component instanceof HtmlCommandLink) {
			HtmlCommandLink link = (HtmlCommandLink) component;
			Object value = link.getValue();
			if (value != null) {
				return String.valueOf(value);
			} else {
				for (UIComponent child : link.getChildren()) {
					if (child instanceof ValueHolder) {
						return exportarValor(context, child);
					}
				}
				return "";
			}
		} else if (component instanceof ValueHolder) {
			if (component instanceof EditableValueHolder) {
				Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
				if (submittedValue != null) {
					return submittedValue.toString();
				}
			}
			ValueHolder valueHolder = (ValueHolder) component;
			Object value = valueHolder.getValue();

			if (value == null || value.toString().isEmpty() || value.toString().equalsIgnoreCase("null")) {
				return "";
			}

			return value.toString();
		} else {
			String value = component.toString();
			if (value != null) {
				return value.trim();
			}
			return "";
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		removerArquivo();
	}

	private void removerArquivo() {
		File xmlFile = new File(getArquivoTemporario());
		if (xmlFile.isFile()) {
			xmlFile.delete();
		}

	}

}
