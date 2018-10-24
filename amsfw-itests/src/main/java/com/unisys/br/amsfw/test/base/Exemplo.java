package com.unisys.br.amsfw.test.base;

import org.junit.Test;

/**
 * 
 * @author RegoIanC
 * 
 */
public class Exemplo extends BaseSelenium {
	/**
 * 
 */
	public Exemplo() {
		super("http://localhost:8080/sigro/pages/transferencia/pesquisarTransferencia.xhtml");
	}

	@Override
	protected void fluxoBasico() {
	}

	@Test
	@Override
	public void testCenarioInserir() throws Exception {
	}

	@Override
	public void testCenarioPesquisar() throws Exception {
	}

	@Override
	public void testCenarioEditar() throws Exception {
	}

	@Override
	protected void urlInicioTeste() {
	}

}
