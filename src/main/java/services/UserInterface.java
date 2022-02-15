package services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to print the appropriate menu options for the end user.
 */
public class UserInterface {
	// The user that is logged in, and all the important tables from our db
	private User user;			private User cust; // The user that admins are viewing
	private UserDB userdb;		private AcctDB acctdb;
	private AppDB appdb;		private UserAcctDB useracctdb;
	private Scanner scanner = new Scanner(System.in);
	
	// My menus - I recommend collapsing them
	private final String[] loggedOutPrompt = {
			"=== Main Menu ===", 
			"1. Log in", 
			"2. Register new account", 
			"0. Save and Exit",
			"Enter a number: "};
	private final String[] custNoAcctPrompt = {
			"=== Main Menu ===", 
			"1. Log out", 
			"2. Apply for an Account",
			"3. Apply for a Joint Account",
			"0. Save and Exit",
			"Enter a number: "};

	private final String[] custWithAcctPrompt = { // The tabs look correct on console
			"=== Main Menu ===		=== Account ===", 
			"1. Log out			5. Withdraw", 
			"2. Apply for an Account		6. Deposit", 
			"3. Apply for a Joint Account	7. Transfer",
			"4. Add user to Account		8. View account info",
			"0. Save and Exit",
			"Enter a number: "};
	private final String[] employeePrompt = { // The tabs look correct on console
			"=== EMPLOYEE ===		== Handle applications ==",
			"1. Log out			5. Approve next application",
			"2. View all customers		6. View next application",
			"3. View all accounts		7. Deny next application",
			"4. View all applicants		0. Save and Exit",
			"Enter a number: "};
	private final String[] adminPrompt = { // The tabs look correct on console
			"=== BANK ADMIN ===		=== Customer Accounts ===",
			"1. Log out			7. Select a customer by name",
			"2. View all customers		8. Withdraw from selected customer",
			"3. View all accounts		9. Deposit to selected customer",
			"== Handle applications ==	10. Transfer from selected customer",
			"4. Approve next application	11. Cancel an account",
			"5. View next application	12. View selected customer's details",
			"6. Deny next application	0. Save and Exit",
			"Enter a number: "};

	public UserInterface(UserDB userdb, AcctDB acctdb, AppDB appdb, UserAcctDB useracctdb) {
		this.userdb = userdb;
		this.acctdb = acctdb;
		this.appdb = appdb;
		this.useracctdb = useracctdb;
	}

	//Prints different prompts depending on who is logged in
	public int prompt() throws ParseException {
		String s = "", dirtyInput = "";
		int input = -1;
		
		if (this.user == null) {
			for (int i = 0; i < loggedOutPrompt.length; i++)
				s += loggedOutPrompt[i] + "\n";
		} else if (user.getRoleID() == 1) {
			if (useracctdb.getAccounts(user.getID()).size() < 1) // if user has less than 1 account
				for (int i = 0; i < custNoAcctPrompt.length; i++)
					s += custNoAcctPrompt[i] + "\n";
			else
				for (int i = 0; i < custWithAcctPrompt.length; i++)
					s += custWithAcctPrompt[i] + "\n";
		} else if (user.getRoleID() == 2) {
			for (int i = 0; i < employeePrompt.length; i++)
				s += employeePrompt[i] + "\n";
		} else if (user.getRoleID() == 3) {
			for (int i = 0; i < adminPrompt.length; i++)
				s += adminPrompt[i] + "\n";
		}
		s = s.substring(0, s.length() - 1);
		System.out.println(s);

		// Clean user input
		dirtyInput = scanner.nextLine();
		input = sanitize(dirtyInput);
		
		// Process that input depending on who they are
		if (getUser() == null) {
			handleNewbie(input);
		} else if (getUser().getRoleID() == 1) {
			handleCustomers(input);
		} else if (getUser().getRoleID() == 2) {
			handleEmployees(input);
		} else if (getUser().getRoleID() == 3) {
			handleAdmins(input);
		}
		return input;
	}
	
	public void handleNewbie(int input) {
		switch (input) {
		case 1: // Log in
			this.user = login();
			break;
		case 2: // Register
			this.user = register();
			break;
		case 0: // Save and Quit
			break;
		}
	}
	
