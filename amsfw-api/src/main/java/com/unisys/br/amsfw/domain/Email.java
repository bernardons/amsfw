package com.unisys.br.amsfw.domain;

import java.io.File;
import java.io.Serializable;

/**
 * Serviço de email do framework amsfw. Separador Padrao deve ser ; .
 * 
 * @author delfimsm
 * 
 */
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	private static String separadorEmail = ";";

	private String from;
	
	private String aliasFrom;

	private String to;

	private String cc;

	private String cco;

	private String subject;

	private String text;

	private String separador = separadorEmail;

	private String content = "text/plain";

	private File[] anexos;

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

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	public String getSeparador() {
		return separador;
	}

	public void setSeparador(String separador) {
		this.separador = separador;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public File[] getAnexos() {
		return anexos;
	}

	public void setAnexos(File[] anexos) {
		this.anexos = anexos;
	}

	public String getAliasFrom() {
		return aliasFrom;
	}

	public void setAliasFrom(String aliasFrom) {
		this.aliasFrom = aliasFrom;
	}

}
