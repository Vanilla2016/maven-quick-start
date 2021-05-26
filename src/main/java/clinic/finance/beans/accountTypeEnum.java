package clinic.finance.beans;

public enum accountTypeEnum {
	
	CURRENT, ISA, SAVING, STOCK, FUND;
	
	private String type;
	
	String type() {
		return type;
	}
}
