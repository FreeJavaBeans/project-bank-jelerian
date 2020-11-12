import java.util.List;

public class bankLogic {

	public bankLogic() {
		
	}
	public String validateAccount(String userName, String password) {
		databaseAccess db = new databaseAccess();
		return db.validateUser(userName, password);
	}
	public boolean createAccount(String account, double amount) {
		databaseAccess db = new databaseAccess();
		return db.createAccount(account, amount);
	}
	public boolean addPerson(String firstName, String lastName, String address, String idType, String id, String state) {
		databaseAccess db = new databaseAccess();
		return db.addPerson(firstName, lastName, address, idType, id, state);
	}
	public boolean createUser(String userName, String password, boolean isEmployee) {
		databaseAccess db = new databaseAccess();
		return db.createUser(userName, password, isEmployee);
	}
	public boolean deposit(int accountNumber, double amount) throws TransactionLogUnavailableException {
		databaseAccess db = new databaseAccess();
		return db.deposit(accountNumber, amount);
	}
	public boolean withdraw(int accountNumber, double amount) throws TransactionLogUnavailableException {
		databaseAccess db = new databaseAccess();
		return db.withdraw(accountNumber, amount);
	}
	private boolean isEmployee(String userName){
		databaseAccess db = new databaseAccess();
		return db.isEmployee(userName);
	}
	public boolean displayTransactions(String userName) {
		if(!isEmployee(userName))
			return false;
		databaseAccess db = new databaseAccess();
		List<Transaction> transactions = db.getAllTransactions();
		System.out.println(transactions.size() + " transactions");
		for(int i = 0; i < transactions.size(); i++)
			System.out.println(transactions.get(i).toString());
		return true;
	}

	public boolean transfer(int accountNumber, int targetAccountNumber, double amount) throws TransactionLogUnavailableException {
		databaseAccess db = new databaseAccess();
		return db.transfer(accountNumber, targetAccountNumber, amount);
	}

}
