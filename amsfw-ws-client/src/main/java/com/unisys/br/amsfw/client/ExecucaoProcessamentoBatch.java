package com.unisys.br.amsfw.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import com.unisys.br.amsfw.ws.processamento.ProcessamentoWSService;

/**
 * Classe que realiza o processamento da execução de processamento.
 * 
 * @author DelfimSM
 * 
 */
public class ExecucaoProcessamentoBatch {

	private ExecucaoProcessamentoBatch() {
	}

	private static URL getServiceURL(String urlWsdl) {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = ProcessamentoWSService.class.getResource(".");
			url = new URL(baseUrl, urlWsdl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * Método que invoca o web service.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String url = args[0];
		Integer parametro = Integer.valueOf(args[1]);
		if (args.length < 2) {
			System.out.println("Como parâmetro para a execução deve ser passado o "
					+ "endereço do WebService e o número do processamento");
		}

		new ProcessamentoWSService(getServiceURL(url), new QName(
				"http://processamento.ws.amsfw.br.unisys.com/",
				"ProcessamentoWSService")).getProcessamentoWSPort().executarProcessamento(parametro);

		System.out.println("Acabou a chamada...");
	}

}
