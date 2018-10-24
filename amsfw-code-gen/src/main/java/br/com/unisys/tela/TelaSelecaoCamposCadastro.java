package br.com.unisys.tela;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import br.com.unisys.controller.SystemController;
import br.com.unisys.modelo.ModeloBase;

/**
 * Classe que representa a tela de seleção de campos de listagem.
 * 
 * @author DelfimSM
 * 
 */
public class TelaSelecaoCamposCadastro extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel = new JPanel();

	private JList listaMetodos = null;

	private JList getListaMetodos(JPanel panel, int posicaoVertical) {

		SwingUtils.addLabel(panel, "Propriedades Cadastro", posicaoVertical);

		DefaultListModel lista = new DefaultListModel();
		for (String classeAtual : SystemController.getInstance().getModeloBase().getListaMetodos()) {
			lista.addElement(classeAtual);
		}

		if (listaMetodos == null) {
			listaMetodos = new JList(lista);
			listaMetodos.setBounds(180, posicaoVertical, 450, 500);
			listaMetodos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			listaMetodos.setLayoutOrientation(JList.VERTICAL_WRAP);
			listaMetodos.setMaximumSize(new Dimension(450, 500));
			listaMetodos.setVisible(true);
			listaMetodos.setVisibleRowCount(28);

			panel.add(listaMetodos);
		}

		panel.repaint();
		return listaMetodos;
	}

	/**
	 * Inicializa a interface.
	 * 
	 */
	public void inicializaInterface() {

		getContentPane().add(panel);
		panel.setLayout(null);

		SwingUtils.addLabel(panel, "Classe", 40);
		JLabel labelClasse =
				SwingUtils
						.addLabel(panel, SystemController.getInstance().getModeloBase().getClasse().getName(), 40, 180);
		labelClasse.setSize(450, 30);

		getListaMetodos(panel, 80);
		adicionarBotao();
		this.setJMenuBar(Menu.getMenu());

		setTitle("Selecionar Campos de Cadastro");
		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void adicionarBotao() {
		JButton btn = new JButton("Avançar");
		btn.setBounds(170, 590, 100, 30);
		btn.setToolTipText("Avançar");
		btn.addActionListener(new ButtonAvancarListener());
		panel.add(btn);

		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(50, 590, 100, 30);
		btnVoltar.setToolTipText("Voltar");
		btnVoltar.addActionListener(new ButtonVoltarListener());
		panel.add(btnVoltar);
	}

	private class ButtonAvancarListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// Guarda os campos selecionados na lista de campos do cadastro
			List<Field> camposSelecionados = new ArrayList<Field>();
			Object[] metodos = (Object[]) listaMetodos.getSelectedValues();

			for (Object metodoAtual : metodos) {
				camposSelecionados
						.add(SystemController.getInstance().getModeloBase().getMapaMetodos().get(metodoAtual));
			}
			ModeloBase modeloBase = SystemController.getInstance().getModeloBase();
			modeloBase.setCamposCadastro(camposSelecionados);
			SystemController.getInstance().setModeloBase(modeloBase);

			SystemController.getInstance().irTelaGeracao();
		}
	}

	private class ButtonVoltarListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			SystemController.getInstance().irTelaSelecaoCamposListagem();
		}
	}

	@Override
	public JMenuBar getJMenuBar() {
		return Menu.getMenu();
	}

}
