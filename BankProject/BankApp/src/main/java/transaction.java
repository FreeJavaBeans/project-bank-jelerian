import java.util.Scanner;

public class transaction {
	private Scanner scanner;
	private String username;
	
	public transaction(Scanner scanner, String username) {
		this.scanner = scanner;
		this.username = username;
	}

	public boolean deposit() throws TransactionLogUnavailableException {
		System.out.println("Account number?");
		int accountNumber = scanner.nextInt();
		System.out.println("Amount?");
		double amount = scanner.nextDouble();
		
		if(amount < 0.0)
			return false;
		bankLogic bank_logic = new bankLogic();
		return bank_logic.deposit(accountNumber, amount);
	}

	public boolean withdraw() throws TransactionLogUnavailableException {
		System.out.println("Account number?");
		int accountNumber = scanner.nextInt();
		System.out.println("Amount?");
		double amount = scanner.nextDouble();
		
		if(amount < 0.0)
			return false;
		bankLogic bank_logic = new bankLogic();
		return bank_logic.withdraw(accountNumber, amount);
	}

	public boolean displayTranactions() {
		bankLogic bank_logic = new bankLogic();
		return bank_logic.displayTransactions(username);
	}

	public boolean transfer() throws TransactionLogUnavailableException {
		System.out.println("Source Account number?");
		int accountNumber = scanner.nextInt();
		System.out.println("Target Account number?");
		int targetAccountNumber = scanner.nextInt();
		System.out.println("Amount?");
		double amount = scanner.nextDouble();
		
		if(amount < 0.0)
			return false;
		bankLogic bank_logic = new bankLogic();
		return bank_logic.transfer(accountNumber, targetAccountNumber, amount);
	}

}
