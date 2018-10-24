package com.unisys.br.amsfw.test.base;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unisys.br.amsfw.test.util.ConfigUtil;

/**
 * Classe que deve ser herdada por todos os testes automatizados. Essa classe
 * contem todos os metodos para acessar e manipular atributos da tela.
 * 
 * @author RegoIanC
 * 
 */
public abstract class BaseSelenium {

	private static final int SLEEP_PICK_LIST = 500;
	private static final int SLEEP_SELECT = 400;
	private static final int TIMEOUT_WAIT = 10;
	private static final int TIMEOUT_START = 30;
	private static String comecoUrl = "";
	/**
	 * Define que a Thread vai aguardar 2 segundo para o carregamento de campos
	 * auto complete.
	 **/
	protected static final long TEMPO_CARREGAMENTO_AUTO_COMPLETE = 2200;
	/**
	 * Define que a Thread vai aguardar 300ms segundo para o carregamento
	 * completo do ajax.
	 **/
	protected static final long AGUARDAR_RESPOSTA_AJAX = 300;

	public static final String MENSAGEM_CAMPO_OBRIGATORIO = ConfigUtil.getPropertiesSistema("default.required.message");

	private WebDriver driver = null;

	/**
	 * Aguarda o retorno ajax.
	 * 
	 * @return
	 */
	public boolean aguardaRetornoAjax() {
		final long startTime = System.currentTimeMillis();
		final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

		while ((startTime + 5000) != System.currentTimeMillis()) {
			final Boolean scriptResult = (Boolean) javascriptExecutor.executeScript("return jQuery.active == 0");

			if (scriptResult) {
				return true;
			}
			delay(100);

		}
		return false;
	}

