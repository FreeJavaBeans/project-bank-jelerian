import java.util.Scanner;

public class menu {

	public menu() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		int choice = 0;
        Scanner scanner = new Scanner(System.in);
        String username = null;

		do {
			boolean success = false;
	        System.out.println("1. Login");
			System.out.println("2. Add personal data");
	        System.out.println("3. Create user account");
	        System.out.println("4. Add account");
	        System.out.println("5. Display accounts");
	        System.out.println("6. Deposit");
	        System.out.println("7. Withdraw");
	        System.out.println("8. Transfer");
	        System.out.println("9. Exit");
	        System.out.print("Enter your choice: ");
	        try {
		        choice = scanner.nextInt();
		        switch (choice) {
		            case 1:
		            	login Login = new login(scanner);
		            	username = Login.tryLogin();
		            	if(username == null)
		            		System.out.println("Invalid user. Please try again.");
		            	else
		            		System.out.println("User: " + username + " logged in.");
		                break;
		                 
		            case 2:
		            	addPerson person = new addPerson(scanner);
		            	success = person.addPersonToDatabase();
	               		 if(!success)
	            			 System.out.println("Sorry. Unable to add Person to the database, as requested");
	            		 else
	            			 System.out.println("Success.");
			                break;
	                case 3:
	                	addUser newUser = new addUser(scanner);
		            	success = newUser.addNewUserToDatabase();
	               		 if(!success)
	            			 System.out.println("Sorry. Unable to add new user to the database, as requested");
	            		 else
	            			 System.out.println("Success.");
	                	break;
	                case 4: 
	                	if(username != null) {
	                		addAccount AddAccount = new addAccount(scanner, username);
	                		 success = AddAccount.createNewAccount();
	                		 if(!success)
	                			 System.out.println("Sorry. Unable to create the account, as requested");
	                		 else
	                			 System.out.println("Success.");
	                	}
	                	else
	                		System.out.println("Please log in first.");
	                		
		                break;
	                case 5: 
	                	
		                break;
	                case 6: 
	                	if(username != null) {
	                		transaction deposit = new transaction(scanner, username);
	                		 success = deposit.deposit();
	                		 if(!success)
	                			 System.out.println("Sorry. Unable to make the deposit, as requested");
	                		 else
	                			 System.out.println("Success.");
	                	}
	                	else
	                		System.out.println("Please log in first.");
	                		
		                break;
	                	
	                case 7: 
		                break;
	                case 8: 
		                break;
	                case 9: 
	                	System.out.println("Thanks for using Jay's bank");
		                break;
	     
		            default:
		                System.out.println("Invalid choice");
		        }
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        	System.out.println("Invalid input");
	        	scanner.next();
	        }
		} while(choice != 9);
		scanner.close();
	}
}
