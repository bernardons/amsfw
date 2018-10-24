package com.unisys.br.amsfw.test.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unisys.br.amsfw.test.util.ConfigUtil;

/**
 * Class that must be inherited by all the automated tests. This class contains
 * all the methods for accessing and manipulating screen attributes.
 * 
 * @author RegoIanC
 * @author NevesAli
 * 
 */
public abstract class BaseSelenium {

	public static final int SLEEP_SELECT = 400;
	public static final int TIMEOUT_WAIT = 35;
	public static final long WAIT_RESPONSE_AJAX = 300;
	private static final int TIMEOUT_START = 30;
	private static String startUrl = "";
	private WebDriver driver = null;
	private static Browser BROWSER;
	private static boolean remote = false;
	private static String parameters;

	@Parameter
	public String browser;

	/**
	 * Get browsers in sistema.properties and add its in a list
	 * 
	 * @return
	 * @throws Exception
	 */
	@Parameters
	public static LinkedList<String[]> getEnvironments() throws Exception {

		String[] browsers = null;
		browsers = ConfigUtil.getPropertiesSistema("browser").split(",");
		LinkedList<String[]> env = new LinkedList<String[]>();

		for (int i = 0; i < browsers.length; i++)
			env.add(new String[] { browsers[i] });

		return env;
	}

	/**
	 * Defines what will be the URL that selenium will access.
	 * 
	 * @param urlProjeto
	 */
	public BaseSelenium(final String urlProjeto, final Browser browser,
			boolean isRemote) {
		startUrl = urlProjeto;
		BROWSER = browser;
		remote = isRemote;
	}