	public void handleCustomers(int input) throws ParseException {
		// if our customer has no account
		if (useracctdb.getAccounts(this.user.getID()).size() < 1) {
			switch (input) {
			case 1: // Log out
				LoggingUtil.logTrace(this.user.getUsername() + " logged out.");
				this.user = null;
				break;
			case 2: // Apply for account
				applyForAccount();
				break;
			case 3: // Apply for joint account
				applyForJointAccount();
				break;
			case 0:
				break;
			default:
				System.out.println("Please input one of the numbered options.");
			}
		} else {
			// if our customer has an account
			switch (input) {
			case 1: // Log out
				LoggingUtil.logTrace(this.user.getUsername() + " logged out.");
				this.user = null;
				//this.cust = null;
				break;
			case 2: // Apply for a new account
				applyForAccount();
				break;
			case 3: // Apply for a joint account
				applyForJointAccount();
				break;
			case 4: // Add user to account
				addUserToAccount();
				break;
			case 5: // Withdraw
				withdraw(this.user);
				break;
			case 6: // Deposit
				deposit(this.user);
				break;
			case 7: // Transfer
				transfer(this.user);
				break;
			case 8: // View account info
				System.out.println(this.user.toString());
				ArrayList<Integer> accList = useracctdb.getAccounts(user.getID());
				for (int acctID:accList)
					System.out.println(acctdb.getAcctDB().get(acctID).toString()+"===");
				break;
			case 0:
				break;
			default:
				System.out.println("Please input one of the numbered options.");
			}
		}
	}
	
	public void handleEmployees(int input) {
		//this.emp = (Employee)this.user;
		switch (input) {
		case 1: // Log out
			LoggingUtil.logTrace(this.user.getUsername() + " logged out.");
			this.user = null;
			//this.emp = null;
			break;
		case 2: // View all customers
			System.out.println(userdb.toString());
			break;
		case 3: // View all accounts
			System.out.println(acctdb.toString());
			break;
		case 4: // View all applications
			System.out.println(appdb.toString());
			break;
		case 5: // Approve next application
			approveNextApplication();
			break;
		case 6: // View next application
			viewNextApplication();
			break;
		case 7: // Deny next application
			denyNextApplication();
			break;
		case 0:
			break;
		default:
			System.out.println("Please input one of the numbered options.");
		}
	}
	
	public void handleAdmins(int input) throws ParseException {
		switch (input) {
		case 1: // Log out
			LoggingUtil.logTrace(this.user.getUsername() + " logged out.");
			this.user = null;
			this.cust = null;
			break;
		case 2: // View all customers
			System.out.println(userdb.toString());
			break;
		case 3: // View all accounts
			System.out.println(acctdb.toString());
			break;
		case 4: // Approve next application
			approveNextApplication();
			break;
		case 5: // View next application
			viewNextApplication();
			break;
		case 6: // Deny next application
			denyNextApplication();
			break;
		case 7: // Select a customer by name
			this.cust = selectCustByName();
			break;
		case 8: // Withdraw from selected customer
			if (this.cust != null)
				withdraw(this.cust);
			else
				System.out.println("Select a customer with '7' first.");
			break;
		case 9: // Deposit to selected account
			if (this.cust != null)
				deposit(this.cust);
			else
				System.out.println("Select a customer with '7' first.");
			break;
		case 10: // Transfer from selected account
			if (this.cust != null)
				transfer(this.cust);
			else
				System.out.println("Select a customer with '7' first.");
			break;
		case 11: // Cancel selected account
			if (this.cust != null)
				cancelAccount();
			else
				System.out.println("Select a customer with '7' first.");
			break;
		case 12: // View selected account
			if (this.cust != null) {
				System.out.println("=== SELECTED CUSTOMER ===");
				System.out.println(this.cust.toString());
				ArrayList<Integer> accIDList = useracctdb.getAccounts(this.cust.getID());
				if (accIDList.size() > 0)
					for (int accID:accIDList)
						System.out.println(acctdb.getAcctDB().get(accID).toString());
			} else
				System.out.println("Select a customer with '7' first.");
			break;
		case 0:
			break;
		default:
			System.out.println("Please input one of the numbered options.");
		}
	}

	/**
	 * User management
	 */
	public User login() {
		String inputName="", inputPass="";
		User user;
		
		System.out.println("=== Log in ===\n"
				+ "Leave username or password blank to cancel");
		
		do {// Prompt user for input
			System.out.print("Enter your Username: ");
			inputName = scanner.nextLine();
			if (inputName.length() == 0)
				return null;
			
			System.out.print("Enter your Password: ");
			inputPass = scanner.nextLine();
			if (inputPass.length() == 0) 
				return null;
			
			// validateLogin - checks to see if user is registered and
			user = userdb.validateLogin(inputName, inputPass);
			// if credentials were wrong, it prints the appropriate message
			if (user != null) {
				System.out.println("Welcome back, " + inputName+"!");
				break;
			}
		} while (true);

		LoggingUtil.logTrace(user + " logged in.");
		return user;
	}
	
