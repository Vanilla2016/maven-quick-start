package clinic.programming.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import clinic.finance.beans.BankAccount;
import clinic.finance.util.JDBCUtil;

public class JDBCUtilTest {
	
	private JDBCUtil jdbcUtil; 
	
	public static final String propertiesLocation = "src\\test\\resources\\jdbc.properties";
	
	 @Before
    public void setup() throws IOException, URISyntaxException, ClassNotFoundException {
		 jdbcUtil =  new JDBCUtil(propertiesLocation);
			try {
				jdbcUtil.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }
	
	@Test
	public void testDBConnection() throws ClassNotFoundException {
		assertTrue(jdbcUtil.isConnected());
	}
	
	@Test
	public void testRunDataSelect() {
		assertNotNull(jdbcUtil.runSelectStatement());
	}
	
	@Test
	public void testRunUpdateStatement() {
		BankAccount updateAcc = new BankAccount(1, 202.20);
		assertEquals(new Long(1), new Long(jdbcUtil.runAccountUpdateStatement(updateAcc)));
	}
}
