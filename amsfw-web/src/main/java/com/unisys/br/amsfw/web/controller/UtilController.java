package com.unisys.br.amsfw.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.PropertyUtils;

import com.unisys.br.amsfw.util.DateUtil;

/**
 * Controller utilitário do sistema.
 * 
 * @author DelfimSM
 * 
 */
@ManagedBean
@RequestScoped
public class UtilController {

	/**
	 * Retorna a hora atual do sistema.
	 * 
	 * @return
	 */
	public String getHoraPorExtenso() {
		return DateUtil.dataAtualPorExtenso();
	}
	
	public boolean browserIE() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
		return userAgent.toLowerCase().contains("msie");
	}
	
	public boolean browserFirefox() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
		return userAgent.toLowerCase().contains("firefox");
	}
	
	/**
	 * Testa se o managed bean possui a propriedade.
	 * 
	 * @param o
	 * @param propertyName
	 * @return
	 */
	public static boolean hasProperty(Object o, String propertyName) {
		if (o == null || propertyName == null) {
			return false;
		}
		try {
			return PropertyUtils.getPropertyDescriptor(o, propertyName) != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Funcao que endereca o problema de update em multiplos ids do Primefaces,
	 * em que o seletor Jquery deve ser utilizado.
	 * Ex:update="@([id$=id1],[id$=id2])" para multiplos ids.
	 * update="@([id$=id1]" para 1 id;
	 * */
	public static String getIdFormatado(String idTela) {
		// Recupera os ids que foram passados por parametros.
		String padrao = "[id$=*]";
		if (!idTela.contains(",")) {
			return "@(" + padrao.replace("*", idTela) + ")";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("@(");
		for (String id : idTela.split(",")) {
			sb.append(padrao.replace("*", id.trim()));
			sb.append(",");
		}
		// Remove a ultima virgula
		sb.deleteCharAt(sb.length() - 1);
		// Inserir ultimo parentese.
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Testa se o managed bean possui a propriedade.
	 * 
	 * @param o
	 * @param propertyName
	 * @return
	 */
	public static String formataDataHora(Object data) {
		if (data == null) {
			return "";
		}
		if (data instanceof Date) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
			Date dataAtual = (Date) data;

			return simpleDateFormat.format(dataAtual);
		}

		return "";
	}
	
}
