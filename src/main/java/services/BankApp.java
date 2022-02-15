package services;

import java.io.IOException;
import java.text.ParseException;

/**
 * main initializes class members which handle database communication and makes
 * data strucutres UserInterface then prints the appropriate menus for the end
 * user to manipulate data structures Finally, these changes are reflected in
 * the database and logged
 *
 */
public class BankApp {
	private static UserDB userdb = new UserDB();
	private static AcctDB acctdb = new AcctDB();
	private static AppDB appdb = new AppDB();
	private static UserAcctDB useracct = new UserAcctDB();
	private static UserInterface ui = new UserInterface(userdb, acctdb, appdb, useracct);

	//TODO Make trigger on update to insert log to record transactions
	public static void main(String[] args) throws IOException, ClassNotFoundException, ParseException {
		LoggingUtil.logTrace("Session start.");
        
System.out.println("Welcome to TestMart Bank");

		// Loop de loop
		do {} while (ui.prompt() != 0);
		
		LoggingUtil.logTrace("Session closed.");
		System.out.println("Thank you for choosing TestMart Bank!");
	}

}
