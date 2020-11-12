import java.util.Scanner;

public class addAccount {
	private Scanner scanner;
	private String userName;
	public addAccount(Scanner scanner, String userName) {
		this.scanner = scanner;
		this.userName = userName;
	}
	
	public boolean createNewAccount() {
		System.out.println("Please enter initial amount");
		double amount = scanner.nextDouble();
		bankLogic bank_logic = new bankLogic();
		return bank_logic.createAccount(userName, amount);
	}

}
