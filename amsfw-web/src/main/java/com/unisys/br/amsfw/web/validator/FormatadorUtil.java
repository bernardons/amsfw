package com.unisys.br.amsfw.web.validator;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Classe responsável por ter os métodos responsáeis pela formatação das
 * Strings.
 * 
 * @author samuelmd
 * 
 */
public final class FormatadorUtil {

	/**
	 * Construtor privado da classe.
	 */
	private FormatadorUtil() {

	}

	/**
	 * 
	 * 
	 * @param texto
	 * @param mascara
	 * @return
	 */
	public static String aplicarMascara(String texto, String mascara) {

		String result = "";

		String[] caracs = new String[]{"[", ".", "/", "-", ":", "(", ")", ",", "]"};
		String[] mascaras = new String[mascara.length()];

		for (int i = 0; i < mascara.length(); i++) {

			for (String carac : caracs) {
				if (mascara.charAt(i) == carac.charAt(0)) {
					mascaras[i] = "" + mascara.charAt(i);
					break;
				}
			}
		}

		int numeroVezesAlterado = 0;

		for (int i = 0; i < mascaras.length; i++) {

			if ((mascaras[i] != null) && !StringUtils.isEmpty(mascaras[i])) {
				result = result.concat(mascaras[i]);
				numeroVezesAlterado++;
			} else {
				if ((i - numeroVezesAlterado) < texto.length()) {
					result = result.concat("" + texto.charAt(i - numeroVezesAlterado));
				}
			}

		}

		return result;
	}

	/**
	 * Método responsável por retirar a máscara do campo.
	 * 
	 * @param texto
	 *            a ser retirado a mascara
	 * @return String sem a mascara
	 */
	public static String retirarMascara(String texto) {

		String[] caracs = new String[]{"[", ".", "/", "-", ":", "(", ")", ",", "]", "_"};

		for (String carac : caracs) {
			texto = StringUtils.replaceChars(texto, carac, "");
		}

		return texto;
	}

	/**
	 * Formata um Number para um formato monetário.
	 */
	public static String formatarMoeda(Number valor) {

		Locale l = new Locale("pt", "BR");
		NumberFormat formatador = NumberFormat.getNumberInstance(l);
		formatador.setMinimumFractionDigits(2);
		formatador.setGroupingUsed(true);
		return formatador.format(valor);
	}
}