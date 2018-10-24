package com.unisys.br.amsfw.util;

/**
 * Classe utilitaria para Cnpj.
 * 
 * @author ReisOtaL
 * 
 */
public class CnpjUtil {

	private static final int SETE = 7;
	private static final int OITO = 8;
	private static final int SEIS = 6;
	private static final int NOVE = 9;
	private static final int QUATRO = 4;
	private static final int CINCO = 5;
	private static final int DEZ = 10;
	private static final int DOZE = 12;
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
		String cnpjCalc = cnpj.substring(0, DOZE);

		char[] charCnpj = cnpj.toCharArray();
		for (int i = 0; i < QUATRO; i++) {
			if (charCnpj[i] - QUARENTA_OITO >= 0 && charCnpj[i] - QUARENTA_OITO <= NOVE) {
				soma += (charCnpj[i] - QUARENTA_OITO) * (SEIS - (i + 1));
			}
		}

		for (int i = 0; i < OITO; i++) {
			if (charCnpj[i + QUATRO] - QUARENTA_OITO >= 0 && charCnpj[i + QUATRO] - QUARENTA_OITO <= NOVE) {
				soma += (charCnpj[i + QUATRO] - QUARENTA_OITO) * (DEZ - (i + 1));
			}
		}
		int dig = ONZE - soma % ONZE;
		cnpjCalc =
				(new StringBuilder(String.valueOf(cnpjCalc))).append(
						dig != DEZ && dig != ONZE ? Integer.toString(dig) : "0").toString();
		soma = 0;
		for (int i = 0; i < CINCO; i++) {
			if (charCnpj[i] - QUARENTA_OITO >= 0 && charCnpj[i] - QUARENTA_OITO <= NOVE) {
				soma += (charCnpj[i] - QUARENTA_OITO) * (SETE - (i + 1));
			}
		}

		for (int i = 0; i < OITO; i++) {
			if (charCnpj[i + CINCO] - QUARENTA_OITO >= 0 && charCnpj[i + CINCO] - QUARENTA_OITO <= NOVE) {
				soma += (charCnpj[i + CINCO] - QUARENTA_OITO) * (DEZ - (i + 1));
			}
		}

		dig = ONZE - soma % ONZE;
		cnpjCalc =
				(new StringBuilder(String.valueOf(cnpjCalc))).append(
						dig != DEZ && dig != ONZE ? Integer.toString(dig) : "0").toString();

		if (!cnpj.equals(cnpjCalc)) {
			return false;
		}
		return true;
	}

	private static boolean validaTamanhoNumero(String cnpj) {
		if (cnpj == null || cnpj.length() != TAMANHO_CNPJ) {
			return false;
		}

		try {
			Long numero = Long.parseLong(cnpj);
			//Cnpj 0000 não  é válido.
			if (numero == 0) {
				return false;
			}
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			return false;
		}
		return true;
	}

}
