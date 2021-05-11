package clinic.programming.training;

import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import clinic.programming.wiremock.WireMockRuleUnMatchStubs;
import clinic.webriver.chrome.WebDriverUtil;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WebDriverUtilTest {
    
	private static WebDriverUtil driverUtil = null;

	private final String propertiesLocation = "src\\test\\resources\\test.properties";
	
	private final String stubLoginUri = "http://127.0.0.1:8089/virginmoney";
	private final String stubSummaryUri = "http://127.0.0.1:8089/virginsummary";
	
	private final String summaryScreenTitle = "Error 404";
	private final double summaryValue = 19362.64;
	
    @Rule
    public WireMockRuleUnMatchStubs wireMockRule = new WireMockRuleUnMatchStubs(8089);
    
    String virginMoneyHTML = "";
    String virginSummaryHTML = "";
    
    @Before
    public void setup() throws IOException, URISyntaxException {
    	driverUtil =  new WebDriverUtil(propertiesLocation);
    }
    
	
    /*
     * Return stubbed HTML from specified stub files
     */
    private String returnHTMLResource (String htmlStubName) throws URISyntaxException, IOException {
    	
    	htmlStubName += ".html";
    	Path catPath = Paths.get(getClass().getClassLoader().getResource(htmlStubName).toURI());
		StringBuilder catData = new StringBuilder();
		Stream<String> catLines = Files.lines(catPath);
		catLines.forEach(line -> catData.append(line).append("\n"));
		catData.deleteCharAt(catData.length()-1);
		catLines.close();
		return catData.toString();
	}
    
    @Before
    public void loadResourceAsURL() throws IOException, URISyntaxException, WebDriverException{
    	
    	//Mock URL return as HTML stub files
    	virginMoneyHTML = returnHTMLResource("virginmoney");
    	wireMockRule.stubFor(get(urlEqualTo("/virginmoney"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/html")
						.withBody(virginMoneyHTML)));

    	virginSummaryHTML = returnHTMLResource("virginsummary");
    	wireMockRule.stubFor(get(urlEqualTo("/virginsummary"))
    			.willReturn(aResponse()
    					.withStatus(200)
    					.withHeader("Content-Type", "text/html")
    					.withBody(virginSummaryHTML)));
    }
    
   
    @Test
    public void testLoadResourceAsUrl() {
    	driverUtil.initializeChromeWebDriver(stubLoginUri);
    	
    }
    
    @Test
    /*
     * initializeChromeWebDriver param had been set to virginHTML - 
     * 			the actual path to the file, not the URL configured to return it
     */
    public void testFocusLoginBoxes() {
    	WebElement loginBox = null;
    	driverUtil.initializeChromeWebDriver(stubLoginUri);
    	loginBox = driverUtil.getDocElement(driverUtil.getVirginLoginBoxId());
    	assertNotNull(loginBox);
    }
     
     @Test
     public void testLogInInputFields () {
    	 driverUtil.initializeChromeWebDriver(stubLoginUri);
    	 driverUtil.logIntoSite();
    	 assertTrue(driverUtil.getPageTitle().equalsIgnoreCase(summaryScreenTitle));
     }
     
     @Test
     public void testSummaryScreen () {
    	 driverUtil.initializeChromeWebDriver(stubSummaryUri);
    	 BigDecimal valuationSummary = driverUtil.getValuationSummary();
    	 assertTrue(valuationSummary.doubleValue() == summaryValue);
    }
     
     
   @After
     public void tearDown() {
    	 driverUtil.quitChromeWebDriver();
     }
     
}
