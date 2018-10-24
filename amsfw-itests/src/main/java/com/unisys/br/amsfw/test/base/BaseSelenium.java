package com.unisys.br.amsfw.test.base;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
	private static final int SLEEP_SELECT = 1000;
	private static final int TIMEOUT_WAIT = 30;
	private static final int TIMEOUT_START = 30;
	private static String startUrl = "";
	private WebDriver driver = null;
	private static Browser BROWSER;

	protected static final long TEMPO_CARREGAMENTO_AUTO_COMPLETE = 4200;

	protected static final long AGUARDAR_RESPOSTA_AJAX = 300;

	public static final String MENSAGEM_CAMPO_OBRIGATORIO = ConfigUtil
			.getPropertiesSistema("default.required.message");

	public String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * Defines what will be the URL that selenium will access.
	 * 
	 * @param urlProject
	 */
	public BaseSelenium(final String urlProject, final Browser browser) {
		startUrl = urlProject;
		BROWSER = browser;
	}

	/**
	 * Starts selenium.
	 * 
	 * @throws Exception
	 */
	@Before
	public void seleniumStart() throws Exception {
		String address = System.getProperty("user.dir") + "\\drivers";

		if (BROWSER == Browser.Chrome) {
			System.setProperty("webdriver.chrome.driver", address
					+ "\\ChromeDriver.exe");
			driver = new ChromeDriver();
		} else if (BROWSER == Browser.InternetExplorer) {
			System.setProperty("webdriver.ie.driver", address
					+ "\\IEDriverServer.exe");
			DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
			dc.setCapability(
					InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
					true);
			dc.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver(dc);
		} else {
			driver = new FirefoxDriver();
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(startUrl);
		driver.manage().timeouts()
				.implicitlyWait(TIMEOUT_START, TimeUnit.SECONDS);
	}

	/**
	 * Terminate selenium.
	 * 
	 * @throws Exception
	 */
	@After
	public void seleniumStop() throws Exception {
		driver.quit();
	}

	/**
	 * Aguarda o retorno ajax.
	 * 
	 * @return
	 */
	public boolean waitReturnAjax() {
		final long startTime = System.currentTimeMillis();
		final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

		while ((startTime + 5000) != System.currentTimeMillis()) {
			final Boolean scriptResult = (Boolean) javascriptExecutor
					.executeScript("return jQuery.active == 0");

			if (scriptResult) {
				return true;
			}
			delay(100);

		}
		return false;
	}

	public void delay(final long amount) {
		try {
			Thread.sleep(amount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Redirects application page.
	 * 
	 * @param page
	 *            Add the page to be concatenated to parameterized homepage.
	 */
	public void goToPage(String page) {
		driver.get(startUrl + page);

	}

	/**
	 * Wait for the page has the title to the text passed as a parameter for the
	 * parameterized time by TIMEOUT_WAIT parameter.
	 * 
	 * @param text
	 *            in which the method will wait to be present in the page title.
	 */
	protected void waitTitleContain(final String text) {
		new WebDriverWait(driver, TIMEOUT_WAIT)
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.getTitle().toLowerCase().startsWith(text);
					}
				});

	}

	/**
	 * Wait for the page is the text passed as a parameter for the parameterized
	 * time by TIMEOUT_WAIT parameter.
	 * 
	 * @param text
	 *            in which the method waits to be present on the page.
	 */
	public void waitPageContainText(final String text) {
		new WebDriverWait(driver, TIMEOUT_WAIT)
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.getPageSource().contains(text);
					}
				});
	}

	/**
	 * Finds and returns an element of type input that contains the text passed
	 * as a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findInputByIdContaining(String text) {
		WebElement element = driver.findElement(By
				.xpath("//input[contains(@id,'" + text + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	/* Verificar esse metodo */
	protected WebElement encontrarInputPorIdRegexContendo(String elemento) {
		return driver.findElement(By.cssSelector(elemento));
	}

	/**
	 * Finds and returns an element of type select that contains the text passed
	 * as a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findSelectByIdContaining(String text) {
		WebElement element = driver.findElement(By
				.xpath("//select[contains(@id,'" + text + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	/**
	 * Finds and returns an element of type link that contains the text passed
	 * as a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findLinkByIdContaining(String text) {
		WebElement element = driver.findElement(By.xpath("//a[contains(@id,'"
				+ text + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	/**
	 * Finds and returns an element of type radio button (input) that contains
	 * the text passed as a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findRadioButtonByIdContaining(String text) {
		WebElement element = driver.findElement(By
				.xpath("//input[contains(@id,'" + text + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	/**
	 * Finds and returns any element in accordance with the passed parameters.
	 * 
	 * @param tag
	 *            to search.
	 * @param attribute
	 *            the element being sought.
	 * @param value
	 *            element attribute to be searched.
	 */
	public WebElement findAnyElement(String tag, String attribute, String value) {
		WebElement element = driver.findElement(By.xpath("//" + tag
				+ "[contains(@" + attribute + ",'" + value + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;

	}

	/**
	 * Finds and returns an element of type text area that contains the text
	 * passed as a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findTextAreaByIdContaining(String text) {
		WebElement element = driver.findElement(By
				.xpath("//textarea[contains(@id,'" + text + "')]"));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	/**
	 * Choose specific value in a select type element found by id in accordance
	 * with the passed parameters.
	 * 
	 * @param idSelect
	 *            present in the ID attribute of the select sought.
	 * @param value
	 *            to be chosen in the select.
	 */
	public void setSelectValueById(String idSelect, String value)
			throws Exception {
		new Select(findSelectByIdContaining(idSelect))
				.selectByVisibleText(value);
		delay(SLEEP_SELECT);
	}

	/**
	 * Finds and returns an element the css passed as a parameter.
	 * 
	 * @param cssSelector
	 */
	public WebElement findElementByCssSelector(String cssSelector) {
		WebElement element = driver.findElement(By.cssSelector(cssSelector));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	/**
	 * Choose specific value in a select type element found by cssSelector id in
	 * accordance with the passed parameters.
	 * 
	 * @param cssSelector
	 * @param value
	 *            to be chosen in the select.
	 */
	public void setSelectValueByCss(String cssSelector, String value)
			throws Exception {
		new Select(findElementByCssSelector(cssSelector))
				.selectByVisibleText(value);
		delay(SLEEP_SELECT);
	}

	/**
	 * It offers options with auto complete a field found by its ID.
	 * 
	 * @param text
	 *            text or part of this text in the field ID.
	 * @param value
	 *            text or part of this text in the autocomplete options pursued
	 *            in the field.
	 */
	public void findInputAutoCompleteByIdContaining(String text, String value)
			throws InterruptedException {
		driver.findElement(By.xpath("//input[contains(@id,'" + text + "')]"))
				.sendKeys("");
		driver.findElement(By.xpath("//input[contains(@id,'" + text + "')]"))
				.sendKeys(value);
		Thread.sleep(TEMPO_CARREGAMENTO_AUTO_COMPLETE);
		driver.findElement(By.xpath("//input[contains(@id,'" + text + "')]"))
				.sendKeys(Keys.TAB);
		Thread.sleep(AGUARDAR_RESPOSTA_AJAX);
	}

	/**
	 * It offers options with auto complete a field found by its Css.
	 * 
	 * @param text
	 *            text or part of this text in the field cssSelector.
	 * @param value
	 *            text or part of this text in the autocomplete options pursued
	 *            in the field.
	 */
	public void findInputAutoCompleteByCssContaining(String cssSelector,
			String value) throws InterruptedException {
		driver.findElement(By.cssSelector(cssSelector)).sendKeys("");
		driver.findElement(By.cssSelector(cssSelector)).sendKeys(value);
		Thread.sleep(TEMPO_CARREGAMENTO_AUTO_COMPLETE);
		driver.findElement(By.cssSelector(cssSelector)).sendKeys(Keys.TAB);
		Thread.sleep(AGUARDAR_RESPOSTA_AJAX);
	}

	/**
	 * Select all item from a picklist.
	 * 
	 * 
	 */
	protected void selectAllPickList() throws Exception {
		getDriver().findElement(By.className("ui-icon-arrowstop-1-e")).click();
		Thread.sleep(SLEEP_PICK_LIST);
	}

	/**
	 * Select item from a picklist.
	 * 
	 * @param itemLabel
	 *            Label item is selected.
	 */
	public void selectItemPickList(String itemLabel) throws Exception {
		Actions actions = new Actions(driver);
		WebElement element = driver.findElement(By
				.xpath("//li[contains(@data-item-label,'" + itemLabel + "')]"));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		waitReturnAjax();
		actions.doubleClick(element);
		waitReturnAjax();
		actions.perform();
		waitReturnAjax();
	}

	/**
	 * Select an item from a tree by Id.
	 * 
	 * @param text
	 * @throws Exception
	 */
	public void selectItemTreeById(String text) throws Exception {
		driver.findElement(By.xpath(".//*[@id='" + text + "']/span/span[1]"))
				.click();
	}

	/**
	 * Select an item from a tree by label.
	 * 
	 * @param label
	 * @throws Exception
	 */
	public void selectItemTreeByLabel(String label) throws Exception {
		getDriver().findElement(
				By.xpath("//span[contains(., \"" + label + "\")]")).click();
	}

	/**
	 * Verifies that the selenium opened the correct screen.
	 * 
	 * @param currentPage
	 * @return if you are on the correct page returns true
	 */
	protected boolean isCorrectPage(String currentPage) {
		return driver.getCurrentUrl().contains(currentPage);
	}

	/**
	 * Click on a page button using the label of the button as a parameter.
	 * 
	 * @param label
	 *            button when clicked.
	 */
	public void clickLinkByLabel(String label) {
		WebElement link = driver.findElement(By.linkText(label));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(link));
		link.click();
	}

	/**
	 * Click on a page button using the tag and the text of the button as a
	 * parameter by javascript
	 * 
	 * @param tag
	 * @param text
	 */
	public void clickTextByJS(String tag, String text) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"arguments[0].click()",
				driver.findElement(By.xpath("//" + tag + "[contains(text(),'"
						+ text + "')]")));
	}

	/**
	 * Click on a page button using the css of the button as a parameter.
	 * 
	 * @param cssSelector
	 *            button when clicked.
	 */
	public void clickButtonByCss(String cssSelector) {
		WebElement css = driver.findElement(By.cssSelector(cssSelector));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(css));
		css.click();
	}

	/**
	 * Click on a page button using the id of the button as a parameter.
	 * 
	 * @param id
	 *            button when clicked.
	 */
	public void clickButtonByIdContaining(String id) {
		WebElement button = driver.findElement(By
				.xpath("//button[contains(@id,'" + id + "')]"));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(button));
		button.click();
	}

	/**
	 * Clears the value of an input found by the id attribute that contains the
	 * value of the passed parameter.
	 * 
	 * @param idInput
	 *            Id or id part of the input to be cleaned.
	 */
	public void cleanInputWithIdContaining(String idInput) throws Exception {
		findInputByIdContaining(idInput).clear();
	}

	/**
	 * Clears the value of an text area found by the id attribute that contains
	 * the value of the passed parameter.
	 * 
	 * @param idTextArea
	 *            Id or id part of the text area to be cleaned.
	 */
	public void cleanTextAreaWithIdContaining(String idTextArea) {
		findTextAreaByIdContaining(idTextArea).clear();
	}

	/**
	 * Returns the contents of the attribute 'value' of element input found by
	 * its id.
	 * 
	 * @param idInput
	 * @return
	 * @return String - Contents of the attribute 'value' of element..
	 */
	public String getValueInputById(String idInput) {
		WebElement input = findInputByIdContaining(idInput);
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(input));
		return input.getAttribute("value");
	}

	/**
	 * Return the value of a select.
	 * 
	 * @param idCampo
	 * 
	 */
	public String getValueSelectById(String idCampo) {
		return new Select(findSelectByIdContaining(idCampo))
				.getFirstSelectedOption().getAttribute("value");
	}

	/**
	 * Returns the contents of the attribute 'value' of element text area found
	 * by its id.
	 * 
	 * @param idTextArea
	 * @return
	 * @return String - Contents of the attribute 'value' of element.
	 */
	public String getValueTextAreaById(String idTextArea) {
		WebElement element = findTextAreaByIdContaining(idTextArea);
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element.getAttribute("value");
	}

	/**
	 * Checks whether a <i>input</i> this with the readonly attribute equal to
	 * true.
	 * 
	 * @param idInput
	 * @return true if the <i>input</i> is readonly.
	 */
	public boolean checkAttributeStateReadOnlyInput(String idInput) {
		if (findInputByIdContaining(idInput) != null
				&& findInputByIdContaining(idInput).getAttribute("readonly") != null
				&& findInputByIdContaining(idInput).getAttribute("readonly")
						.equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether a <i>select</i> this with the readonly attribute equal to
	 * true.
	 * 
	 * @param idSelect
	 * @return true if the <i>select</i> is readonly.
	 */
	protected boolean testReadOnlySelect(String campo) {
		if (findSelectByIdContaining(campo) != null
				&& findSelectByIdContaining(campo).getAttribute("readonly") != null
				&& findSelectByIdContaining(campo).getAttribute("readonly")
						.equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Check if a <i> CheckBox </ li> css for this with the readonly attribute
	 * equal to true *.
	 * 
	 * @param value
	 * @return true caso o <i>input</i> esteja readonly
	 */
	protected boolean testReadOnlyCheckBoxPorCss(String value) {
		if (getDriver().findElement(By.cssSelector(value)) != null
				&& findElementByCssSelector(value).getAttribute("readonly") != null
				&& findElementByCssSelector(value).getAttribute("readonly")
						.equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Check if a <i> Select </ li> css for this with the readonly attribute
	 * equal to true *.
	 * 
	 * @param value
	 * @return true caso o <i>input</i> esteja readonly
	 */
	protected boolean testReadOnlySelectPorCss(String value) {
		if (findElementByCssSelector(value) != null
				&& findElementByCssSelector(value).getAttribute("readonly") != null
				&& findElementByCssSelector(value).getAttribute("readonly")
						.equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Arrow on input that contains the last id in the first parameter the value
	 * passed in the second parameter.
	 * 
	 * @param idInput
	 * @param value
	 */
	public void setInputValue(String idInput, String value) throws Exception {
		cleanInputWithIdContaining(idInput);
		findInputByIdContaining(idInput).sendKeys(value);
	}

	/**
	 * Arrow on textarea that contains the last id in the first parameter the
	 * value passed in the second parameter.
	 * 
	 * @param idTextArea
	 * @param value
	 */
	public void setTextAreaValue(String idTextArea, String value) {
		cleanTextAreaWithIdContaining(idTextArea);
		findTextAreaByIdContaining(idTextArea).sendKeys(value);
	}

	/**
	 * Defines the basic flow of a test case.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected abstract void basicFlow() throws Exception;

	/**
	 * It has all the methods related to the test to enter the ECT.
	 * 
	 * @throws Exception
	 */
	public abstract void testScenarioInsert() throws Exception;

	/**
	 * It has all the methods related to the test research of ECT.
	 * 
	 * @throws Exception
	 */
	public abstract void testScenarioSearch() throws Exception;

	/**
	 * It has all the methods related to the test edit ECT.
	 * 
	 * @throws Exception
	 */
	public abstract void testScenarioEdit() throws Exception;

	/**
	 * It has all the methods related to the test view of ECT.
	 * 
	 * @throws Exception
	 */
	public void testScenarioView() throws Exception {

	}

	/**
	 * It has all the methods related delete the ECT test.
	 * 
	 * @throws Exception
	 */
	public void testScenarioDelete() throws Exception {

	}

	/**
	 * Test method that performs a CRUD flow.
	 * 
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		basicFlow();
		testScenarioInsert();
		urlStartTest();
		testScenarioSearch();
		urlStartTest();
		testScenarioEdit();
	}

	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Returns the URL of the page that will be tested.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract void urlStartTest();

	/**
	 * Method capturing the test image.
	 * 
	 * @param testCase
	 * @param testMethod
	 * @param version
	 */
	protected void evidenceSelenium(String version, String testCase,
			String testMethod) throws Exception {
		File imageFile = (File) ((TakesScreenshot) getDriver())
				.getScreenshotAs(OutputType.FILE);
		String failureImageFileName = "C:\\" + version + "\\" + testCase + "\\"
				+ testMethod + ".png";
		File evidenciaSeleniumFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, evidenciaSeleniumFile);
	}

	/**
	 * Choose specific value in a select type element found by cssSelector in
	 * accordance with the passed parameters.
	 * 
	 * @param cssSelector
	 * @param textOptionPartial
	 */
	public void setSelectValuePartialText(String cssSelector,
			String textoParcial) {
		WebElement select = getDriver()
				.findElement(By.cssSelector(cssSelector));
		List<WebElement> listaDeOpcoes = select.findElements(By
				.tagName("option"));
		for (WebElement opcaoSelecionada : listaDeOpcoes) {
			if (opcaoSelecionada.getText().contains(textoParcial)) {
				opcaoSelecionada.click();
				break;
			}
		}
	}

	/**
	 * Method that waits for the page rendered
	 */
	public void waitForPageLoad() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver wdriver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		});
	}

	/**
	 * Finds and returns an element of type span that contains the text passed
	 * as parameter.
	 * 
	 * @param text
	 *            the element sought.
	 */
	public WebElement findSpanContainText(String text) {
		WebElement element = driver.findElement(By
				.xpath("//span[contains(text(),'" + text + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	/**
	 * Method that focus the input.
	 * 
	 * @param idInput
	 */
	public void onFocusInput(String idInput) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("$(\"input[id*='" + idInput + "']\").focus();", 4000);
	}

}