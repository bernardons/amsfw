package com.unisys.br.amsfw.web.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.unisys.br.amsfw.util.CnpjUtil;
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
	public void validate(FacesContext context, UIComponent component, Object value) {
		if (!CnpjUtil.validarCNPJ((String) value)) {
			throw new ValidatorException(FacesUtils.addErrorMessageReturningMessage(CNPJ_INVALIDO));
		}
	}

}
