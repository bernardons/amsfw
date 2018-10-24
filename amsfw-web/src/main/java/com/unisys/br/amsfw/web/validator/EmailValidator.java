package com.unisys.br.amsfw.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validador de email.
 * 
 * @author DelfimSM
 *
 */
@FacesValidator("validator.EmailValidator")
public class EmailValidator implements Validator {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\."
			+ "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" + "(\\.[A-Za-z]{2,})$";

	private Pattern pattern;
	private Matcher matcher;

	/**
	 * Construtor do validador de email.
	 * 
	 */
	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		matcher = pattern.matcher(value.toString().trim());
		if (!matcher.matches() && !value.toString().trim().equals("")) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email inválido",""));
		}

	}
}
