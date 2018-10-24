package br.com.unisys.tela;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.unisys.controller.SystemController;
import br.com.unisys.modelo.ModeloBase;
import br.com.unisys.modelo.TipoGeracaoEnum;
import br.com.unisys.util.SystemProperties;
import br.com.unisys.velocity.VelocityTemplate;

/**
 * Classe que representa a tela inicial.
 * 
 * @author DelfimSM
 * 
 */
public class TelaGeracao extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel = new JPanel();

	private JTextField pacoteDominio;

	private JTextField paginaPesquisa;

	private JTextField paginaCadastro;

	private JTextField autor;

	private JTextField funcionalidade;

	private JTextField grupoBreadcrumb;

	private JTextField tipoChave;

	/**
	 * Inicializa a interface.
	 */
	public void inicializaInterface() {

		getContentPane().add(panel);
		panel.setLayout(null);

		
		SwingUtils.addLabel(panel, "Tipo", 5);
		JLabel labelTipoGeracao =
				SwingUtils.addLabel(
						panel,
						SystemController.getInstance().getModeloBase().getTipoGeracao().getLabel(),
						5,
						180);
		labelTipoGeracao.setSize(450, 30);

		
		SwingUtils.addLabel(panel, "Classe", 40);
		JLabel labelClasse =
				SwingUtils.addLabel(
						panel,
						SystemController.getInstance().getModeloBase().getClasse().getName(),
						40,
						180);
		labelClasse.setSize(450, 30);

		SwingUtils.addLabel(panel, "Pacote de Dominio", 80);
		pacoteDominio = SwingUtils.addTextField(panel, 80);
		pacoteDominio.setText(SystemController.getInstance().getModeloBase().getStringDefaultPacoteDominio());

		SwingUtils.addLabel(panel, "Página de Pesquisa", 120);
		paginaPesquisa = SwingUtils.addTextField(panel, 120);
		paginaPesquisa.setText(SystemController.getInstance().getModeloBase().getStringDefaultPaginaPesquisa());

		SwingUtils.addLabel(panel, "Página de Cadastro", 160);
		paginaCadastro = SwingUtils.addTextField(panel, 160);
		paginaCadastro.setText(SystemController.getInstance().getModeloBase().getStringDefaultPaginaCadastro());

		SwingUtils.addLabel(panel, "Autor", 200);
		autor = SwingUtils.addTextField(panel, 200);

		SwingUtils.addLabel(panel, "Funcionalidade", 240);
		funcionalidade = SwingUtils.addTextField(panel, 240);
		funcionalidade.setText(SystemController.getInstance().getModeloBase().getStringDefaultFuncionalidade());

		SwingUtils.addLabel(panel, "Grupo Breadcrumb", 280);
		grupoBreadcrumb = SwingUtils.addTextField(panel, 280);

		SwingUtils.addLabel(panel, "Tipo Chave", 320);
		tipoChave = SwingUtils.addTextField(panel, 320);
		tipoChave.setText(SystemController.getInstance().getModeloBase().getStringDefaultTipoChave());

		adicionarBotao();

		this.setJMenuBar(Menu.getMenu());

		setTitle("Gerador de CRUDs");
		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void adicionarBotao() {
		JButton btn = new JButton("Gerar");
		btn.setBounds(170, 590, 100, 30);
		btn.setToolTipText("Gerar CRUD");
		btn.addActionListener(new ButtonListener());
		panel.add(btn);

		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(50, 590, 100, 30);
		btnVoltar.setToolTipText("Voltar");
		btnVoltar.addActionListener(new ButtonVoltarListener());
		panel.add(btnVoltar);
	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			ModeloBase modeloBase = SystemController.getInstance().getModeloBase();
			modeloBase.setPacoteDominio(pacoteDominio.getText());
			modeloBase.setEntidade(modeloBase.getClasse().getSimpleName());
			modeloBase.setPaginaPesquisa(paginaPesquisa.getText());
			modeloBase.setPaginaCadastro(paginaCadastro.getText());
			modeloBase.setAutor(autor.getText());
			modeloBase.setFuncionalidade(funcionalidade.getText());
			modeloBase.setGrupoBreadcrumb(grupoBreadcrumb.getText());
			modeloBase.setTipoChave(tipoChave.getText());
			
			modeloBase.setPacoteProjeto(
					SystemProperties.getPropriedade(SystemProperties.PACOTE_PROJETO));
			
			SystemController.getInstance().setModeloBase(modeloBase);

			System.out.println("Entrou...");
			System.out.println(modeloBase.getAutor());
			System.out.println(modeloBase.getEntidade());
			System.out.println(modeloBase.getClasse());
			System.out.println(modeloBase.getCamposPesquisa().size());
			System.out.println(modeloBase.getCamposListaPesquisa().size());
			System.out.println(modeloBase.getCamposCadastro().size());
			System.out.println(modeloBase.getFuncionalidade());
			System.out.println(modeloBase.getGrupoBreadcrumb());
			System.out.println(modeloBase.getPacoteDominio());
			System.out.println(modeloBase.getPaginaPesquisa());
			System.out.println(modeloBase.getTipoChave());

			if (modeloBase.getTipoGeracao() == null
					|| modeloBase.getTipoGeracao().equals(TipoGeracaoEnum.CRUD_COMPLETO)) {
				VelocityTemplate.gravarCrudCompleto(modeloBase);
			} else if (modeloBase.getTipoGeracao().equals(TipoGeracaoEnum.SOMENTE_PESQUISA)) {
				VelocityTemplate.gravarSomentePesquisa(modeloBase);
			} else if (modeloBase.getTipoGeracao().equals(TipoGeracaoEnum.SOMENTE_CADASTRO)) {
				VelocityTemplate.gravarSomenteCadastro(modeloBase);
			} else if (modeloBase.getTipoGeracao().equals(TipoGeracaoEnum.DAO_DAO_LOCAL)) {
				VelocityTemplate.gravarDAOeDAOLocal(modeloBase);
			}

			JLabel label = SwingUtils.addLabel(panel, "Geração executada com sucesso.", 380);
			label.setSize(400, 30);
			panel.repaint();

		}
	}

	private class ButtonVoltarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			SystemController.getInstance().irTelaSelecaoCamposCadastro();
		}
	}

	@Override
	public JMenuBar getJMenuBar() {
		return Menu.getMenu();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

}
