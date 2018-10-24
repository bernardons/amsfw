package com.unisys.br.amsfw.dao.util.service.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
			Address from;
			if (email.getAliasFrom() != null) {
				from = new InternetAddress(email.getFrom(), email.getAliasFrom());
			} else {
				from = new InternetAddress(email.getFrom());
			}
			m.setFrom(from);
			if (email.getTo() != null) {
				m.setRecipients(Message.RecipientType.TO, getListaEmails(email.getSeparador(), email.getTo()));
			}
			if (email.getCc() != null) {
				m.setRecipients(Message.RecipientType.CC, getListaEmails(email.getSeparador(), email.getCc()));
			}
			if (email.getCco() != null) {
				m.setRecipients(Message.RecipientType.BCC, getListaEmails(email.getSeparador(), email.getCco()));
			}
			m.setSubject(email.getSubject());
			m.setSentDate(DateUtil.getDataAtualHoraMinuto());
			m.setContent(email.getText(), email.getContent() + "; charset=UTF-8");

			Transport.send(m);
			System.out.println("Email enviado...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro enviando mensagem: " + e);
			throw new AmsfwException("Erro enviando mensagem: " + e, e);
		}
	}

	/**
	 * Envia email com anexo.
	 * 
	 * @author RegoIanC
	 * 
	 * @param email
	 */
	public void enviarEmailAnexo(Email email) {

		try {
			MimeMessage m = new MimeMessage(getMailSession());
			Address from = new InternetAddress(email.getFrom());
			m.setFrom(from);
			if (email.getTo() != null) {
				m.setRecipients(Message.RecipientType.TO, getListaEmails(email.getSeparador(), email.getTo()));
			}
			if (email.getCc() != null) {
				m.setRecipients(Message.RecipientType.CC, getListaEmails(email.getSeparador(), email.getCc()));
			}
			if (email.getCco() != null) {
				m.setRecipients(Message.RecipientType.BCC, getListaEmails(email.getSeparador(), email.getCco()));
			}
			MimeBodyPart mbp1 = new MimeBodyPart();
			// mbp1.setText();
			mbp1.setContent(email.getText(), email.getContent());

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			for (File anexo : email.getAnexos()) {
				MimeBodyPart mbp2 = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(anexo);
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(anexo.getName());
				mp.addBodyPart(mbp2);
			}

			m.setContent(mp);

			m.setSubject(email.getSubject());
			m.setSentDate(DateUtil.getDataAtualHoraMinuto());

			Transport.send(m);
			System.out.println("Email enviado...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro enviando mensagem: " + e);
			throw new AmsfwException("Erro enviando mensagem: " + e, e);
		}
	}

	private Address[] getListaEmails(String separador, String email) throws Exception {
		String[] emails = email.split(separador);
		List<Address> listaEmail = new ArrayList<Address>();
		for (String e : emails) {
			if (e == null || e.trim().isEmpty()) {
				continue;
			}
			listaEmail.add(new InternetAddress(e));
		}

		return listaEmail.toArray(new InternetAddress[0]);
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
