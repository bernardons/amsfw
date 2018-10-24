package com.unisys.br.amsfw.web.exporter;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.outputlabel.OutputLabel;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.unisys.br.amsfw.util.DateUtil;
import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Classe gerada para criar o PDF do datatable.
 * 
 * @author FernandF
 * 
 */
public class PDFExporterSigro {

	private Font cellFont;
	private Font facetFont;

	/**
	 * TableHeader.
	 * 
	 * @author FernandF
	 * 
	 */
	private class TableHeader extends PdfPageEventHelper {
		private Image image;
		private Cell cell1;
		private Cell cell3;
		private Cell cell4;
		private Cell cell5;
		private Font font9;
		private Font font15;

		public void onOpenDocument(PdfWriter writer, Document document) {
			try {
				font9 = FontFactory.getFont("Arial", "iso-8859-1", 9f);
				font15 = FontFactory.getFont("Arial", "iso-8859-1", 15f, Font.BOLD);

				ServletContext servletContext =
						(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
				String logo =
						servletContext.getRealPath("") + File.separator + "WEB-INF" + File.separator + "relatorios"
								+ File.separator + "logo" + File.separator + "caixa_logo.gif";
				Image image;
				image = Image.getInstance(logo);
				image.setAlignment(Image.ALIGN_LEFT);
				image.scalePercent(50);
				this.image = image;

				cell1 = new Cell();
				cell1.setRowspan(2);
				cell1.add(this.image);

				cell3 = new Cell();
				Paragraph p2 = new Paragraph("#CONFIDENCIAL 10", font9);
				cell3.add(p2);
				cell3.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);

				UIComponent formulario = FacesContext.getCurrentInstance().getViewRoot().findComponent("formPesquisa");
				UIComponent component = FacesUtils.findComponent(formulario, "tituloReportDataTable");
				if (component != null) {
					OutputLabel label = (OutputLabel) component;
					cell4 = new Cell();
					cell4.setColspan(2);
					Paragraph p3 = new Paragraph(label.getValue().toString().toUpperCase(), font15);
					p3.setAlignment(Paragraph.ALIGN_CENTER);
					cell4.add(p3);
					cell4.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
				}

				cell5 = new Cell();
				cell5.setColspan(2);
				Paragraph p4 = new Paragraph(".");
				cell5.add(p4);
				cell5.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
				cell5.setUseBorderPadding(true);
			} catch (BadElementException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			try {
				Table table = new Table(2, 3);

				table.setBorderWidth(0);
				table.getDefaultCell().setBorder(0);

				Cell cell2 = new Cell();
				Paragraph p1 = new Paragraph(String.format("Página %d", writer.getPageNumber()), font9);
				cell2.add(p1);
				cell2.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);

				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);
				table.addCell(cell5);

				table.setWidth(100);

				document.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
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
	public void export(DataTable table, String filename) throws IOException {
		try {
			FacesContext context = FacesContext.getCurrentInstance();

			Document document = new Document();
			document.setMargins(20, 20, 15, 15);
			String rodape = FacesUtils.getBundleKey("menu", "sigro.sistema.gestao.risco");
			for (int i = 0; i < getSpaces(); i++) {
				rodape += " ";
			}
			rodape += DateUtil.getDataHoraAtual();
			HeaderFooter footer = new HeaderFooter(
					new Phrase(rodape,
					FontFactory.getFont("Arial", "iso-8859-1", 8f)), 
					false);
			footer.setBorder(0);
			footer.setPageNumber(0);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			TableHeader event = new TableHeader();
			document.setFooter(footer);
			writer.setPageEvent(event);

			document.setPageSize(PageSize.A4.rotate());
			if (!document.isOpen()) {
				document.open();
			}

			document.add(exportPDFTable(table));

			document.close();

			writePDFToResponse(context.getExternalContext(), baos, filename);

			context.responseComplete();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PdfPTable exportPDFTable(DataTable table) throws DocumentException {
		String encoding = "iso-8859-1";
		int columnsCount = getColumnsCount(table);
		PdfPTable pdfTable = new PdfPTable(columnsCount);
		pdfTable.setSplitLate(false);
		pdfTable.setHeaderRows(1);
		pdfTable.setWidthPercentage(100f);
		this.cellFont = FontFactory.getFont("Arial", encoding, 8);
		this.facetFont = FontFactory.getFont("Arial", encoding, 10, Font.BOLD);

		// Adiciona o cabecalho do relatorio
		addColumnFacets(table, pdfTable, "header");

		// Exporta os dados do relatorio
		exportAll(table, pdfTable);

		// Adiciona o rodape do relatorio
		if (table.hasFooterColumn()) {
			addColumnFacets(table, pdfTable, "footer");
		}

		// Muda o tamanho das colunas de acordo com o que esta na tela
		UIComponent formulario = FacesContext.getCurrentInstance().getViewRoot().findComponent("formPesquisa");
		UIComponent component = FacesUtils.findComponent(formulario, "tamanhoColunasReportDataTable");
		if (component != null) {
			OutputLabel label = (OutputLabel) component;
			String value = label.getValue().toString();
			String[] colunas = null;
			if (value.contains(",")) {
				colunas = value.split(",");
			} else {
				colunas = new String[1];
				colunas[0] = value;
			}
			float[] columnWidths = new float[columnsCount];
			for (int i = 0; i < columnsCount; i++) {
				columnWidths[i] = Float.parseFloat(colunas[i]);
			}
			pdfTable.setWidths(columnWidths);
		}

		table.setRowIndex(-1);

		return pdfTable;
	}

	private void exportAll(DataTable table, PdfPTable pdfTable) {
		int first = table.getFirst();
		int rowCount = table.getRowCount();
		boolean lazy = table.isLazy();

		if (lazy) {
			table.setRows(rowCount);
			table.setFirst(0);
			table.loadLazyData();
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				exportRow(table, pdfTable, rowIndex);
			}

			// restore
			table.setFirst(first);
			table.loadLazyData();
		} else {
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				exportRow(table, pdfTable, rowIndex);
			}

			// restore
			table.setFirst(first);
		}
	}

	private void exportRow(DataTable table, PdfPTable pdfTable, int rowIndex) {
		table.setRowIndex(rowIndex);

		if (!table.isRowAvailable()) {
			return;
		}

		exportCells(table, pdfTable);
	}

	private void exportCells(DataTable table, PdfPTable pdfTable) {
		for (UIColumn col : table.getColumns()) {
			if (!col.isRendered() && !col.isExportable()) {
				continue;
			}

			if (col instanceof DynamicColumn) {
				((DynamicColumn) col).applyModel();
			}

			if (col.isExportable()) {
				addColumnValue(pdfTable, col.getChildren(), this.cellFont, col.getStyle());
			}
		}
	}

	private void addColumnFacets(DataTable table, PdfPTable pdfTable, String columnType) {
		for (UIColumn col : table.getColumns()) {
			if (!col.isRendered() && !col.isExportable()) {
				continue;
			}

			if (col instanceof DynamicColumn) {
				((DynamicColumn) col).applyModel();
			}

			if (col.isExportable()) {
				addColumnValue(pdfTable, col.getFacet(columnType), this.facetFont);
			}
		}
	}

	private void addColumnValue(PdfPTable pdfTable, UIComponent component, Font font) {
		String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(value, font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		cell.addElement(p);
		cell.setPaddingLeft(0);
		cell.setPaddingRight(0);
		cell.setPaddingTop(0);
		cell.setPaddingBottom(5);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(new Color(213, 213, 213));

		pdfTable.addCell(cell);
	}

	private void addColumnValue(PdfPTable pdfTable, List<UIComponent> components, Font font, String style) {
		StringBuilder builder = new StringBuilder();
		FacesContext context = FacesContext.getCurrentInstance();

		for (UIComponent component : components) {
			if (component.isRendered()) {
				String value = exportValue(context, component);

				if (value != null) {
					builder.append(value);
				}
			}
		}

		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(builder.toString(), font);
		if (style.contains("text-align")) {
			if (style.contains(";")) {
				String[] estilos = style.split(";");
				for (String valor : estilos) {
					if (valor.contains("text-align")) {
						p.setAlignment(valor.split(":")[1].trim());
						break;
					}
				}
			} else {
				p.setAlignment(style.split(":")[1].trim());
			}

		} else {
			p.setAlignment("left");
		}
		cell.addElement(p);
		cell.setPaddingLeft(1.5f);
		cell.setPaddingRight(1.5f);
		cell.setPaddingTop(0);
		cell.setPaddingBottom(5);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		pdfTable.addCell(cell);
	}

	private void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName)
		throws IOException, DocumentException {
		externalContext.setResponseContentType("application/pdf");
		externalContext.setResponseHeader("Expires", "0");
		externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		externalContext.setResponseHeader("Pragma", "public");
		externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
		externalContext.setResponseContentLength(baos.size());
		FacesUtils.addCookieBlockUiNoResponse((HttpServletResponse) externalContext.getResponse());
		OutputStream out = externalContext.getResponseOutputStream();
		baos.writeTo(out);
		externalContext.responseFlushBuffer();
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

	private String exportValue(FacesContext context, UIComponent component) {
		if (component instanceof HtmlCommandLink) {
			HtmlCommandLink link = (HtmlCommandLink) component;
			Object value = link.getValue();
			if (value != null) {
				return String.valueOf(value);
			} else {
				for (UIComponent child : link.getChildren()) {
					if (child instanceof ValueHolder) {
						return exportValue(context, child);
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
			if (value == null) {
				return "";
			}
			if (valueHolder.getConverter() != null) {
				return valueHolder.getConverter().getAsString(context, component, value);
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
	
	private int getSpaces() {
		switch (Calendar.getInstance().get(Calendar.MONTH) + 1) {
		case 1:
			return 210;
		case 2:
			return 206;
		case 3:
			return 211;
		case 4:
		case 5:
			return 215;
		case 6:
		case 7:
			return 212;
		case 8:
		case 9:
		case 10:
			return 208;
		case 11:
		case 12:
			return 206;
		default:
			return 210;
		}
	}
}