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

import clinic.finance.beans.BankAccount;
import clinic.finance.beans.accountEnum;
import clinic.finance.util.JDBCUtil;
import clinic.finance.util.WebDriverUtil;
import clinic.programming.jdbc.JDBCUtilTest;
import clinic.programming.wiremock.WireMockRuleUnMatchStubs;

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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

public class WebDriverUtilTest {
    
	private static WebDriverUtil driverUtil = null;
	private static JDBCUtil jdbcUtil = null;
	
	private final String propertiesLocation = "src\\test\\resources\\test.properties";
	private final String VIRGINSTUBSUMMARYURI = "summaryUri";

	private final double summaryValue = 19362.64;
	
    @Rule
    public WireMockRuleUnMatchStubs wireMockRule = new WireMockRuleUnMatchStubs(8089);
    
    String virginMoneyHTML = "";
    String virginSummaryHTML = "";
    
    String marcusHTML = "";
    String callenHTML = "";
    String callenPacHTML = "";
    
    @Before
    public void setup() throws IOException, URISyntaxException {
    	driverUtil =  new WebDriverUtil(propertiesLocation);
    	
    	jdbcUtil =  new JDBCUtil(JDBCUtilTest.propertiesLocation);
    	
			try {
					jdbcUtil.getConnection();
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
    	
    	/*Mock URL returns as HTML stub files*/
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

    	callenHTML = returnHTMLResource("callen");
    	wireMockRule.stubFor(get(urlEqualTo("/callen"))
    			.willReturn(aResponse()
    					.withStatus(200)
    					.withHeader("Content-Type", "text/html")
    					.withBody(callenHTML)));
    	
    	callenPacHTML = returnHTMLResource("callenpac");
    	wireMockRule.stubFor(get(urlEqualTo("/callenpac"))
    			.willReturn(aResponse()
    					.withStatus(200)
    					.withHeader("Content-Type", "text/html")
    					.withBody(callenPacHTML)));

    	/*
    	marcusHTML = returnHTMLResource("marcus");
    	wireMockRule.stubFor(get(urlEqualTo("/marcus"))
    			.willReturn(aResponse()
    					.withStatus(200)
    					.withHeader("Content-Type", "text/html")
    					.withBody(marcusHTML)));
    	 */

    }
    
   @Ignore
    @Test
    public void testLoadResourceAsUrl() {
    	driverUtil.setPropsPrefix(accountEnum.VIRGIN.getPropsPrefix());
    	driverUtil.initializeChromeWebDriver(accountEnum.VIRGIN.getloginUriProp(), false);
    }
   
   @Ignore
    @Test
    /*
     * initializeChromeWebDriver param had been set to virginHTML - 
     * 			the actual path to the file, not the URL configured to return it
     */
    public void testFocusLoginBoxes() {
    	WebElement loginBox = null;
    	for (accountEnum accountName : accountEnum.values()) {
    			driverUtil.setPropsPrefix(accountName.getPropsPrefix());
    			driverUtil.initializeChromeWebDriver(accountName.getloginUriProp(), false);
    			loginBox = driverUtil.getDocElement(driverUtil.getLoginBoxId());
    			assertNotNull(loginBox);
    	}
    }
     
    
     @Test
     public void testLogInInputFields () throws InterruptedException {
    	 for (accountEnum accountName : accountEnum.values()) {
    			
    		 	 driverUtil.setPropsPrefix(accountName.getPropsPrefix());
		    	 driverUtil.initializeChromeWebDriver(accountName.getloginUriProp(), false);
		    	 driverUtil.logIntoSite(accountName.name(), accountName.getloginUriProp());

		    	 if(accountName.name().equalsIgnoreCase(accountEnum.VIRGIN.toString())){
		    		 assertTrue(driverUtil.getPageTitle().equalsIgnoreCase(accountEnum.VIRGIN.getSummaryScreenTitle()));
		    	 }else if(accountName.name().equalsIgnoreCase(accountEnum.CATERALLEN.toString())){
    	 
		    		 //System.out.println(driverUtil.getPageTitle());
		    		 //System.out.println(accountEnum.CATERALLEN.getSummaryScreenTitle());
    	 			 driverUtil.setPropsPrefix(accountEnum.CATERALLEN.getPropsPrefix());
		    		 driverUtil.initializeChromeWebDriver(accountEnum.CATERALLEN.getPACTitle(), false);
		    		 driverUtil.logIntoSite(accountEnum.CATERALLEN.toString(), accountEnum.CATERALLEN.getPACTitle());
		    		 assertTrue(driverUtil.getPageTitle().equalsIgnoreCase(accountEnum.CATERALLEN.getSummaryScreenTitle()));
		    	 }
		    	 
    	 	}
    }
     
    
     @Ignore
     @Test
     public void testReturnSummaryFromVirginAccountScreen () {
    	 driverUtil.setPropsPrefix(accountEnum.VIRGIN.getPropsPrefix());
    	 driverUtil.initializeChromeWebDriver(VIRGINSTUBSUMMARYURI, false);
    	 BigDecimal valuationSummary = driverUtil.getValuationSummary();
    	 assertTrue(valuationSummary.doubleValue() == accountEnum.VIRGIN.getBalance());
    	 
    	 if (valuationSummary != null) {
    		 assertEquals(new Long(1), new Long(
    				 jdbcUtil.runAccountUpdateStatement(
						 new BankAccount(1, valuationSummary.doubleValue())
						 )
    				 ));
    	 }
    }
  
   @After
     public void tearDown() {
    	 driverUtil.quitChromeWebDriver();
     }
}
