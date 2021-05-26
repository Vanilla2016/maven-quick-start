package clinic.programming.training;

import java.math.BigDecimal;
import java.sql.SQLException;

import clinic.finance.beans.BankAccount;
import clinic.finance.util.JDBCUtil;
import clinic.finance.util.WebDriverUtil;

public class Application {

	public Application() {
	
	}
	
    // method main(): ALWAYS the APPLICATION entry point
    public static void main (String[] args) throws InterruptedException {
    	
    	WebDriverUtil driverUtil = null;
    	JDBCUtil jdbcUtil = null;

    	final String VIRGINPREFIX = "virgin.login.";
    	
    	final String propertiesLocation = "src\\main\\resources\\docelement.properties";
    	final String jdbcpropertiesLocation = "src\\main\\resources\\jdbc.properties";
    	
    	BigDecimal accountValuation;
    	
    	driverUtil = new WebDriverUtil(propertiesLocation);
    	jdbcUtil = new JDBCUtil(jdbcpropertiesLocation);
    	
    	try {
				jdbcUtil.getConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (args[0] != null && args[0].length() > 0) {
			
			driverUtil.setPropsPrefix(VIRGINPREFIX);
    		driverUtil.initializeChromeWebDriver(args[0]);
    		driverUtil.logIntoSite();
    		
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
    }
}
