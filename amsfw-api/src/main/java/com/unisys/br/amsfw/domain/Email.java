package com.unisys.br.amsfw.domain;



import java.io.Serializable;

/**
 * Serviço de email do framework amsfw.
 * 
 * @author delfimsm
 * 
 */
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	private String from;

	private String to;

	private String subject;

	private String text;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
