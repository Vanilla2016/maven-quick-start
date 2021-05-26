package clinic.finance.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class CommonFinanceUtil {

	protected String USERKEY = "userName";
	protected String PASSKEY = "passWord";

	protected File propFile;
	protected FileReader propFileReader;
	protected Properties prop = null;
	protected String propsPrefix;
		
	protected void loadPropertiesFromResource(String filePath) {
		
		try {
			propFile = new File(filePath);
				propFileReader = new FileReader(propFile);
		    prop = new Properties();
		    prop.load(propFileReader);
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected abstract Properties getProperties();
}