	private void delay(final long amount) {
		try {
			Thread.sleep(amount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Define qual sera a URL que o selenium irá acessar.
	 * 
	 * @author RegoIanC
	 * @param urlProjeto
	 */
	public BaseSelenium(final String urlProjeto) {
		comecoUrl = urlProjeto;
	}

	/**
	 * Inicia o selenium.
	 * 
	 * @throws Exception
	 */
	@Before
	public void seleniumStart() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(comecoUrl);
		driver.manage().timeouts().implicitlyWait(TIMEOUT_START, TimeUnit.SECONDS);
	}

	/**
	 * Finaliza o selenium.
	 * 
	 * @throws Exception
	 */
	@After
	public void seleniumStop() throws Exception {
		// Close the browser
		driver.quit();
	}

	protected void irParaPagina(String pagina) {
		driver.get(comecoUrl + pagina);

	}

	protected void aguardarTituloConter(final String conteudo) {
		// Timeout de 10 segundos para verificar se a página foi renderizada.
		new WebDriverWait(driver, TIMEOUT_WAIT).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith(conteudo);
			}
		});
	}

	protected void aguardarTextoConter(final String conteudo) {
		// Timeout de 10 segundos para verificar se a página foi renderizada.
		new WebDriverWait(driver, TIMEOUT_WAIT).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getPageSource().contains(conteudo);
			}
		});
	}

	protected WebElement encontrarInputPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]"));
	}

	protected WebElement encontrarSelectPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//select[contains(@id,'" + elemento + "')]"));
	}

	protected WebElement encontrarLinkPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//a[contains(@id,'" + elemento + "')]"));
	}

	protected WebElement encontrarRadioButtonPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]"));
	}

	protected WebElement encontrarQualquerElemento(String tag, String atributo, String elemento) {
		return driver.findElement(By.xpath("//" + tag + "[contains(@" + atributo + ",'" + elemento + "')]"));

	}

	protected WebElement encontrarSpanPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//span[contains(@id,'" + elemento + "')]"));
	}

	protected WebElement encontrarTextAreaPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//textarea[contains(@id,'" + elemento + "')]"));
	}

	protected void setSelectValue(String elemento, String value) throws Exception {
		new Select(encontrarSelectPorIdContendo(elemento)).selectByVisibleText(value);
		Thread.sleep(SLEEP_SELECT);
	}

	protected WebElement encontrarSelectPorCssContendo(String seletor) {
		return driver.findElement(By.cssSelector(seletor));
	}

	protected void setSelectValueByCss(String seletor, String value) throws Exception {
		new Select(encontrarSelectPorCssContendo(seletor)).selectByVisibleText(value);
		Thread.sleep(SLEEP_SELECT);
	}

	protected void encontrarAutoCompletePorIdContendo(String elemento, String value) throws InterruptedException {
		driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]")).sendKeys("");
		driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]")).sendKeys(value);
		Thread.sleep(TEMPO_CARREGAMENTO_AUTO_COMPLETE);
		driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]")).sendKeys(Keys.TAB);
		Thread.sleep(AGUARDAR_RESPOSTA_AJAX);
	}

	/**
	 * Seleciona todos os itens do pickList.
	 * 
	 * @author OliveiMH
	 * @throws Exception
	 */
	protected void selecionaTodosPickList() throws Exception {
		getDriver().findElement(By.className("ui-icon-arrowstop-1-e")).click();
		Thread.sleep(SLEEP_PICK_LIST);
	}

	/**
	 * Seleciona item a item de um pickList.
	 * 
	 * @author OliveiMH
	 * @param itemLabel
	 * @throws Exception
	 */
	protected void selecionaItemPickList(String itemLabel) throws Exception {
		aguardaRetornoAjax();
		Actions actions = new Actions(getDriver());
		WebElement elemento = getDriver().findElement(By.xpath("//li[contains(@data-item-label,'" + itemLabel + "')]"));
		aguardaRetornoAjax();
		actions.doubleClick(elemento);
		aguardaRetornoAjax();
		actions.perform();
		aguardaRetornoAjax();
	}

	/**
	 * Seleciona um item de uma arvore por Id.
	 * 
	 * @author OliveiMH
	 * @throws Exception
	 */
	protected void selecionaItemArvorePorId(String elemento) throws Exception {
		getDriver().findElement(By.xpath(".//*[@id='" + elemento + "']/span/span[1]")).click();
	}

	protected void selecionaLinkDaArvorePorLabel(String label) throws Exception {
		getDriver().findElement(By.xpath("//span[contains(., \"" + label + "\")]")).click();
	}

	/**
	 * Verifica se o selenium abriu a tela correta.
	 * 
	 * @param paginaAtual
	 * @return se estiver na página correta retorna true
	 */
	protected boolean isPaginaCorreta(String paginaAtual) {
		return driver.getCurrentUrl().contains(paginaAtual);
	}

	/**
	 * Realiza o click de um botão da tela passando o label do botão como
	 * parâmetro.
	 * 
	 * @param label
	 */
	protected void clicaBotaoPorLabel(String label) {
		driver.findElement(By.linkText(label)).click();
	}
	
	/**
	 * Realiza o click de um botão da tela passando um atributo css do botão
	 * como parâmetro.
	 * 
	 * @param label
	 */
	protected void clicaBotaoPorCss(String css) {
		driver.findElement(By.cssSelector(css)).click();
	}

	/**
	 * Realiza o click de um botão da tela passando um atributo css do botão
	 * como parâmetro.
	 * 
	 * @author FlanmarP
	 * 
	 * @param label
	 */
	protected void clicaBotaoPorCss(String tag, String atributo, String valor) {
		driver.findElement(By.cssSelector(tag + "[" + atributo + "='" + valor + "']")).click();
	}

	/**
	 * Realiza o click de um botão da tela passando id do botão como parâmetro.
	 * 
	 * @param label
	 */
	protected void clicaBotaoPorId(String id) {
		getDriver().findElement(By.xpath("//button[contains(@id,'" + id + "')]")).click();
	}

	protected WebElement encontrarBotaoPorCss(String css) {
		return driver.findElement(By.cssSelector(css));
	}

	/**
	 * Limpa o valor que estiver no campo.
	 * 
	 * @param idCampo
	 */
	protected void limpaCampo(String idCampo) throws Exception {
		Thread.sleep(600);
		encontrarInputPorIdContendo(idCampo).sendKeys(Keys.SHIFT, Keys.HOME);
		Thread.sleep(600);
		encontrarInputPorIdContendo(idCampo).sendKeys(Keys.BACK_SPACE);
		Thread.sleep(600);
	}

	/**
	 * Limpa o valor que estiver no campo data.
	 * 
	 * @author OliveiMH
	 * @param idCampo
	 * @throws Exception
	 */
	protected void limpaData(String idCampo) throws Exception {
		encontrarInputPorIdContendo(idCampo).sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
				Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
				Keys.BACK_SPACE);
		Thread.sleep(AGUARDAR_RESPOSTA_AJAX);
	}

	protected void limpaTextArea(String idCampo) {
		encontrarTextAreaPorIdContendo(idCampo).sendKeys(Keys.SHIFT, Keys.HOME);
		encontrarTextAreaPorIdContendo(idCampo).sendKeys(Keys.BACK_SPACE);
	}

	/**
	 * Retorna o value de um input.
	 * 
	 * @param idCampo
	 * @return Valor de um Input
	 */
	protected String pegarValorInputPorId(String idCampo) {
		return encontrarInputPorIdContendo(idCampo).getAttribute("value");
	}

	/**
	 * Retorna o value de um select.
	 * 
	 * @param idCampo
	 * @return Valor de um Input
	 */
	protected String pegarValorSelectPorId(String idCampo) {
		return new Select(encontrarSelectPorIdContendo(idCampo)).getFirstSelectedOption().getAttribute("value");
	}

	/**
	 * Retorna o value de um textArea.
	 * 
	 * @param idCampo
	 * @return Valor de um Input
	 */
	protected String pegarValorTextAreaPorId(String idCampo) {
		return encontrarTextAreaPorIdContendo(idCampo).getAttribute("value");
	}

	/**
	 * Verifica se um <i>input</i> esta com o atributo readonly igual a true.
	 * 
	 * @param campo
	 * @return true caso o <i>input</i> esteja readonly
	 */
	protected boolean testReadOnly(String campo) {
		if (encontrarInputPorIdContendo(campo) != null
				&& encontrarInputPorIdContendo(campo).getAttribute("readonly") != null
				&& encontrarInputPorIdContendo(campo).getAttribute("readonly").equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Verifica se um <i>select</i> esta com o atributo readonly igual a true.
	 * 
	 * @param campo
	 * @return true caso o <i>select</i> esteja readonly
	 */
	protected boolean testReadOnlySelect(String campo) {
		if (encontrarSelectPorIdContendo(campo) != null
				&& encontrarSelectPorIdContendo(campo).getAttribute("readonly") != null
				&& encontrarSelectPorIdContendo(campo).getAttribute("readonly").equals("true")) {
			return true;
		}
		return false;
	}

	protected String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * Seta no input do primeiro parametro o valor passado no segundo parametro.
	 * 
	 * @param elemento
	 * @param value
	 * @throws Exception
	 */
	protected void setInputValue(String elemento, String value) throws Exception {
		limpaCampo(elemento);
		Thread.sleep(600);
		encontrarInputPorIdContendo(elemento).sendKeys(value);
	}

	/**
	 * Define os fluxos básicos de um caso de teste.
	 */
	protected abstract void fluxoBasico();

	/**
	 * Possui todos os métodos relacionados com o teste de inserir do ECT<br>
	 * .
	 * 
	 * @throws Exception
	 */
	public abstract void testCenarioInserir() throws Exception;

	/**
	 * Possui todos os métodos relacionados com o teste de pesquisar do ECT<br>
	 * .
	 * 
	 * @throws Exception
	 */
	public abstract void testCenarioPesquisar() throws Exception;

	/**
	 * Possui todos os métodos relacionados com o teste de editar do ECT<br>
	 * .
	 * 
	 * @throws Exception
	 */
	public abstract void testCenarioEditar() throws Exception;

	/**
	 * Possui todos os métodos relacionados com o teste de visualizar do ECT<br>
	 * .
	 * 
	 * @throws Exception
	 */
	protected void testCenarioVisualizar() throws Exception {

	}

	/**
	 * Possui todos os métodos relacionados com o teste de Excluir do ECT<br>
	 * .
	 * 
	 * @throws Exception
	 */
	public void testCenarioExcluir() throws Exception {

	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void realizaTeste() throws Exception {
		fluxoBasico();
		testCenarioInserir();
		urlInicioTeste();
		testCenarioPesquisar();
		urlInicioTeste();
		testCenarioEditar();
	}

	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Retorna a url da pagina que sera testada.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract void urlInicioTeste();

	/**
	 * Metodo que captura a imagem do teste.
	 * 
	 * @author OliveiMH
	 * @param casoDeTeste
	 * @param metodoTeste
	 * @throws Exception
	 */
	protected void evidenciaSelenium(String versao, String casoDeTeste, String metodoTeste) throws Exception {
		File imageFile = (File) ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String failureImageFileName = "C:\\" + versao + "\\" + casoDeTeste + "\\" + metodoTeste + ".png";
		File evidenciaSeleniumFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, evidenciaSeleniumFile);
	}

	/**
	 * retorna o valor contido no semáforo(elemento).
	 * 
	 * @author FlanmarP
	 * @param xpath
	 * @return
	 */
	protected String retornaTexto(String xPath) {
		return driver.findElement(By.xpath(xPath)).getText();
	}

	/**
	 * retorna a cor correspondente ao semáforo(elemento).
	 * 
	 * @author FlanmarP
	 * @param xpath
	 * @return
	 */
	protected String retornaCor(String xPath) {
		return driver.findElement(By.xpath(xPath)).getCssValue("background-color");
	}

	/**
	 * Verifica se existe um determinado texto da página.
	 * 
	 * @author FlanmarP
	 * @param texto
	 * @return
	 */
	protected boolean localizarTexto(String texto) {
		return driver.getPageSource().contains(texto);
	}
}