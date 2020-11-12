import java.util.Scanner;

public class addPerson {
	private Scanner scanner;
	public addPerson(Scanner scanner) {
		this.scanner = scanner;
	}
	

	public boolean addPersonToDatabase() {
		System.out.println("First Name?");
		String firstName = scanner.next();
		System.out.println("Last Name?");
		String lastName = scanner.next();
		System.out.println("Address?");
		String address = scanner.nextLine();
		System.out.println("State id type?");
		String idType = scanner.next();
		System.out.println("State id number?");
		String id = scanner.next();
		System.out.println("State?");
		String state = scanner.next();
		bankLogic bank_logic = new bankLogic();
		return bank_logic.addPerson(firstName, lastName, address, idType, id, state);
	}


}
