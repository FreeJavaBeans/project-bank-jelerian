import java.util.Scanner;

public class login {
	private Scanner scanner;

	public login(Scanner scanner) {
		this.scanner = scanner;
	}

	public String tryLogin() {
		System.out.println("Please enter Username");
		String userName = scanner.next();
		System.out.println("Please enter Password");
		String password = scanner.next();
		bankLogic bank_logic = new bankLogic();
		if(bank_logic.validateAccount(userName, password) == null)
			userName = null;
		return userName;
	}
}
