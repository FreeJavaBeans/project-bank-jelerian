import java.util.Scanner;

public class addUser {
	private Scanner scanner;
	public addUser(Scanner scanner) {
		this.scanner = scanner;
	}

	public boolean addNewUserToDatabase() {
		System.out.println("UserName?");
		String userName = scanner.next();
		System.out.println("Password?");
		String password = scanner.next();
		System.out.println("Employee(Y/N)?");
		String answer = scanner.nextLine();
		boolean isEmployee = answer.toUpperCase() == "Y";
		bankLogic bank_logic = new bankLogic();
		return bank_logic.createUser(userName, password, isEmployee);
	}

}
