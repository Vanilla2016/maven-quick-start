package clinic.webriver.chrome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtil {

	private WebDriver driver = null;
	
	private final String USERNAMEKEY = "virgin.login.username";
	private final String PINKEY = "virgin.login.pin";
	private final String LOGINBOXKEY = "virgin.loginbox.id";
	private final String PINBOXKEY = "virgin.pinbox.id";
	private final String LOGINKEY = "virgin.login.id";
	private final String SUMMARYCLASSNAME = "summary.class.name";

	private String userName;
	private String pin;
	private String virginLoginBoxId; 
	private String virginPinBoxId;
	private String virginLoginId;
	
	private String summaryClassName;

	private BigDecimal valuationSummary;

	private Properties loadPropertiesFromResource(String filePath) {
	   
		File propFile;
		FileReader propFileReader;
		Properties prop = null;
		
		try {
			propFile = new File(filePath);
				propFileReader = new FileReader(propFile);
		    prop = new Properties();
		    prop.load(propFileReader);
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}
	
	/*
	 * properties constructor
	 */
	public WebDriverUtil (String propertiesPath) {
		setSiteProperties(loadPropertiesFromResource(propertiesPath));
	}
	
	
	public void setSiteProperties (Properties prop) {
		
		if (prop!=null) {
			userName = prop.getProperty(USERNAMEKEY);
			pin = prop.getProperty(PINKEY);
			virginLoginBoxId = prop.getProperty(LOGINBOXKEY);
			virginPinBoxId = prop.getProperty(PINBOXKEY);
			virginLoginId = prop.getProperty(LOGINKEY);
			summaryClassName = prop.getProperty(SUMMARYCLASSNAME);
		}
	}
	
	public void initializeChromeWebDriver(String url) {
			
			WebDriverManager.chromedriver().browserVersion("90.0.4430.24").setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized"); 
			options.addArguments("enable-automation"); 
			options.addArguments("--no-sandbox"); 
			options.addArguments("--disable-infobars");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-browser-side-navigation"); 
			options.addArguments("--disable-gpu"); 
			driver = new ChromeDriver(options); 
			driver.get(url);
		}
	
	public void quitChromeWebDriver() {
		driver.quit();
	}
	
	public WebElement getDocElement(String docElementId) {
		return(driver.findElement(By.id(docElementId)));
	}

	public List <WebElement> getDocElementsByClass(String docElementClass) {
		return(driver.findElements(By.className(docElementClass)));
	}

	public void populateDocElement(String docElementId, String charString) {
		driver.findElement(By.id(docElementId)).sendKeys(charString);
	}
	
	public String getDriverURL() {
		return driver.getCurrentUrl();
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public void logIntoSite() {
		 
		 populateDocElement(virginLoginBoxId, userName);
	   	 populateDocElement(virginPinBoxId, pin);
	   	 WebElement submitButton = getDocElement(virginLoginId);
	   	 /*
	   	  * This seems to implicitly operate a get, 
	   	  * as the method blocks until the Summary screen is returned
	   	  */
	   	 submitButton.click();
	}
	
	/*
	 * Parses a value from the element with a £ symbol
	 */
	public BigDecimal getValuationSummary () {
		
		List <WebElement> valuationSummaryElems = getDocElementsByClass(summaryClassName);
		for(WebElement element : valuationSummaryElems) {
			String elementText = element.getText();
			if (elementText.contains("£")) {
				elementText = elementText.substring(elementText.indexOf("£")+1 , elementText.indexOf(","))
						+ elementText.substring(elementText.indexOf(",")+1);
				valuationSummary = new BigDecimal(elementText);

			}
		};
		return valuationSummary;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPin() {
		return pin;
	}

	public String getVirginLoginBoxId() {
		return virginLoginBoxId;
	}

	public String getVirginPinBoxId() {
		return virginPinBoxId;
	}
	
	public String getVirginLoginId() {
		return virginLoginId;
	}
}
