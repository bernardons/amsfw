package br.com.unisys.tela;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import br.com.unisys.controller.SystemController;
import br.com.unisys.modelo.TipoGeracaoEnum;

/**
 * Menu do sistema.
 * 
 * @author DelfimSM
 * 
 */
public class Menu {
	
	private Menu() {
	}

	/**
	 * Recupera o menu do sistema.
	 * 
	 * @return
	 */
	public static JMenuBar getMenu() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();

		menu = new JMenu("Geração");
		menu.setMnemonic(KeyEvent.VK_G);
		menuBar.add(menu);

		menuItem = new JMenuItem(TipoGeracaoEnum.CRUD_COMPLETO.getLabel(), KeyEvent.VK_C);
		menuItem.setAction(getAcaoCrudCompleto());
		menuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(menuItem);

		menuItem = new JMenuItem(TipoGeracaoEnum.SOMENTE_PESQUISA.getLabel(), KeyEvent.VK_P);
		menuItem.setAction(getAcaoSomentePesquisa());
		menuItem.setMnemonic(KeyEvent.VK_P);
		menu.add(menuItem);

		menuItem = new JMenuItem(TipoGeracaoEnum.SOMENTE_CADASTRO.getLabel(), KeyEvent.VK_C);
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAction(getAcaoSomenteCadastro());
		menu.add(menuItem);

		menuItem = new JMenuItem(TipoGeracaoEnum.DAO_DAO_LOCAL.getLabel(), KeyEvent.VK_D);
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAction(getAcaoSomenteDAOeDAOLocal());
		menu.add(menuItem);

		menu = new JMenu("Projeto");
		menu.setMnemonic(KeyEvent.VK_N);
		menuBar.add(menu);

		menuItem = new JMenuItem("Configurações", KeyEvent.VK_S);
		menuItem.setMnemonic(KeyEvent.VK_S);
		menu.add(menuItem);

		return menuBar;
	}

	private static AbstractAction getAcaoCrudCompleto() {
		return new AbstractAction(TipoGeracaoEnum.CRUD_COMPLETO.getLabel()) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("Entrou Crud Completo");
				SystemController.getInstance().getTelaSelecaoCamposFiltro().setVisible(false);
				SystemController.limparController();
				SystemController.getInstance().irTelaSelecaoCamposFiltro();
				SystemController.getInstance().getModeloBase().setTipoGeracao(TipoGeracaoEnum.CRUD_COMPLETO);
			}
		};
	}

	private static AbstractAction getAcaoSomentePesquisa() {

		return new AbstractAction(TipoGeracaoEnum.SOMENTE_PESQUISA.getLabel()) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("Entrou Somente Pesquisa");
				SystemController.getInstance().getTelaSelecaoCamposFiltro().setVisible(false);
				
				SystemController.limparController();
				SystemController.getInstance().irTelaSelecaoCamposFiltro();
				SystemController.getInstance().getModeloBase().setTipoGeracao(TipoGeracaoEnum.SOMENTE_PESQUISA);
			}
		};
	}

	private static AbstractAction getAcaoSomenteCadastro() {

		return new AbstractAction(TipoGeracaoEnum.SOMENTE_CADASTRO.getLabel()) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("Entrou Somente Cadastro");
				SystemController.getInstance().getTelaSelecaoCamposFiltro().setVisible(false);
				
				SystemController.limparController();
				SystemController.getInstance().irTelaSelecaoCamposFiltro();
				SystemController.getInstance().getModeloBase().setTipoGeracao(TipoGeracaoEnum.SOMENTE_CADASTRO);
			}
		};
	}
	
	private static AbstractAction getAcaoSomenteDAOeDAOLocal() {

		return new AbstractAction(TipoGeracaoEnum.DAO_DAO_LOCAL.getLabel()) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("Entrou Somente DAO e DAO Local");
				SystemController.getInstance().getTelaSelecaoCamposFiltro().setVisible(false);
				
				SystemController.limparController();
				SystemController.getInstance().irTelaSelecaoCamposFiltro();
				SystemController.getInstance().getModeloBase().setTipoGeracao(TipoGeracaoEnum.DAO_DAO_LOCAL);
			}
		};
	}
}
