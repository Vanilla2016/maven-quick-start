package clinic.programming.training;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import clinic.finance.beans.BankAccount;
import clinic.finance.beans.accountEnum;
import clinic.finance.util.JDBCUtil;
import clinic.finance.util.WebDriverUtil;

public class Application {

	public Application() {
	
	}
	
    // method main(): ALWAYS the APPLICATION entry point
    public static void main (String[] args) throws InterruptedException {
    	
    	WebDriverUtil driverUtil = null;
    	JDBCUtil jdbcUtil = null;

    	final int WAIT_TIME = 3000;
    	final String VIRGINPREFIX = "virgin.login.";
    	
    	final String propertiesLocation = "src\\main\\resources\\docelement.properties";
    	final String jdbcpropertiesLocation = "src\\main\\resources\\jdbc.properties";
    	
    	BigDecimal accountValuation=null;
    	
    	driverUtil = new WebDriverUtil(propertiesLocation);
    	jdbcUtil = new JDBCUtil(jdbcpropertiesLocation);
    	
    	try {
				jdbcUtil.getConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//if (args[0] != null && args[0].length() > 0) {
			
		for (accountEnum accountName : accountEnum.values()) {
			
			driverUtil.setPropsPrefix(accountName.getPropsPrefix());
			System.out.println(accountName.getloginUriProp());
	    	driverUtil.initializeChromeWebDriver(accountName.getloginUriProp(), true);
	    	
	    	/*
	    	Thread.sleep(WAIT_TIME);//Wait for load
	    	if(accountName.name().equalsIgnoreCase(accountEnum.CATERALLEN.toString())){
	       	 
	 			 driverUtil.setPropsPrefix(accountEnum.CATERALLEN.getPropsPrefix());
	 			 
	 			 WebDriver driver = driverUtil.getDriver();
	 			 
	 			 String MainWindow=driver.getWindowHandle();		
	        		
	 	        // To handle all new opened window.				
	 	        Set<String> s1=driver.getWindowHandles();		
	 	        Iterator<String> i1=s1.iterator();		
	 	        		
	 	        while(i1.hasNext())			
	 	        {		
	 	            String ChildWindow=i1.next();		
	 	            		
	 	            if(!MainWindow.equalsIgnoreCase(ChildWindow))			
	 	            {    		
	 	                 
	 	                    // Switching to Child window
	 	                    driver.switchTo().window(ChildWindow);	                                                                                                           
	 	                  //  driver.findElement(By.name("emailid"))
	 	                  //  .sendKeys("gaurav.3n@gmail.com");                			
	 	                    
	 	                 //   driver.findElement(By.name("btnLogin")).click();			
	 	                                 
	 				// Closing the Child Window.
	 	                        driver.close();		
	 	            }		
	 	        }		

	 	        driverUtil.logIntoSite(accountName.name(), accountName.getloginUriProp());
	 	        // Switching to Parent window i.e Main Window.
	 	            driver.switchTo().window(MainWindow);	
	    		 //driverUtil.initializeChromeWebDriver(accountEnum.CATERALLEN.getPACTitle(), true);
	    		 driverUtil.logIntoSite(accountEnum.CATERALLEN.toString(), accountEnum.CATERALLEN.getPACTitle());
	    		 Thread.sleep(3000);//Wait for load
	    	 }
	    	 */

	    	driverUtil.logIntoSite(accountName.name(), accountName.getloginUriProp());
	    	Thread.sleep(3000);//Wait for load
 			
    		accountValuation = driverUtil.getValuationSummary();
    		
    		if(accountValuation!= null ) {
    			int updateCode = jdbcUtil.runAccountUpdateStatement(
    					new BankAccount(1, accountValuation.doubleValue())
    			);
    			
    			if(updateCode != 0) {
        			driverUtil.logOutOfSite();
        		}
    		}
    		driverUtil.quitChromeWebDriver();
    	}
	  //}
    }
}
