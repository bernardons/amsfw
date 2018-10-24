package br.com.unisys.tela;

import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Utilitários da classe do swing.
 * 
 * @author DelfimSM
 * 
 */
public class SwingUtils {

	private SwingUtils() {
	}

	/**
	 * Adiciona o label na tela.
	 * 
	 * @param panel
	 * @param texto
	 * @param posicaoVertical
	 * @param posicaoHorizontal
	 * @return
	 */
	public static JLabel addLabel(JPanel panel, String texto, int posicaoVertical, int posicaoHorizontal) {
		JLabel label = new JLabel(texto);
		label.setBounds(posicaoHorizontal, posicaoVertical, 140, 30);
		panel.add(label);

		return label;
	}

	/**
	 * Adiciona o label na tela.
	 * 
	 * @param panel
	 * @param texto
	 * @param posicaoVertical
	 * @return
	 */
	public static JLabel addLabel(JPanel panel, String texto, int posicaoVertical) {

		JLabel label = new JLabel(texto);
		label.setFont(new Font("Arial", Font.BOLD, 11));
		label.setBounds(30, posicaoVertical, 140, 30);
		panel.add(label);

		return label;
	}

	/**
	 * Adiciona um campo de texto.
	 * 
	 * @param panel
	 * @param posicaoVertical
	 * @return
	 */
	public static JTextField addTextField(JPanel panel, int posicaoVertical) {
		JTextField text = null;
		text = new JTextField();
		text.setBounds(180, posicaoVertical, 450, 30);

		panel.add(text);
		return text;
	}

	/**
	 * Adiciona o combo box.
	 * 
	 * @param panel
	 * @param opcoesTipoGeracao
	 * @param posicaoSelecionada
	 * @param posicaoVertical
	 * @return
	 */
	public static JComboBox addComboBox(
		JPanel panel,
		String[] opcoesTipoGeracao,
		int posicaoSelecionada,
		int posicaoVertical) {
		JComboBox listaOpcoes = new JComboBox(opcoesTipoGeracao);
		listaOpcoes.setSelectedIndex(posicaoSelecionada);
		listaOpcoes.setBounds(180, posicaoVertical, 450, 30);
		panel.add(listaOpcoes);

		return listaOpcoes;
	}

	/**
	 * Recupera o arranjo a partir de uma coleção.
	 * 
	 * @param opcoesTipoGeracao
	 * @return
	 */
	public static String[] recuperarArranjoDeColecao(List<String> opcoesTipoGeracao) {
		String[] arranjo = new String[opcoesTipoGeracao.size()];

		int i = 0;
		for (String valorAtual : opcoesTipoGeracao) {
			arranjo[i] = valorAtual;
			i = i + 1;
		}

		return arranjo;
	}

	/**
	 * Adiciona um combo box.
	 * 
	 * @param panel
	 * @param opcoesTipoGeracao
	 * @param posicaoSelecionada
	 * @param posicaoVertical
	 * @return
	 */
	public static JComboBox addComboBox(
		JPanel panel,
		List<String> opcoesTipoGeracao,
		int posicaoSelecionada,
		int posicaoVertical) {

		return addComboBox(panel, recuperarArranjoDeColecao(opcoesTipoGeracao), posicaoSelecionada, posicaoVertical);
	}

}
