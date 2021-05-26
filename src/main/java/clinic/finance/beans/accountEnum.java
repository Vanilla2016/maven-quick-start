package clinic.finance.beans;

public enum accountEnum {

	//FIRSTDIRECT, CATERALLEN, TANDEM
	VIRGIN(1, 0.0, accountTypeEnum.ISA.toString(), "Virgin S&S", 0),
				MARCUS(2, 0.0, accountTypeEnum.CURRENT.toString(), "Marcus", 0);
		
	accountEnum(int accId, double balance, String type, String name, int interest){
		new BankAccount(accId, balance, type, name, interest);
	}
	
}
