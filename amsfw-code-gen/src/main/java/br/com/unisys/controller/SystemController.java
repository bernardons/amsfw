package br.com.unisys.controller;

import br.com.unisys.modelo.ModeloBase;
import br.com.unisys.tela.TelaGeracao;
import br.com.unisys.tela.TelaSelecaoCamposCadastro;
import br.com.unisys.tela.TelaSelecaoCamposFiltro;
import br.com.unisys.tela.TelaSelecaoCamposListagem;

/**
 * Controller de sistema.
 * 
 * @author DelfimSM
 * 
 */
public class SystemController {

	private ModeloBase modeloBase;

	private TelaSelecaoCamposFiltro telaSelecaoCamposFiltro;

	private TelaSelecaoCamposListagem telaSelecaoCamposListagem;

	private TelaSelecaoCamposCadastro telaSelecaoCamposCadastro;

	private TelaGeracao telaGeracao;

	private static SystemController instance;

	private SystemController() {
	}

	/**
	 * Limpa todo o controller.
	 */
	public static void limparController() {
		SystemController.getInstance().getTelaSelecaoCamposFiltro().setVisible(false);
		SystemController.getInstance().getTelaSelecaoCamposListagem().setVisible(false);
		SystemController.getInstance().getTelaSelecaoCamposCadastro().setVisible(false);
		SystemController.getInstance().getTelaGeracao().setVisible(false);
		instance = null;
	}

	/**
	 * Recupera uma instancia singleton do SystemController.
	 * 
	 * @return
	 */
	public static SystemController getInstance() {
		if (instance == null) {
			instance = new SystemController();
		}
		return instance;
	}

	/**
	 * Recupera a Tela deseleção de filtros.
	 * 
	 * @return
	 */
	public TelaSelecaoCamposFiltro getTelaSelecaoCamposFiltro() {
		if (telaSelecaoCamposFiltro == null) {
			telaSelecaoCamposFiltro = new TelaSelecaoCamposFiltro();
		}
		return telaSelecaoCamposFiltro;
	}

	public void setTelaSelecaoCamposFiltro(TelaSelecaoCamposFiltro telaSelecaoCamposFiltro) {
		this.telaSelecaoCamposFiltro = telaSelecaoCamposFiltro;
	}

	/**
	 * Recupera a tela de seleção de campos de listagem.
	 * 
	 * @return
	 */
	public TelaSelecaoCamposListagem getTelaSelecaoCamposListagem() {
		if (telaSelecaoCamposListagem == null) {
			telaSelecaoCamposListagem = new TelaSelecaoCamposListagem();
		}
		return telaSelecaoCamposListagem;
	}

	public void setTelaSelecaoCamposListagem(TelaSelecaoCamposListagem telaSelecaoCamposListagem) {
		this.telaSelecaoCamposListagem = telaSelecaoCamposListagem;
	}

	/**
	 * Recupera a tela de campos de cadastro.
	 * 
	 * @return
	 */
	public TelaSelecaoCamposCadastro getTelaSelecaoCamposCadastro() {
		if (telaSelecaoCamposCadastro == null) {
			telaSelecaoCamposCadastro = new TelaSelecaoCamposCadastro();
		}
		return telaSelecaoCamposCadastro;
	}

	public void setTelaSelecaoCamposCadastro(TelaSelecaoCamposCadastro telaSelecaoCamposCadastro) {
		this.telaSelecaoCamposCadastro = telaSelecaoCamposCadastro;
	}

	/**
	 * Recupera a tela de geração.
	 * 
	 * @return
	 */
	public TelaGeracao getTelaGeracao() {
		if (telaGeracao == null) {
			telaGeracao = new TelaGeracao();
		}
		return telaGeracao;
	}

	public void setTelaGeracao(TelaGeracao telaGeracao) {
		this.telaGeracao = telaGeracao;
	}

	/**
	 * Recupera o modelo base.
	 * 
	 * @return
	 */
	public ModeloBase getModeloBase() {
		if (modeloBase == null) {
			modeloBase = new ModeloBase();
		}
		return modeloBase;
	}

	public void setModeloBase(ModeloBase modeloBase) {
		this.modeloBase = modeloBase;
	}

	/**
	 * Vai para a tela de seleção de campos de filtro.
	 */
	public void irTelaSelecaoCamposFiltro() {
		getTelaSelecaoCamposFiltro().setVisible(true);
		getTelaSelecaoCamposListagem().setVisible(false);
		getTelaSelecaoCamposCadastro().setVisible(false);
		getTelaGeracao().setVisible(false);
	}

	/**
	 * Vai para a tela de seleção de campos de listagem.
	 */
	public void irTelaSelecaoCamposListagem() {

		getTelaSelecaoCamposListagem().setVisible(true);
		getTelaSelecaoCamposListagem().inicializaInterface();

		getTelaSelecaoCamposFiltro().setVisible(false);
		getTelaSelecaoCamposCadastro().setVisible(false);
		getTelaGeracao().setVisible(false);
	}

	/**
	 * Vai para a tela de seleção de campos de cadastro.
	 */
	public void irTelaSelecaoCamposCadastro() {
		getTelaSelecaoCamposFiltro().setVisible(false);
		getTelaSelecaoCamposListagem().setVisible(false);

		getTelaSelecaoCamposCadastro().setVisible(true);
		getTelaSelecaoCamposCadastro().inicializaInterface();

		getTelaGeracao().setVisible(false);
	}

	/**
	 * Vai para a tela de geração.
	 */
	public void irTelaGeracao() {
		getTelaSelecaoCamposFiltro().setVisible(false);
		getTelaSelecaoCamposListagem().setVisible(false);
		getTelaSelecaoCamposCadastro().setVisible(false);

		getTelaGeracao().setVisible(true);
		getTelaGeracao().inicializaInterface();

	}
}
