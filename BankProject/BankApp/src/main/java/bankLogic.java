
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
	public boolean deposit(int accountNumber, double amount) {
		databaseAccess db = new databaseAccess();
		return db.deposit(accountNumber, amount);
	}
}
