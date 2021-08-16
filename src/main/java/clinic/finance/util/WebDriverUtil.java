package clinic.finance.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import clinic.finance.beans.accountEnum;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtil extends CommonFinanceUtil{

	private WebDriver driver = null;
	
	protected String PINKEY = "pin";
	private final String LOGINKEY = "id";
	protected String LOGINBUTTONKEY = "login";
	protected String LOGOUTBUTTONKEY = "logout";
	protected String LOGINBOXKEY = "loginBox";
	protected String PINBOXKEY = "pinBox";
	protected String USERPACKEY = "pac";
	
	private final String SUMMARYCLASSNAME = "summary.class.name";
	private BigDecimal valuationSummary;
	private String virginLoginBoxId; 
	/*
	 * properties constructor
	 */
	public WebDriverUtil (String propertiesPath) {
		loadPropertiesFromResource(propertiesPath);
	}
	
	@Override
	protected Properties getProperties() {
		return prop;
	}
	
	public void setPropsPrefix(String prefix) {
		propsPrefix = prefix;
	}
	
	public void initializeChromeWebDriver(String uriKey, boolean liveContext) {
			
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
			//if (liveContext) {
			//	driver.get(uriKey);
			//}else {
				driver.get(prop.getProperty(propsPrefix+uriKey));
		//	}
		}
	
	public void quitChromeWebDriver() {
		driver.quit();
	}
	
	public WebElement getDocElement(String docElementId) {
		return(driver.findElement(By.id(docElementId)));
	}

	public WebElement getDocElementByName(String docElementId) {
		return(driver.findElement(By.name(docElementId)));
	}

	public WebElement getDocElementByClass(String docElementClass) {
		return(driver.findElement(By.className(docElementClass)));
	}

	public List <WebElement> getDocElementsByClass(String docElementClass) {
		return(driver.findElements(By.className(docElementClass)));
	}

	public void populateDocElement(String docElementId, String charString) {
		driver.findElement(By.id(docElementId)).sendKeys(charString);
	}
	
	public String getPageTitle() {
		return driver.getTitle();
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	/*
	 * siteName param as different login screens feature different elements
	 */

	public void logIntoSite(String siteName, String navPos) throws InterruptedException {
		
		WebElement submitButton  = null;
		boolean submit = false;
		
		if (siteName.equalsIgnoreCase(accountEnum.VIRGIN.toString())) {
		
			populateDocElement(prop.getProperty(accountEnum.VIRGIN.getPropsPrefix()+LOGINBOXKEY),
					prop.getProperty(accountEnum.VIRGIN.getPropsPrefix()+USERKEY));
			populateDocElement(prop.getProperty(accountEnum.VIRGIN.getPropsPrefix()+PINBOXKEY), 
					prop.getProperty(accountEnum.VIRGIN.getPropsPrefix()+PINKEY));
			submitButton = getDocElement(prop.getProperty(propsPrefix+LOGINBUTTONKEY));
		if (submitButton != null) submit=true;
		
		}
		
		else if (siteName.equalsIgnoreCase(accountEnum.CATERALLEN.toString())) {
			if (navPos.equalsIgnoreCase("pacURi")) {//PAC screen
				char [] pinCode = prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+PINKEY).toCharArray();
				int pacPos=1;
				for(char pinDigit : pinCode){
					
					populateDocElement(prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+USERPACKEY)+pacPos, 
							String.valueOf(pinDigit));
					pacPos++;
				}
				populateDocElement(prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+PINBOXKEY), 
												prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+PASSKEY));
				submitButton = getDocElement(prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+LOGINBUTTONKEY));
				if (submitButton != null) submit=true;
			}else {
				 // Switching to Alert        
		       // Alert alert = driver.switchTo().alert();		
		        		
		        // Capturing alert message.    
		      //  String alertMessage= driver.switchTo().alert().getText();		
		        		
		        // Displaying alert message		
		   //     System.out.println(alertMessage);
		        Thread.sleep(5000);
		    	populateDocElement(prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+LOGINBOXKEY), 
												prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+USERKEY));
				submitButton = getDocElement(prop.getProperty(accountEnum.CATERALLEN.getPropsPrefix()+LOGINBUTTONKEY));
				if (submitButton != null) submit=true;
			}
		}
	
		/*
		 * This seems to implicitly operate a get, 
		 * as the method blocks until the Summary screen is returned
		 */
		if (submit)	submitButton.click();
	}
	
	/*
	 * Parses a value from the element with a £ symbol
	 */
	public BigDecimal getValuationSummary () {
		
		List <WebElement> valuationSummaryElems = getDocElementsByClass(prop.getProperty(SUMMARYCLASSNAME));
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
	
	public void logOutOfSite() {
		 WebElement logoutButton = getDocElementByName(prop.getProperty(propsPrefix+LOGOUTBUTTONKEY));
		 logoutButton.click();
		 
		 logoutButton = getDocElementByName(prop.getProperty(propsPrefix+LOGOUTBUTTONKEY));
		 logoutButton.click();
	}
	
	public String getLoginBoxId() {
		return prop.getProperty(propsPrefix+LOGINBOXKEY);
	}
}
