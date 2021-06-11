package clinic.finance.beans;

public enum accountEnum {

	//FIRSTDIRECT, CATERALLEN, TANDEM
	VIRGIN(1, 19362.64, accountTypeEnum.ISA.toString(), "Virgin S&S", 0, "virgin.login.", "loginUri", "virginMoneyHTML", "Error 404");
	//,
				//MARCUS(2, 0.0, accountTypeEnum.CURRENT.toString(), "Marcus", 0);
	//CATERALLEN(3, 0.0, accountTypeEnum.BUSINESS.toString(), "CaterAllen", 0, "callen.login.", "loginUri", "callenHTML", "Welcome to Cater Allen Private Bank");
		
	private String loginUriProp; 
	private String propsPrefix; 
	private String summaryScreenTitle; 
	private String PACTitle = "pacUri"; 
	private double balance; 
	
	accountEnum(int accId, double balance, String type, String name, int interest, String propsPrefix, String loginUriProp, String mockPageName, String summaryScreenTitle){
		this.loginUriProp = loginUriProp;
		this.propsPrefix = propsPrefix;
		this.summaryScreenTitle = summaryScreenTitle;
		this.balance = balance;
		
		new BankAccount(accId, balance, type, name, interest, propsPrefix, loginUriProp, mockPageName, summaryScreenTitle);
	}
	
	public String getloginUriProp() {
		return loginUriProp;
	}

	public String getPropsPrefix() {
		return propsPrefix;
	}

	public String getSummaryScreenTitle() {
		return summaryScreenTitle;
	}
	public double getBalance() {
		return balance;
	}
	public String getPACTitle() {
		return PACTitle;
	}
}