	/**
	 * Starts selenium.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Before
	public void seleniumStart() throws Exception {

		String address = System.getProperty("user.dir") + "\\drivers";

		if (remote) {
			DesiredCapabilities capability = null;
			String platform = ConfigUtil.getPropertiesSistema("platform") != null ? ConfigUtil
					.getPropertiesSistema("platform") : "ANY";

			if (BROWSER.equals(BROWSER.Firefox)) {
				capability = new DesiredCapabilities().firefox();
			} else if (BROWSER.equals(BROWSER.Chrome)) {
				capability = new DesiredCapabilities().chrome();
			} else if (BROWSER.equals(BROWSER.InternetExplorer)) {
				capability = new DesiredCapabilities().internetExplorer();
				capability
						.setCapability(
								InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
								true);
				capability.setCapability("ignoreZoomSetting", true);
			}

			capability.setCapability("platform", Platform.fromString(platform));
			driver = new RemoteWebDriver(new URL(
					ConfigUtil.getPropertiesSistema("url.hub")), capability);

		} else {
			if (BROWSER.equals(Browser.Chrome)) {
				System.setProperty("webdriver.chrome.driver", address
						+ "\\ChromeDriver.exe");
				driver = new ChromeDriver();
			} else if (BROWSER.equals(Browser.InternetExplorer)) {
				System.setProperty("webdriver.ie.driver", address
						+ "\\IEDriverServer.exe");
				DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
				dc.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				dc.setCapability("ignoreZoomSetting", true);
				driver = new InternetExplorerDriver(dc);
			} else if (BROWSER.equals(Browser.Phantomjs)) {
				ArrayList<String> cliArgsCap = new ArrayList<String>();
				DesiredCapabilities capabilities = DesiredCapabilities
						.phantomjs();
				cliArgsCap.add("--web-security=false");
				cliArgsCap.add("--ssl-protocol=any");
				cliArgsCap.add("--ignore-ssl-errors=true");
				capabilities.setCapability("takesScreenshot", true);
				capabilities.setJavascriptEnabled(true); // enabled by default
				capabilities.setCapability("acceptSslCerts", true);
				capabilities.setCapability(
						PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
				capabilities.setCapability(
						PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
						new String[] { "--logLevel=2" });
				capabilities
						.setCapability(
								PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
								address + "\\phantomjs.exe");
				this.driver = new PhantomJSDriver(capabilities);

			} else {
				DesiredCapabilities dc = DesiredCapabilities.firefox();
				dc.setCapability("marionette", true);
				System.setProperty("webdriver.gecko.driver", address
						+ "\\geckodriver.exe");
				// System.setProperty("webdriver.firefox.bin","C:/Program
				// Files/Mozilla Firefox/firefox.exe");
				driver = new FirefoxDriver();
			}
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(startUrl);
		driver.manage().timeouts()
				.implicitlyWait(TIMEOUT_START, TimeUnit.SECONDS);

		saveEnvironments();
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
	 * Awaits the ajax return.
	 */
	public void waitReturnAjax() {
		while (true) {
			Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) driver)
					.executeScript("return jQuery.active == 0");
			if (ajaxIsComplete) {
				break;
			}
			delay(100);
		}
	}

	/**
	 * Terminate selenium.
	 * 
	 * @throws Exception
	 */
	@After
	public void seleniumStop() throws Exception {
		if (driver != null) {
			driver.quit();
		}
	}

	/**
	 * Set environment parameters and save archive at resources folder.
	 */
	@AfterClass
	public static void saveEnvironments() {

		String FILENAME = System.getProperty("user.dir")
				+ "\\target\\allure-results\\";
		File file = new File(FILENAME);

		if (!file.exists()) {
			file.mkdir();
			// Get Browsers e Url
			String[] b = ConfigUtil.getPropertiesSistema("browser").split(",");
			for (int i = 1; i <= b.length; i++)
				createParameter("Browser " + i, b[i - 1]);
			createParameter("URL", startUrl);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			// Calendar cal = Calendar.getInstance();
			Date d = new Date();
			createParameter("Data", dateFormat.format(d));

			String environment = "<qa:environment xmlns:qa=\"urn:model.commons.qatools.yandex.ru\">"
					+ "<id>2a54c4d7-7d79-4615-b80d-ffc1107016a1</id>"
					+ "<name>Allure sample test pack</name>"
					+ parameters
					+ "</qa:environment>";

			BufferedWriter bw = null;
			FileWriter fw = null;

			try {

				fw = new FileWriter(FILENAME + "environment.xml", false);
				bw = new BufferedWriter(fw);
				bw.write(environment);

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Add environment parameters.
	 * 
	 * @param key
	 * @param value
	 */
	private static void createParameter(String key, String value) {
		parameters += "<parameter><name>" + key + "</name>" + "<key>" + key
				+ "</key>" + "<value>" + value + "</value></parameter>";
	}

	/**
	 * Method that identifies and click on an element by id passed as a
	 * parameter in the 'id' attribute.
	 * 
	 * @author NevesAli
	 * @param id
	 * 
	 */
	public void clickElementById(String id) {
		WebElement element = getDriver().findElement(By.id(id));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
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
	public void waitTitleContain(final String text) {
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
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	/**
	 * Finds and returns an element of type span that contains the text passed
	 * as a parameter.
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
	 * Finds and returns an element of type div that contains the text passed as
	 * a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findDivByIdContaining(String text) {
		WebElement element = driver.findElement(By.xpath("//div[contains(@id,'"
				+ text + "')]"));
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
		wait.until(ExpectedConditions.visibilityOf(element));
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
	 * Finds and returns an element of type span that contains the text passed
	 * as a parameter in the ID attribute.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findSpanByIdContaining(String text) {
		WebElement element = driver.findElement(By
				.xpath("//span[contains(@id,'" + text + "')]"));
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
	 * Finds and returns an element of type input that contains mask for CPF.
	 * 
	 * @param text
	 *            present in the ID attribute of the element sought.
	 */
	public WebElement findInputByIdContainingMaskCPF(String text) {
		findInputByIdContaining(text).sendKeys(Keys.CONTROL, Keys.LEFT);
		WebElement element = driver.findElement(By
				.xpath("//input[contains(@id,'" + text + "')]"));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
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
	public void setSelectValueById(String idSelect, String value) {
		new Select(findSelectByIdContaining(idSelect))
				.selectByVisibleText(value);
		delay(SLEEP_SELECT);
	}

	/**
	 * Choose specific value in a select type element found by cssSelector in
	 * accordance with the passed parameters.
	 * 
	 * @param cssSelector
	 * @param textOptionPartial
	 */
	public void setSelectValueByCssPartialText(String cssSelector,
			String textOptionPartial) {
		WebElement select = getDriver()
				.findElement(By.cssSelector(cssSelector));
		List<WebElement> listaDeOpcoes = select.findElements(By
				.tagName("option"));
		for (WebElement opcaoSelecionada : listaDeOpcoes) {
			if (opcaoSelecionada.getText().contains(textOptionPartial)) {
				opcaoSelecionada.click();
				break;
			}
		}
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
	public void setSelectValueByCss(String cssSelector, String value) {
		new Select(findElementByCssSelector(cssSelector))
				.selectByVisibleText(value);
		delay(SLEEP_SELECT);
	}

	/**
	 * It offers options with auto complete a field found by its ID.
	 * 
	 * @param textId
	 *            text or part of this text in the field ID.
	 * @param textOption
	 *            text or part of this text in the autocomplete options pursued
	 *            in the field.
	 */
	public void findInputAutoCompleteByIdContaining(String textId,
			String textOption) {
		WebElement element = driver.findElement(By
				.xpath("//input[contains(@id,'" + textId + "')]"));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOf(element));
		element.sendKeys("");
		element.sendKeys(textOption);
		delay(2200);
		element.sendKeys(Keys.TAB);
		delay(WAIT_RESPONSE_AJAX);
	}

	/**
	 * Select item from a picklist.
	 * 
	 * @param itemLabel
	 *            Label item is selected.
	 */
	public void selectItemPickList(String itemLabel) {
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
	 * Click on a page button using the css of the button as a parameter.
	 * 
	 * @param css
	 *            button when clicked.
	 */
	public void clickInputByCss(String css) {
		WebElement input = driver.findElement(By.cssSelector(css));
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(input));
		input.click();
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
	public void cleanInputWithIdContaining(String idInput) {
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
	public boolean testReadOnlySelect(String idSelect) {
		if (findSelectByIdContaining(idSelect) != null
				&& findSelectByIdContaining(idSelect).getAttribute("readonly") != null
				&& findSelectByIdContaining(idSelect).getAttribute("readonly")
						.equals("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether a <i>textarea</i> this with the readonly attribute equal
	 * to true.
	 * 
	 * @param idInput
	 * @return true if the <i>textarea</i> is readonly.
	 */
	public boolean checkAttributeStateReadOnlyTextArea(String idTextArea) {
		if (findTextAreaByIdContaining(idTextArea) != null
				&& findTextAreaByIdContaining(idTextArea).getAttribute(
						"readonly") != null
				&& findTextAreaByIdContaining(idTextArea).getAttribute(
						"readonly").equals("true")) {
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
	public void setInputValue(String idInput, String value) {
		cleanInputWithIdContaining(idInput);
		findInputByIdContaining(idInput).sendKeys(value);
	}

	public void clicaElementoPorIdJS(String idSubmit) {
		WebElement e = getDriver().findElement(By.id(idSubmit));
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_WAIT);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
				.id(idSubmit)));
		((JavascriptExecutor) getDriver()).executeScript(
				"arguments[0].click()", e);
	}

	public void SetaValorPorIdJS(String id, String valor) {
		JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
		myExecutor.executeScript("document.getElementById('" + id
				+ "').value='" + valor + "'");
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
	 * @throws Exception
	 */
	protected abstract void basicFlow() throws Exception;

	/**
	 * It has all the methods related to the test to enter the ECT.
	 * 
	 * @throws Exception
	 */
	protected abstract void testScenarioInsert() throws Exception;

	/**
	 * It has all the methods related to the test research of ECT.
	 * 
	 * @throws Exception
	 */
	protected abstract void testScenarioSearch() throws Exception;

	/**
	 * It has all the methods related to the test edit ECT.
	 * 
	 * @throws Exception
	 */
	protected abstract void testScenarioEdit() throws Exception;

	/**
	 * It has all the methods related to the test view of ECT.
	 * 
	 * @throws Exception
	 */
	protected abstract void testScenarioView() throws Exception;

	/**
	 * It has all the methods related delete the ECT test.
	 * 
	 * @throws Exception
	 */
	protected abstract void testScenarioDelete() throws Exception;

	/**
	 * Method capturing the test image.
	 * 
	 * @param testCase
	 * @param testMethod
	 * @param version
	 * @throws IOException
	 */
	protected void evidenceSelenium(String version, String testCase,
			String testMethod) throws IOException {
		File imageFile = (File) ((TakesScreenshot) getDriver())
				.getScreenshotAs(OutputType.FILE);
		String failureImageFileName = "C:\\" + version + "\\" + testCase + "\\"
				+ testMethod + ".png";
		File evidenciaSeleniumFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, evidenciaSeleniumFile);
	}

	/**
	 * Select date passed as a parameter in the datepicker element with id
	 * passed as a parameter.
	 * 
	 * @param idDatapicker
	 *            present in the ID attribute of the element sought.
	 * @param date
	 *            value to select the calendar
	 */
	public void selectDateInDatepickerPopupWithIdContaining(
			String idDatapicker, String date) {
		findInputByIdContaining(idDatapicker).click();
		clickLinkByLabel(date);
	}
}