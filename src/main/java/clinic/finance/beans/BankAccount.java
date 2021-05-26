package clinic.finance.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankAccount {
	
	private int accountId = 0;

	private double accountBalance = 0;

	private String accountType = null;

	private String accountName = null;

	private int accountInterest = 0;

	private String accountCol = null;
	
	private String uri = null;
	
	/*
	 * Maintain List of updateable fields based on the 
	 * validation of parameter values
	 */
	private List <String> updateFields  =  new ArrayList<String>();
	
	public BankAccount (int accountId, double accountBalance, String accountType,
			String accountName , int accountInterest, String accountCol, String uri){
		
		this(accountId, accountBalance, accountType,
				accountName , accountInterest);
		
		if (accountCol != null) {
			this.accountCol = accountCol;
			updateFields.add("accountCol");
		}
		if (uri != null) {
			this.uri = uri;
			updateFields.add("uri");
		}
	}
	
	public BankAccount (int accountId, double accountBalance, String accountType,
			String accountName , int accountInterest){
		
		this(accountId, accountBalance);
		
		if (accountType != null) {
			this.accountType = accountType;
			updateFields.add("accountType");
		}
		if (accountName != null) {
			this.accountName = accountName;
			updateFields.add("accountName");
		}
		if (accountInterest > 0) {
			this.accountInterest = accountInterest;
			updateFields.add("accountInterest");
		}
	}
	
	public BankAccount (int accountId, double accountBalance) {
		
		if (accountId > 0) {
			this.accountId = accountId;
			updateFields.add("accountId");
		}
		if (accountBalance > 0) {
			this.accountBalance = accountBalance;
			updateFields.add("accountBalance");
		}
	}
	
	public List <String> getUpdateFields() {
		return updateFields; 
	}
	
	public int getAccountId() {
		return accountId;
	}
	public double getAccountBalance() {
		return accountBalance;
	}
	public String getAccountType() {
		return accountType;
	}
	public String getAccountName() {
		return accountName;
	}
	public int getAccountInterest() {
		return accountInterest;
	}
	public String getAccountCol() {
		return accountCol;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
