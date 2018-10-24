package com.unisys.br.amsfw.web.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;

import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Validador de cnpj.
 * 
 * @author DelfimSM
 *
 */
@FacesValidator("validator.CnpjValidator")
public class CnpjValidator implements Validator {

	private static final String CNPJ_INVALIDO = "Cnpj inválido.";

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)  {
		validaCNPJ((String) value);
	}

	/**
	 * Valida CNPJ do usuário.
	 * 
	 * @param cnpj
	 *            String valor com 14 dígitos
	 */
	public static void validaCNPJ(String cnpj) throws ValidatorException {
		
		if(StringUtils.isEmpty(cnpj)){
			return;
		}
		
		cnpj = cnpj.replace(".", "");
		cnpj = cnpj.replace("-", "");
		cnpj = cnpj.replace("/", "");
		
		if (cnpj == null || cnpj.length() != 14) {
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CNPJ_INVALIDO));
		}

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CNPJ_INVALIDO));
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char chr_cnpj[] = cnpj.toCharArray();
		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}

		int dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(
				dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
		soma = 0;
		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}
		
		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(
				dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();

		if (!cnpj.equals(cnpj_calc))
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CNPJ_INVALIDO));
	}

}
