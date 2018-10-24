package com.unisys.br.amsfw.web.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.util.Constants;

import com.lowagie.text.DocumentException;

/**
 * Classe gerada para criar o TXT do datatable.
 * 
 * @author FernandF
 * 
 */
public class TXTExporterSigro {

	private StringBuilder export;

	/**
	 * Metodo utilizado para exportar para TXT a tabela passada por parametro.
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
			int first = table.getFirst();
			int rowCount = table.getRowCount();

			table.setRows(rowCount);
			table.setFirst(0);
			table.loadLazyData();
			String separador = table.getCellSeparator();
			export = new StringBuilder();
			export.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
			export.append("\r\n");
			int count = 0;
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				exportRow(table, rowIndex, separador);
				export.deleteCharAt(export.length() - 1);
				export.deleteCharAt(export.length() - 1);
				export.append("\r\n");
				count++;
			}
			export.append(count);

			table.setFirst(first);
			table.loadLazyData();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			baos.write(export.toString().getBytes());

			writeTXTToResponse(context.getExternalContext(), baos, filename);

			context.responseComplete();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exportRow(DataTable table, int rowIndex, String separador) {
		table.setRowIndex(rowIndex);
		if (!table.isRowAvailable()) {
			return;
		}
		exportCells(table, separador);
	}

	private void exportCells(DataTable table, String separador) {
		for (UIColumn col : table.getColumns()) {
			if (!col.isRendered()) {
				continue;
			}

			if (col instanceof DynamicColumn) {
				((DynamicColumn) col).applyModel();
			}

			if (col.isExportable()) {
				addColumnValue(col.getChildren(), separador);
			}
		}
	}

	private void addColumnValue(List<UIComponent> components, String separador) {
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
		export.append(builder.toString() + separador);
	}

	private void writeTXTToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName)
		throws IOException, DocumentException {
		externalContext.setResponseContentType("application/txt");
		externalContext.setResponseHeader("Expires", "0");
		externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		externalContext.setResponseHeader("Pragma", "public");
		externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".txt");
		externalContext.setResponseContentLength(baos.size());
		externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
		OutputStream out = externalContext.getResponseOutputStream();
		baos.writeTo(out);
		externalContext.responseFlushBuffer();
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
}