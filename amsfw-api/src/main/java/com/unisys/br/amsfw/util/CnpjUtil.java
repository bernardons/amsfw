package com.unisys.br.amsfw.util;


/**
 * Classe utilitaria para Cnpj.
 * 
 * @author ReisOtaL
 * 
 */
public class CnpjUtil {

	private static final int QUARENTA_OITO = 48;
	private static final int ONZE = 11;
	private static final int TAMANHO_CNPJ = 14;

	private CnpjUtil() {
	}

	/**
	 * Valida CNPJ passado retornando true ou false.
	 * 
	 * @param cnpj
	 *            String valor com 14 dígitos
	 */
	public static boolean validarCNPJ(String cnpj) {

		if (cnpj == null || cnpj.trim().isEmpty()) {
			return false;
		}

		cnpj = cnpj.replace(".", "");
		cnpj = cnpj.replace("-", "");
		cnpj = cnpj.replace("/", "");

		if (!validaTamanhoNumero(cnpj)) {
			return false;
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char[] chr_cnpj = cnpj.toCharArray();
		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - QUARENTA_OITO >= 0 && chr_cnpj[i] - QUARENTA_OITO <= 9) {
				soma += (chr_cnpj[i] - QUARENTA_OITO) * (6 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 4] - QUARENTA_OITO >= 0 && chr_cnpj[i + 4] - QUARENTA_OITO <= 9) {
				soma += (chr_cnpj[i + 4] - QUARENTA_OITO) * (10 - (i + 1));
			}
		}
		int dig = ONZE - soma % ONZE;
		cnpj_calc =
				(new StringBuilder(String.valueOf(cnpj_calc))).append(
						dig != 10 && dig != ONZE ? Integer.toString(dig) : "0").toString();
		soma = 0;
		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - QUARENTA_OITO >= 0 && chr_cnpj[i] - QUARENTA_OITO <= 9) {
				soma += (chr_cnpj[i] - QUARENTA_OITO) * (7 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - QUARENTA_OITO >= 0 && chr_cnpj[i + 5] - QUARENTA_OITO <= 9) {
				soma += (chr_cnpj[i + 5] - QUARENTA_OITO) * (10 - (i + 1));
			}
		}

		dig = ONZE - soma % ONZE;
		cnpj_calc =
				(new StringBuilder(String.valueOf(cnpj_calc))).append(
						dig != 10 && dig != ONZE ? Integer.toString(dig) : "0").toString();

		if (!cnpj.equals(cnpj_calc)) {
			return false;
		}
		return true;
	}

	private static boolean validaTamanhoNumero(String cnpj) {
		if (cnpj == null || cnpj.length() != TAMANHO_CNPJ) {
			return false;
		}

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			return false;
		}
		return true;
	}

}
