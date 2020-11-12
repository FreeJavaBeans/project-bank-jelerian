import java.util.Scanner;

public class transaction {
	private Scanner scanner;
	private String username;
	
	public transaction(Scanner scanner, String username) {
		this.scanner = scanner;
		this.username = username;
	}

	public boolean deposit() {
		System.out.println("Account number?");
		int accountNumber = scanner.nextInt();
		System.out.println("Amount?");
		double amount = scanner.nextDouble();
		
		if(amount < 0.0)
			return false;
		bankLogic bank_logic = new bankLogic();
		return bank_logic.deposit(accountNumber, amount);
	}

}