	public User register() {
		String inputName="", inputPass="";
		User user = null;
		
		System.out.println("=== User Registration ===\n"
				+ "Leave username or password blank to cancel");
		do {// Prompt user for input
			System.out.print("Enter a unique Username: ");
			inputName = scanner.nextLine();
			if (inputName.length() == 0)
				return null;
			
			System.out.print("Enter desired Password: ");
			inputPass = scanner.nextLine();
			if (inputPass.length() == 0)
				return null;
			
			user = new User(inputName, inputPass);
			
			// validate user - it makes sure there are no duplicate names
			if (userdb.validateRegistration(user)) {
				System.out.println(inputName+" has been successfully registered!\n"
						+ "You have been logged in. Try applying for an Account!");
				break;
			} else {
				System.out.println("That username was taken. Please try again.");
			}
		} while (true);
		LoggingUtil.logTrace(user.getUsername() + " successfully registered.");
		// Get the post registered user (has ID), not the pre-registered user (has no ID)
		user = userdb.getUser(inputName);
		return user;
	}
	
	/**
	 * Customer management
	 */
	public boolean applyForAccount() {
		this.user.applyForAccount();
		LoggingUtil.logTrace(this.user.getUsername() + " applied for an Account.");
		System.out.println("=== Application sent! ===\n"
				+ "Your Account will be ready as soon as an Employee processes your application.");
		return true;
	}
	
