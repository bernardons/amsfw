package com.unisys.br.amsfw.web.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe responsavel por renderizar o pdf para tela. Bug do primefaces 3.5 faz
 * com que controllers de @ViewScoped nao serializem o StreamedContent. 
 * Servlet foi criada para atender o IE. Para o correto funcionamento, o controller do relatorio
 * deve ser @SessionScoped e ser limpo no menuController.
 * 
 * @author ReisOtaL
 * 
 */
@WebServlet("/relatorio.pdf")
public class PdfReportServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo para retornar o stream do pdf.
	 * O nome do controller deve ser passado por parametro.
	 * Ex: eventoRiscoLinhaNegocioReportController.
	 */
	@SuppressWarnings("rawtypes")
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	GenericReportController genericController =
    			(GenericReportController) request.getSession().getAttribute(getNomeReportController(request));
    	if (genericController != null) {
    		byte[] content = genericController.getPdfByteArray();
    		response.setContentType("application/pdf");
    		response.setContentLength(content.length);
    		response.getOutputStream().write(content);
    	}
    	
    }

	/**
	 * Metodo que recupera o ReportController da requisicao.
	 * Para o funcionamento correto e essencial que o controller siga o padrao.
	 * xxxxxReportController.
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getNomeReportController(HttpServletRequest request) {
		Enumeration enumeration = request.getSession().getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String paramName = (String) enumeration.nextElement();
			if (paramName.contains("ReportController")) {
				return paramName;
			}
		}
		
		return null;
	}

}
