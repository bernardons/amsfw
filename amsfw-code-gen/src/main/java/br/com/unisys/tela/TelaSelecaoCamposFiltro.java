package br.com.unisys.tela;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import br.com.unisys.controller.SystemController;
import br.com.unisys.modelo.ModeloBase;
import br.com.unisys.service.ClasseUtil;

/**
 * Classe que representa a tela inicial.
 * 
 * @author DelfimSM
 * 
 */
public class TelaSelecaoCamposFiltro extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel = new JPanel();

	private String classeSelecionada;

	private JList listaMetodos = null;

	private Map<String, String> mapaClasses = null;

	private List<String> listaClasses = null;

	private JComboBox comboClasses = null;

	/**
	 * Inicializa a seleçào de campos de filtro.
	 */
	public TelaSelecaoCamposFiltro() {
		inicializaInterface();
	}

	private JList getListaMetodos(JPanel panel, int posicaoVertical, String nomeClasse) {

		SwingUtils.addLabel(panel, "Propriedades Pesquisa", posicaoVertical);

		DefaultListModel lista = new DefaultListModel();
		List<String> listaMetodosRecuperados = ClasseUtil.getListaMetodos(nomeClasse);

		for (String classeAtual : listaMetodosRecuperados) {
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
		} else {
			listaMetodos.setListData(SwingUtils.recuperarArranjoDeColecao(listaMetodosRecuperados));
		}

		// Busca a lista e mapa de métodos para ser recuperado em outras telas.
		SystemController.getInstance().getModeloBase().setMapaMetodos(ClasseUtil.getMapaMetodos(nomeClasse));
		SystemController.getInstance().getModeloBase().setListaMetodos(listaMetodosRecuperados);

		panel.repaint();
		return listaMetodos;
	}

	private void inicializaVariaveis() {
		mapaClasses = ClasseUtil.getMapaClasses();

		listaClasses = ClasseUtil.getListaClasses();
	}

	private void inicializaInterface() {

		inicializaVariaveis();

		getContentPane().add(panel);
		panel.setLayout(null);

		SwingUtils.addLabel(panel, "Classes", 40);
		comboClasses = SwingUtils.addComboBox(panel, listaClasses, 0, 40);
		comboClasses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				classeSelecionada = (String) cb.getSelectedItem();
				String nomeCompletoClasse = mapaClasses.get(classeSelecionada);
				getListaMetodos(panel, 80, nomeCompletoClasse);
			}
		});

		JButton btn = new JButton("Avançar");
		btn.setBounds(170, 590, 100, 30);
		btn.setToolTipText("Avançar	");
		btn.addActionListener(new ButtonAvancarListener());

		panel.add(btn);
		this.setJMenuBar(Menu.getMenu());

		setTitle("Seleção de campos de Filtro");
		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class ButtonAvancarListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			preencheCamposModeloBase();
			SystemController.getInstance().irTelaSelecaoCamposListagem();
		}
	}

	private void preencheCamposModeloBase() {

		List<Field> camposSelecionados = new ArrayList<Field>();
		if (listaMetodos != null) {
			Object[] metodos = (Object[]) listaMetodos.getSelectedValues();

			for (Object metodoAtual : metodos) {
				camposSelecionados
						.add(SystemController.getInstance().getModeloBase().getMapaMetodos().get(metodoAtual));
			}
		}

		ModeloBase modeloBase = SystemController.getInstance().getModeloBase();
		try {
			modeloBase.setClasse(Class.forName(mapaClasses.get(classeSelecionada)));
			modeloBase.setEntidade(modeloBase.getClasse().getSimpleName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		modeloBase.setCamposPesquisa(camposSelecionados);

		SystemController.getInstance().setModeloBase(modeloBase);
	}

	@Override
	public JMenuBar getJMenuBar() {
		return Menu.getMenu();
	}

	public String getClasseSelecionada() {
		return classeSelecionada;
	}

	public void setClasseSelecionada(String classeSelecionada) {
		this.classeSelecionada = classeSelecionada;
	}

}
