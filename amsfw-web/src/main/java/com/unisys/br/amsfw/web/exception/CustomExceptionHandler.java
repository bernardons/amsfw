package com.unisys.br.amsfw.web.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import com.unisys.br.amsfw.exception.AmsfwException;
import com.unisys.br.amsfw.web.faces.FacesUtils;

/**
 * Custom Exception Handler que trata as mensagens de exceção do sistema.
 * 
 * @author DelfimSM
 * 
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private static Logger log = Logger.getLogger(CustomExceptionHandler.class.getCanonicalName());
	private ExceptionHandler wrapped;

	/**
	 * Contrutor de Exception Handler.
	 * 
	 * @param exception
	 */
	public CustomExceptionHandler(ExceptionHandler exception) {
		this.wrapped = exception;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() throws FacesException {

		final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
		Boolean atingiuAmsfw = false;

		while (i.hasNext()) {

			ExceptionQueuedEvent event = i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			Throwable t = context.getException();

			final FacesContext fc = FacesContext.getCurrentInstance();
			// final Map<String, Object> requestMap =
			// fc.getExternalContext().getRequestMap();
			final NavigationHandler nav = fc.getApplication().getNavigationHandler();

			// here you do what ever you want with exception
			try {

				Throwable throwable = t.getCause();

				// Trata exceptions do tipo AmsfwException
				if (throwable instanceof AmsfwException) {
					lancarExcecaoTipoAmsfwException(fc, nav, throwable);
					break;
				} else if (throwable != null) {
					while (throwable.getCause() != null) {
						throwable = throwable.getCause();
						if (throwable instanceof AmsfwException) {
							lancarExcecaoTipoAmsfwException(fc, nav, throwable);
							atingiuAmsfw = true;
							break;
						}
					}
					if (!atingiuAmsfw) {
						tratarExcecao(t, fc, nav);
						break;
					}
				} else {
					tratarExcecao(t, fc, nav);
					break;
				}

			} finally {
				// remove it from queue
				i.remove();
			}
		}
		// parent hanle
		getWrapped().handle();
	}

	private void tratarExcecao(Throwable t, final FacesContext fc, final NavigationHandler nav) {
		log.log(Level.SEVERE, "Erro tratado.", t);
		StringWriter stringWriter = new StringWriter();
		t.printStackTrace(new PrintWriter(stringWriter, true));
		FacesUtils.addErrorMessage(t.getMessage() + stringWriter.toString());
		nav.handleNavigation(fc, null, "/error.xhtml");
		fc.renderResponse();
	}

	private void lancarExcecaoTipoAmsfwException(final FacesContext facesContext, final NavigationHandler navigationHandler,
		Throwable throwable) {

		log.log(Level.SEVERE, "Erro tratado.", throwable);

		if (((AmsfwException) throwable).getParametros() != null && ((AmsfwException) throwable).getParametros().length > 0) {
			MessageFormat mf = new MessageFormat(FacesUtils.getMessage(((AmsfwException) throwable).getChave()));
			FacesUtils.addErrorMessage(mf.format(((AmsfwException) throwable).getParametros()).toString());
		} else {
			FacesUtils.addErrorMessageBundle(((AmsfwException) throwable).getChave());
		}

		navigationHandler.handleNavigation(facesContext, null, null);
		facesContext.renderResponse();
	}

}
