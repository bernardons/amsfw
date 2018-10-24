package com.unisys.br.amsfw.web.validator;

import org.apache.commons.lang.StringUtils;

/**
 * Classe Utilitaria para CPF.
 * 
 * @author DelfimSM
 */
public class CpfUtil {

	private static final int CPF_LEN = 11;
	private static final int CPF_9 = 9;
	private static final int CPF_10 = 10;

	private static final String CPF_MASK = "999.999.999-99";
	private static final int NUMBER_OF_DIGITS_CPF = 11;

	/**
	 * Construtor padrÃ£o.
	 */
	private CpfUtil() {

	}

	/**
	 * 
	 * Este método Ã© responsÃ¡vel por calcular regra de cpf.
	 * 
	 * @param num
	 * @return
	 */
	private static String calcDigVerif(String num) {

		Integer primDig, segDig;
		int soma = 0, peso = CPF_10;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		if ((soma % CPF_LEN == 0) | (soma % CPF_LEN == 1)) {
			primDig = Integer.valueOf(0);
		} else {
			primDig = Integer.valueOf(CPF_LEN - (soma % CPF_LEN));
		}

		soma = 0;
		peso = CPF_LEN;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		soma += primDig.intValue() * 2;
		if ((soma % CPF_LEN == 0) | (soma % CPF_LEN == 1)) {
			segDig = Integer.valueOf(0);
		} else {
			segDig = Integer.valueOf(CPF_LEN - (soma % CPF_LEN));
		}

		return primDig.toString() + segDig.toString();
	}

	/**
	 * 
	 * @param cpf
	 *            String valor a ser testado
	 * @return boolean indicando se o usuÃ¡rio entrou com um CPF padrÃ£o
	 */
	private static boolean isCPFPadrao(String cpf) {
		if (cpf.equals("11111111111") || cpf.equals("22222222222")
				|| cpf.equals("33333333333") || cpf.equals("44444444444")
				|| cpf.equals("55555555555") || cpf.equals("66666666666")
				|| cpf.equals("77777777777") || cpf.equals("88888888888")
				|| cpf.equals("99999999999")) {

			return true;
		}

		return false;
	}

	/**
	 * MÃ©todo responsÃ¡vel por validar o CPF. NÃ£o aceita dados como
	 * 1111111111.
	 * 
	 * @param cpf
	 * @return
	 */
	public static boolean validaCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != CPF_LEN) || isCPFPadrao(cpf)) {
			return false;
		}

		try {
			Long numero = Long.parseLong(cpf);
//			Cnpj 0000 não  é válido.
			if (numero == 0) {
				return false;
			}
		} catch (NumberFormatException e) { // CPF nÃ£o possui somente nÃºmeros
			return false;
		}
		
		if (!calcDigVerif(cpf.substring(0, CPF_9)).equals(
				cpf.substring(CPF_9, CPF_LEN))) {
			return false;
		}

		return true;
	}

	/**
	 * MÃ©todo responsÃ¡vel por formatar o CPF.
	 * 
	 * @param cpfLong
	 * @return
	 */
	public static String formatarCpf(Long cpfLong) {
		String cpf = StringUtils.leftPad(cpfLong.toString(),
				NUMBER_OF_DIGITS_CPF, '0');
		cpf = FormatadorUtil.aplicarMascara(cpf, CPF_MASK);

		return cpf;
	}

}
