package testmarkbank.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import services.AcctDB;
import services.UserDB;



/**
 * The wrapper class that does the loading and saving of our database:
 * userdb and acctdb
 */
public class Serializer {

	ArrayList<Object> database = new ArrayList<Object>();
	private static String fileName = "serialize/database.ser";
	
	public Serializer() {}
	/**
	 * Creates the necessary objects needed for the database to
	 * to start, then saves it.
	 * @return UserDB containing the newly created objects
	 */
	public services.UserDB makeDefaultDB() throws IOException {
		// Create a new admin, employee, and customer
		Admin admin = new Admin();
		Employee employee = new Employee();
		Customer customer = new Customer("customer","customer");
		services.UserDB udb = new services.UserDB();
		services.AcctDB adb = new services.AcctDB();
		
		// Add admin, employee, and customer to the database
		udb.getUserDB().add(admin);
		udb.getUserDB().add(employee);
		udb.getUserDB().add(customer);
		
		saveData(udb, adb);
		return udb;
	}
	/**
	 * Loads database from file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void loadData() {
		try (ObjectInputStream objInStream = new ObjectInputStream(
				new FileInputStream(fileName))) {
			database = (ArrayList<Object>)objInStream.readObject();
		} catch (ClassCastException e) {
		} catch (ClassNotFoundException e) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	/**
	 * Loads User Data from database
	 */
	public UserDB getSavedUserDB() {
		return (UserDB)this.database.get(0);
	}
	/**
	 * Loads Account Data from database
	 */
	public services.AcctDB getSavedAcctDB() {
		return (AcctDB)this.database.get(1);
	}
	/**
	 * Saves our databases to file
	 * @param userdb  	@param acctdb
	 * @throws IOException
	 */
	public void saveData(services.UserDB userdb, services.AcctDB acctdb) throws IOException {	
		this.database.add(0, userdb);
		this.database.add(1, acctdb);
		
		try (ObjectOutputStream objOutStream = new ObjectOutputStream(
				new FileOutputStream(fileName))) {
			objOutStream.writeObject(database);
			objOutStream.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

}
