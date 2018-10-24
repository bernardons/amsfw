package com.unisys.br.amsfw.test.base;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Classe que deve ser herdada por todos os testes automatizados. Essa classe contem todos os metodos para acessar e manipular atributos da
 * tela.
 * 
 * @author RegoIanC
 * 
 */
public abstract class BaseSelenium {

	private static String COMECO_URL = "";
	/** Define que a Thread vai aguardar 2 segundo para o carregamento de campos auto complete. **/
	protected static final long TEMPO_CARREGAMENTO_AUTO_COMPLETE = 2200;
	/** Define que a Thread vai aguardar 300ms segundo para o carregamento completo do ajax. **/
	protected static final long AGUARDAR_RESPOSTA_AJAX = 300;

	public static final String MENSAGEM_CAMPO_OBRIGATORIO = "Campo Obrigatório";

	private WebDriver driver = null;

	/**
	 * Define qual sera a URL que o selenium irá acessar.
	 * 
	 * @author RegoIanC
	 * @param urlProjeto
	 */
	public BaseSelenium(final String urlProjeto) {
		COMECO_URL = urlProjeto;
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
		driver.get(COMECO_URL);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
		driver.get(COMECO_URL + pagina);
	}

	protected void aguardarTituloConter(final String conteudo) {
		// Timeout de 10 segundos para verificar se a página foi renderizada.
		new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith(conteudo);
			}
		});
	}

	protected void aguardarTextoConter(final String conteudo) {
		// Timeout de 10 segundos para verificar se a página foi renderizada.
		new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
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

	protected WebElement encontrarSpanPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//span[contains(@id,'" + elemento + "')]"));
	}

	protected WebElement encontrarTextAreaPorIdContendo(String elemento) {
		return driver.findElement(By.xpath("//textarea[contains(@id,'" + elemento + "')]"));
	}

	protected void setSelectValue(String elemento, String value) throws Exception {
		new Select(encontrarSelectPorIdContendo(elemento)).selectByVisibleText(value);
		Thread.sleep(400);
	}

	protected void encontrarAutoCompletePorIdContendo(String elemento, String value) throws InterruptedException {
		driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]")).sendKeys("");
		driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]")).sendKeys(value);
		Thread.sleep(TEMPO_CARREGAMENTO_AUTO_COMPLETE);
		driver.findElement(By.xpath("//input[contains(@id,'" + elemento + "')]")).sendKeys(Keys.TAB);
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
	 * Realiza o click de um botão da tela passando o label do botão como parâmetro.
	 * 
	 * @param label
	 */
	protected void clicaBotaoPorLabel(String label) {
		driver.findElement(By.linkText(label)).click();
	}

	/**
	 * Realiza o click de um botão da tela passando um atributo css do botão como parâmetro.
	 * 
	 * @param label
	 */
	protected void clicaBotaoPorCss(String css) {
		driver.findElement(By.cssSelector(css)).click();
	}

	protected WebElement encontrarBotaoPorCss(String css) {
		return driver.findElement(By.cssSelector(css));
	}

	/**
	 * Limpa o valor que estiver no campo.
	 * 
	 * @param idCampo
	 */
	protected void limpaCampo(String idCampo) {
		encontrarInputPorIdContendo(idCampo).sendKeys(Keys.SHIFT, Keys.HOME);
		encontrarInputPorIdContendo(idCampo).sendKeys(Keys.BACK_SPACE);
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
		if (encontrarInputPorIdContendo(campo) != null && encontrarInputPorIdContendo(campo).getAttribute("readonly") != null
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
		if (encontrarSelectPorIdContendo(campo) != null && encontrarSelectPorIdContendo(campo).getAttribute("readonly") != null
				&& encontrarSelectPorIdContendo(campo).getAttribute("readonly").equals("true")) {
			return true;
		}
		return false;
	}

	protected String getPageSource() {
		return driver.getPageSource();
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

	public WebDriver getDriver() {
		return driver;
	}

}