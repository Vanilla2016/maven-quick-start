package clinic.finance.dao;

import java.util.ArrayList;
import java.util.List;

import clinic.finance.beans.accountTypeEnum;
import clinic.finance.beans.BankAccount;

public class BankAccountDaoImpl implements BankAccountDao {

	//list is working as a database
	   List<BankAccount> students;

	   public BankAccountDaoImpl(){
	      students = new ArrayList<BankAccount>();
	      BankAccount account1 = new BankAccount(1, 0.0, "CURRENT", "Virgin S&S", 0);
	      students.add(account1);
	   }
	   
	@Override
	public List<BankAccount> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankAccount getAccount(int accountNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAccount(BankAccount accountNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAccount(BankAccount accountNo) {
		// TODO Auto-generated method stub

	}

}
