package clinic.finance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import clinic.finance.beans.BankAccount;

/*
 * Essential to use mysql-connector-java 8.0.11 jar.
 * Otherwise returned unrecognized protocol error or NullPointerException
 */
public class JDBCUtil extends CommonFinanceUtil{
	
	protected String URLKEY = "url";
	protected String DBMSKEY = "dbms";
	protected String SERVERKEY = "serverName";
	protected String PORTKEY = "portNumber";
	protected String DBNAMEKEY = "account";

	private Connection conn = null;
	private boolean connected = false;
	
	private final String UPDATSTATPRE = "update wds_fin.account ";
	/*
	 * properties Constructor
	 */
	public JDBCUtil (String propertiesPath) {
		propsPrefix = "jdbc.db.";
		loadPropertiesFromResource(propertiesPath);
	}
	
	@Override
	protected Properties getProperties() {
		return prop;
	}
	
	public Connection getConnection() throws SQLException, ClassNotFoundException {

	    Properties connectionProps = new Properties();
	    connectionProps.put("user", prop.getProperty(propsPrefix+USERKEY));
	    connectionProps.put("password", prop.getProperty(propsPrefix+PASSKEY));
	    connectionProps.put("useUnicode", "yes");
	    
	    if (prop.getProperty(propsPrefix+DBMSKEY).equals("mysql")) {
	    	 conn = DriverManager.getConnection("jdbc:" + prop.getProperty(propsPrefix+DBMSKEY) + "://" +
	    			 prop.getProperty(propsPrefix+SERVERKEY) +
	                   ":" + prop.getProperty(propsPrefix+PORTKEY) + "/",
	                   connectionProps);
	    } else if (prop.getProperty(propsPrefix+DBMSKEY).equals("derby")) {
	        conn = DriverManager.getConnection(
	                   "jdbc:" + prop.getProperty(propsPrefix+DBMSKEY) + ":" +
	                		   prop.getProperty(propsPrefix+DBNAMEKEY) +
	                   ";create=true",
	                   connectionProps);
	    }
	    if (conn !=  null) connected = true;
	    	
	    return conn;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public ResultSet runSelectStatement() {
		String query = "select * from wds_fin.account";
		ResultSet rs = null;
		if (connected) {
			try (Statement stmt = conn.createStatement()) {
		      rs = stmt.executeQuery(query);
		     
		    } catch (SQLException e) {
		    	e.getStackTrace();
		    }
		}
		return rs;
	}
	
	/*
	 * Update relevant value in DB table
	 */
	public int runAccountUpdateStatement(BankAccount account) {
		
		StringBuilder queryBuild = new StringBuilder(UPDATSTATPRE); 
		List feildNames = account.getUpdateFields();
		
		/*Conditional as cannot update row without id*/
		if(feildNames.contains("accountId")) {
			Stream<?> fieldStream = feildNames.stream();
			
			fieldStream.filter(t -> !t.toString()
					.equalsIgnoreCase("accountId"))
						.forEach(new Consumer() {
							@Override
							public void accept(Object t) {
								queryBuild.append("set ");
								queryBuild.append(t.toString());
								queryBuild.append(" = ");
								
								if (t.toString().equalsIgnoreCase("accountBalance")) {
									queryBuild.append(account.getAccountBalance());
									queryBuild.append(" ");
								}
							}
						});
		}
		queryBuild.append("where idaccount = ");
		queryBuild.append(account.getAccountId());
		
		int returnCode = 0;
		
		if (connected) {
			try (Statement stmt = conn.createStatement()) {
				returnCode = stmt.executeUpdate(queryBuild.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnCode;
	}
}
