package com.unisys.br.amsfw.test.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.unisys.br.amsfw.log.Log;

/**
 * Classe para acessar o menu do sistema.
 * 
 * @author FlanmarP
 * 
 */
public class Menu {

	/**
	 * metodo para acessar o menu, deve ser informa no 'idXpth', somento o valor
	 * sorrespondente oa id contido no xpth do leemento.
	 * 
	 * Exemplo: xpth completo = //*[@id="formMenu-j_idt18-itemMenu"].
	 * 
	 * idXpth = formMenu-j_idt18-itemMenu.
	 * 
	 * textLink corresponde a label do link no menu.
	 * 
	 * Fornecer o WebDriver.
	 * 
	 * @author FlanmarP
	 * 
	 * @param textLink
	 * @param idXpth
	 * @param driver
	 */
	public void testAcesarMenu(String textLink, String idXpth, WebDriver driver) {
		try {
			Time.deleyMid();
			WebElement menu = driver.findElement(By.xpath("//label[contains(@id,'" + idXpth + "')]"));
			Actions acao = new Actions(driver);
			acao.moveToElement(menu).build().perform();
			Time.deleyMid();
			driver.findElement(By.linkText(textLink)).click();
			Time.deleyMid();
		} catch (Exception e) {
			new Log(e, getClass()).criarLog(driver);
			driver.quit();
			System.exit(0);
		}
	}
}
