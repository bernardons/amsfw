package com.unisys.br.amsfw.web.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;

import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Validador de Cpf.
 * 
 * @author DelfimSM
 * 
 */
@FacesValidator("validator.TelefoneValidator")
public class TelefoneValidator implements Validator {

	private static final String TELEFONE_INVALIDO = "Telefone inválido.";

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		validaTelefone((String) value);
	}

	/**
	 * Valida CPF do usuário.
	 * 
	 * @param telefone
	 *            String valor com 14 dígitos
	 */
	public static void validaTelefone(String telefone) throws ValidatorException {
		
		if(StringUtils.isEmpty(telefone)){
			return;
		}
		
		if (telefone.length() < 10 || telefone.length() > 14) {
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(TELEFONE_INVALIDO));
		}

		telefone = telefone.replace("(", "");
		telefone = telefone.replace(")", "");
		telefone = telefone.replace("-", "");
		
		try {
			Long.parseLong(telefone);
		} catch (NumberFormatException e) { // Telefone não possui somente números
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(TELEFONE_INVALIDO));
		}
		
	}
}