	public boolean applyForJointAccount() {
		String input = "";
		ArrayList<String> nameList = new ArrayList<String>();
		System.out.println("=== Joint Account Application ===\n"
						+ "Please enter the exact username of the person you wish to share an account with.\n"
						+ "One username per line. Mispelled usernames or improper input may result in your\n"
						+ "application being denied. When you are done listing names, enter a blank line.");
		
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			System.out.print(i+1 + ". ");
			input = scanner.nextLine();
			if (input.length() == 0)
				break;
			nameList.add(input);
		}
		// For each name in nameList, create and submit an Application for the same account
		this.user.applyForJointAccount(nameList);
		LoggingUtil.logTrace(this.user + " applied for a Joint Account with: "
						+ nameList.toString());
		System.out.println("=== Application sent! ===\n"
						+ "Your Account will be ready as soon as an Employee processes your application.");
		return true;
	}
	
	public boolean addUserToAccount() {
		User target = selectCustByName();
		if (target != null) {
			target.applyForAccount();
			LoggingUtil.logTrace(target.getUsername() + " applied for an Account.");
			System.out.println("=== Application sent! ===\n"
			+ target.getUsername() + " will gain access as soon as an Employee processes the application.");
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Account management
	 */
	public boolean withdraw(User cust) throws ParseException {
		System.out.println("=== Withdraw ===");
		String textInput = "";
		double amount = 0.0;
		// Prompts the user to choose an account and returns their selection
		Account selection = selectAcctFromCust(cust);
		
		if (selection == null)
			return false;
		
		if (selection.getBalance() == 0) {
			System.out.println("You can't withdraw from an empty account.");
			return false;
		}
		do {// Turn user input into a double
			System.out.print("How much would you like to withdraw? (leave blank to cancel): ");
			textInput = scanner.nextLine();
			if (textInput.length() == 0)
				return false;
			amount = sanitizeCurrency(textInput);
			
			// Get the account from choice, and withdraw amount 
			if (amount >= 0.0 && selection.withdraw(amount) && (amount * 100) % 1 == 0)
				break;
			else
				System.out.println("Invalid input. Please try again.\n");
		} while (true);
		// Print and log result
		LoggingUtil.logTrace(cust.getUsername() + " withdrew " + amount + " from Account #" + selection.getID());
		System.out.println(selection.printBalance());
		return true;
	}
	
	public boolean deposit(User cust) throws ParseException {
		System.out.println("=== Deposit ===");
		String textInput = "";
		double amount = 0.0;
		// Prompts the user to choose an account and returns their selection
		Account selection = selectAcctFromCust(cust);
		
		if (selection == null)
			return false;
		
		do {// Turn user input into a double
			System.out.print("How much would you like to deposit? (leave blank to cancel): ");
			textInput = scanner.nextLine();
			if (textInput.length() == 0)
				return false;
			amount = sanitizeCurrency(textInput);
			
			// Get the account from choice, and withdraw amount
			if (amount >= 0.0 && selection.deposit(amount) && (amount * 100) % 1 == 0)
				break;
			else
				System.out.println("Invalid input. Please try again.\n");
		} while (true);
		// Print and log result
		LoggingUtil.logTrace(cust + " deposited " + amount + " to Account #" + selection.getID());
		System.out.println(selection.printBalance());
		return true;
	}

	public boolean transfer(User cust) throws ParseException {
		System.out.println("=== Transfer ===");
		String textInput = "";
		double amount = 0.0;
		Account targetAcct = null;
		int targetID;
		Account selection = selectAcctFromCust(cust);
		
		if (selection == null)
			return false;
		if (selection.getBalance() == 0) {
			System.out.println("You can't transfer from an empty account.");
			return false;
		}
		
		do {// Turn user input into a double
			System.out.print("How much would you like to transfer? (leave blank to cancel): ");
			textInput = scanner.nextLine();
			if (textInput.length() == 0)
				return false;
			amount = sanitizeCurrency(textInput);
			
			// Verifies that the amount to transfer is acceptable
			if (amount >= 0.0 && (amount * 100) % 1 == 0 && amount <= selection.getBalance())
				break;
			else
				System.out.println("Invalid amount. Please try again.\n");
		} while (true);
		
		// if the user did not cancel, start loop
		do {
			// Turn user input into an id number
			System.out.print("Which account # would you like to transfer to? (Leave blank to cancel): ");
			textInput = scanner.nextLine();
			if (textInput.length() == 0)
				return false;
			// Get the target Account from targetID
			targetID = sanitize(textInput);
			targetAcct = this.acctdb.getAcctDB().get(targetID);
			
			// if the account exists and transfer is successful, break out
			if (targetAcct != null && selection.transfer(targetID, amount)) {
				acctdb.update();
				break;
			}
			System.out.println("Invalid ID. Please try again.\n");
		} while (true);
		
		// Print result
		LoggingUtil.logTrace(cust + " transfered " + amount + " from Account #" 
				+ selection.getID() + " to Account #" + targetAcct.getID());
		// Get our updated account
		selection = acctdb.getAcctDB().get(selection.getID());
		System.out.println(selection.printBalance());
		return true;
	}
	
	/**
	 * Employee management
	 */
	public boolean approveNextApplication() {
		Application app = appdb.getNextApplication();
		if (app != null) {
			app.approve(); // change application status to approved and puts the changes on DB
			useracctdb.addPair(app.getUserID(), app.getAccountID()); // link user and reserved account
			
			LoggingUtil.logTrace("Approved! " + app.getUser().getUsername() + " can now access Account #"+ app.getAccountID());
			System.out.println("Approved! " + app.getUser().getUsername() + " can access Account #"+ app.getAccountID());
			// Calling this for employee convenience
			viewNextApplication();
			return true;
		} else {
			System.out.println("No pending applications remaining.");
			return false;
		}
	}

	public boolean viewNextApplication() {
		Application app = appdb.getNextApplication();
		if (app != null) {
			System.out.println("=== Next Applicant ===");
			System.out.println(app.toString());
			return true;
		} else {
			System.out.println("No pending applications remaining.");
			return false;
		}
	}
	
	public boolean denyNextApplication() {
		Application app = appdb.getNextApplication();
		if (app != null) {
			app.deny();  // change application status to denied and puts the changes on DB
			LoggingUtil.logTrace("Denied " + app.getUser().getUsername() + "'s Account Application.");
			System.out.println("Denied " + app.getUser().getUsername() + "'s Account Application.");
			// Calling this for employee convenience
			viewNextApplication();
			return true;
		} else {
			System.out.println("No pending applications remaining.");
			return false;
		}
	}
	/**
	 * Admin management
	 */
	public User selectCustByName() {
		String inputName="";
		User selected;
		
		System.out.println("=== Select a User ===\n"
				+ "Leave blank to cancel");
		
		do {// Prompt user for input
			System.out.print("Search: ");
			inputName = scanner.nextLine();
			
			if (inputName.length() == 0) {
				return null;
			} else { // Search for the user
				selected = userdb.getUser(inputName);
			}
			if (selected == null || selected.getRoleID() > 1) {
				System.out.println(inputName + " not found.");
				continue; // go back to Prompt user for input
			} else {
				LoggingUtil.logTrace(selected.getUsername() + " selected.");
				System.out.println(inputName + " selected.");
				break;
			}
		} while (true);
		
		return selected;
	}
	
	public Account selectAcctFromCust(User customer) {
		int choice;
		String textInput = "";
		Account acct = null; // the account to return
		// Get all of our customer's accounts
		ArrayList<Integer> custAccountList = useracctdb.getAccounts(customer.getID());
		
		// If the customer only has one account, choose that account automatically
		if (custAccountList.size() == 1) {
			acct = acctdb.getAcctDB().get(custAccountList.get(0));
			System.out.println("Selecting " + customer.getUsername()
						+ "'s only Account: " + acct.toString());
			return acct;
		} else if (custAccountList.size() > 1) {
			// Displays all of the customer's accounts
			System.out.println("Select an account (leave blank to cancel):\n");
			for (int i = 0; i < custAccountList.size(); i++) {
				System.out.println(i+1 +". "+ custAccountList.get(i).toString());
			}
			while (true) { // Loop until valid choice is made
				// Turn user input into an int
				textInput = scanner.nextLine();
				if (textInput.length() == 0)
					return null;
				choice = sanitize(textInput);
				// Get user's account id based on their choice
				// if choice is within bounds, go to next step
				if (choice > 0 && choice < custAccountList.size() + 1) {
					acct = acctdb.getAcctDB().get(custAccountList.get(choice - 1));
					break;
				}
				// else choice is out of bounds
				System.out.println("Input out of bounds. Please try again.\n");
			}
			return acct;
		} else {
			System.out.println("No accounts found.");
		}
		return null;
	}
	
	public boolean cancelAccount() {
		System.out.println("=== Cancel Account ===");
		Account acct = selectAcctFromCust(this.cust);
		if (acct != null) {
			if (acct.cancel()) {
				System.out.println(this.cust.getUsername() 
						+ " lost access to Account #" + acct.getID());
				LoggingUtil.logTrace(this.cust.getUsername() 
						+ " lost access to Account #" + acct.getID());
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Cleans user input to prevent exceptions
	 */
	// Interprets Strings as ints
	public int sanitize(String dirtyInput) {
		int output = -1;
		String cleanInput = dirtyInput.replaceAll("\\D", "");
		// (anything)(2 or more '.' | consecutive or nonconsecutive)(anything)
		cleanInput = cleanInput.replaceAll("\\.(.+)?\\.","");
		
		if (dirtyInput.length() == 0)
			return -1;
		if (!cleanInput.equals(dirtyInput)) {
			System.out.println("Your input contains illegal character(s).");
			return -1;
		}
		if (dirtyInput.length() > 7) {
			System.out.println("Your input is too long.");
			return -1;
		}
		output = Integer.parseInt(cleanInput);
		return output;
	}
	
	// Interprets Strings as currency (double)
	public double sanitizeCurrency(String dirtyInput) {
		double output = -1;
		// The following regex removes all non-digits non-decimal points
		String cleanInput = dirtyInput.replaceAll("[^0-9.]", "");
		// (anything)(2 or more '.' | consecutive or nonconsecutive)(anything)
		cleanInput = cleanInput.replaceAll("\\.(.+)?\\.","");
		
		if (dirtyInput.length() == 0)
			return -1;
		if (!dirtyInput.equals(cleanInput)) {
			System.out.println("Your input contains illegal character(s).");
			return -1;
		}
		if (dirtyInput.length() > 15) {
			System.out.println("Is this Zimbabwean currency? Your input is too long.");
			return -1;
		}
		output = Double.parseDouble(dirtyInput);
		if ((output * 100) % 1 > 0) {
			System.out.println("We don't deal with fractions of a cent.");
			return -1;
		}
		return output;
	}

	/**
	 * BEANS
	 */
	public User getUser() {					return user;			}
	public void setUser(User user) {		this.user = user;		}
	public UserDB getUserdb() {				return this.userdb;		}
	public void setUserdb(UserDB userdb) {	this.userdb = userdb;	}
	public AcctDB getAcctdb() {				return this.acctdb;		}
	public void setAcctdb(AcctDB acctdb) {	this.acctdb = acctdb;	}
}
