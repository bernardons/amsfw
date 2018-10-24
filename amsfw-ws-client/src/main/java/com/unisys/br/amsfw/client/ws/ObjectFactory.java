package com.unisys.br.amsfw.client.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.unisys.br.amsfw.ws.processamento
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private static final QName EXECUTARPROCESSAMENTO_QNAME = new QName(
			"http://processamento.ws.amsfw.br.unisys.com/",
			"executarProcessamento");
	private static final QName EXECUTARPROCESSAMENTORESPONSE_QNAME = new QName(
			"http://processamento.ws.amsfw.br.unisys.com/",
			"executarProcessamentoResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.unisys.br.amsfw.ws.processamento.
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ExecutarProcessamentoResponse }.
	 * 
	 */
	public ExecutarProcessamentoResponse createExecutarProcessamentoResponse() {
		return new ExecutarProcessamentoResponse();
	}

	/**
	 * Create an instance of {@link ExecutarProcessamento }.
	 * 
	 */
	public ExecutarProcessamento createExecutarProcessamento() {
		return new ExecutarProcessamento();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ExecutarProcessamento }{@code >}.
	 * 
	 */
	@XmlElementDecl(namespace = "http://processamento.ws.amsfw.br.unisys.com/", name = "executarProcessamento")
	public JAXBElement<ExecutarProcessamento> createExecutarProcessamento(ExecutarProcessamento value) {
		return new JAXBElement<ExecutarProcessamento>(
				EXECUTARPROCESSAMENTO_QNAME,
				ExecutarProcessamento.class,
				null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ExecutarProcessamentoResponse }{@code >}.
	 * 
	 */
	@XmlElementDecl(namespace = "http://processamento.ws.amsfw.br.unisys.com/", name = "executarProcessamentoResponse")
	public JAXBElement<ExecutarProcessamentoResponse> createExecutarProcessamentoResponse(
		ExecutarProcessamentoResponse value) {
		return new JAXBElement<ExecutarProcessamentoResponse>(
				EXECUTARPROCESSAMENTORESPONSE_QNAME,
				ExecutarProcessamentoResponse.class,
				null,
				value);
	}

}
