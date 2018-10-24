package com.unisys.br.amsfw.log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;

/**
 * Classe burra que contem os atributos para montagem de um log.
 * 
 * @author OliveiMH
 */
public class Log {

	private String testCase;
	private String metodo;
	private Date dataErro;
	private Integer linhaCodigo;
	private String descricaoFalha;

	/**
	 * Construtor vazio.
	 */
	public Log() {
	}

	/**
	 * Construtor com todos os parametros necessario para montar um log.
	 * 
	 * @param metodo
	 * @param dataErro
	 * @param tempoExecucao
	 * @param resultadoTeste
	 * @param linhaCodigo
	 * @param descricaoFalha
	 */
	public Log(Error erro) {
		StackTraceElement stack = erro.getStackTrace()[2];
		this.metodo = stack.getMethodName();
		this.dataErro = new Date();
		this.linhaCodigo = stack.getLineNumber();
		this.descricaoFalha = erro.getMessage();
		this.testCase = stack.getClassName();
	}

	/**
	 * Construtor com todos os parametros necessario para montar um log.
	 * 
	 * @param metodo
	 * @param dataErro
	 * @param tempoExecucao
	 * @param resultadoTeste
	 * @param linhaCodigo
	 * @param descricaoFalha
	 */
	@SuppressWarnings("rawtypes")
	public Log(Exception erro, Class classe) {
		for (StackTraceElement item : erro.getStackTrace()) {
			if (item.getClassName().equals(classe.getName())) {
				this.metodo = item.getMethodName();
				this.dataErro = new Date();
				this.linhaCodigo = item.getLineNumber();
				this.descricaoFalha = erro.getMessage();
				this.testCase = item.getClassName();
				break;
			}
		}
	}

	/**
	 * Método que monta o log com o nome da classe,metodo,linha, data e
	 * descrição da falhar Escrever ao fim do StringBuilder. Concatenação de um
	 * StringBuilder
	 * 
	 * @return
	 */
	private String montaLog() {
		StringBuilder log = new StringBuilder();
		log.append("O teste da classe - ").append(testCase).append(": Falhou")
				.append("\n");
		log.append("Método: ").append(metodo).append("\n");
		log.append("Linha do erro: ").append(linhaCodigo).append("\n");
		log.append("Data/Hora do erro: ")
				.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
						.format(dataErro)).append("\n");
		log.append("Descrição da falha: ").append(descricaoFalha);
		return log.toString();
	}

	/**
	 * Método que cria log na pasta C.
	 * 
	 * @param driver
	 */
	public void criarLog(WebDriver driver) {
		String log = montaLog();
		File dir = new File("C:\\Log Selenium");
		dir.mkdir();
		File file = new File("C:\\Log Selenium\\" + testCase + ".log");
		try {
			FileWriter fw = new FileWriter(file, true);
			fw.write(log);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Date getDataErro() {
		return dataErro;
	}

	public void setDataErro(Date dataErro) {
		this.dataErro = dataErro;
	}

	public Integer getLinhaCodigo() {
		return linhaCodigo;
	}

	public void setLinhaCodigo(Integer linhaCodigo) {
		this.linhaCodigo = linhaCodigo;
	}

	public String getDescricaoFalha() {
		return descricaoFalha;
	}

	public void setDescricaoFalha(String descricaoFalha) {
		this.descricaoFalha = descricaoFalha;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

}
