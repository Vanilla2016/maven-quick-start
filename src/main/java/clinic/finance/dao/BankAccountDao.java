package clinic.finance.dao;

import java.util.List;
import clinic.finance.beans.BankAccount;

public interface BankAccountDao {
	   
	public List<BankAccount> getAllAccounts();
	   public BankAccount getAccount(int accountNo);
	   public void updateAccount(BankAccount accountNo);
	   public void deleteAccount(BankAccount accountNo);
	}
