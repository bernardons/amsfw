package br.gov.cef.simco.selenium.util;

/**
 * Classe para gerar um deley na execução.
 * 
 * @author FlanmarP
 * 
 */
public class Time {

	private Time() {
	}

	private static final int MIN = 1000;
	private static final int MIDDLE = 2000;
	private static final int MAX = 4000;

	/**
	 * 
	 * @throws InterruptedException
	 */
	public static void deleyMin() throws InterruptedException {
		Thread.sleep(MIN);
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	public static void deleyMid() throws InterruptedException {
		Thread.sleep(MIDDLE);
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	public static void deleyMax() throws InterruptedException {
		Thread.sleep(MAX);
	}
}
