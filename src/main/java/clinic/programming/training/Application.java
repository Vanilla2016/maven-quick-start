package clinic.programming.training;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import clinic.webriver.chrome.WebDriverUtil;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Application {

	public Application() {
		System.out.println ("Inside Application");
	}
	
    // method main(): ALWAYS the APPLICATION entry point
    public static void main (String[] args) {
    	
    	final String propertiesLocation = "src\\main\\resources\\docelement.properties";
    	WebDriverUtil driverUtil = null;
    	
    	driverUtil = new WebDriverUtil(propertiesLocation);
    	
		if (args[0] != null && args[0].length() > 0) {
    		driverUtil.initializeChromeWebDriver(args[0]);
    		driverUtil.logIntoSite();
    	}
    }
}
