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
@FacesValidator("validator.CpfValidator")
public class CpfValidator implements Validator {

	private static final String CPF_INVALIDO = "Cpf inválido.";

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		validaCpf((String) value);
	}

	/**
	 * Valida CPF do usuário.
	 * 
	 * @param cpf
	 *            String valor com 14 dígitos
	 */
	public static void validaCpf(String cpf) throws ValidatorException {
		
		if(StringUtils.isEmpty(cpf)){
			return;
		}
		
		if (cpf.length() != 14 && cpf.length() != 11) {
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CPF_INVALIDO));
		}

		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		
		try {
			Long.parseLong(cpf);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CPF_INVALIDO));
		}
		
		if(! CpfUtil.validaCPF(cpf)){
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CPF_INVALIDO));
		}

	}
	
}
