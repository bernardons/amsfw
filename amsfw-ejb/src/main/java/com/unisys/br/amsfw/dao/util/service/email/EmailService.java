package com.unisys.br.amsfw.dao.util.service.email;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.unisys.br.amsfw.domain.Email;
import com.unisys.br.amsfw.exception.AmsfwException;
import com.unisys.br.amsfw.util.DateUtil;

/**
 * Serviço de email do framework amsfw.
 * 
 * @author delfimsm
 * 
 */
@Stateless
public class EmailService {

	@Resource(mappedName = "java:jboss/mail/expresso")
	private Session mailSession;

	/**
	 * Envia emai para o sistema.
	 * 
	 * @param email
	 */
	public void enviarEmail(Email email) {

		try {
			MimeMessage m = new MimeMessage(getMailSession());
			Address from = new InternetAddress(email.getFrom());
			Address[] to = new InternetAddress[] {new InternetAddress(email.getTo()) };

			m.setFrom(from);
			m.setRecipients(Message.RecipientType.TO, to);
			m.setSubject(email.getSubject());
			m.setSentDate(DateUtil.getDataAtualHoraMinuto());
			m.setContent(email.getText(), "text/plain");

			Transport.send(m);
			System.out.println("Email enviado...");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Erro enviando mensagem: " + e);
			throw new AmsfwException("Erro enviando mensagem: " + e, e);
		}
	}

	/**
	 * Metodo protegido que retorna o resource Session default. Caso a classe
	 * seja estendida, e outra Session seja necessaria, esse metodo deve ser
	 * sobrescrito para prover a mesma funcionalidade.
	 * 
	 * @return
	 */
	protected Session getMailSession() {
		return mailSession;
	}

}
