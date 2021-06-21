package clinic.programming.training;

import java.math.BigDecimal;
import java.sql.SQLException;

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
	    	driverUtil.initializeChromeWebDriver(accountName.getloginUriProp(), false);
	    	driverUtil.logIntoSite(accountName.name(), accountName.getloginUriProp());
	    	
	    	Thread.sleep(WAIT_TIME);//Wait for load
/*
 * 
	    	if(accountName.name().equalsIgnoreCase(accountEnum.CATERALLEN.toString())){
	       	 
	 			 driverUtil.setPropsPrefix(accountEnum.CATERALLEN.getPropsPrefix());
	    		 driverUtil.initializeChromeWebDriver(accountEnum.CATERALLEN.getPACTitle(), false);
	    		 driverUtil.logIntoSite(accountEnum.CATERALLEN.toString(), accountEnum.CATERALLEN.getPACTitle());
	    		 Thread.sleep(3000);//Wait for load
	    	 }
 */
			
			
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
