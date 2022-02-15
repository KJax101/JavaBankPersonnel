package services;

import java.util.ArrayList;
import java.util.Comparator;

public class User implements Comparator<User> {
	
	private String username, password;
	private int id, roleID, active;
	UserDAO userdao = new UserDAO();
	AccountDAO accdao = new AccountDAO();
	ApplicationDAO appdao = new ApplicationDAO();
	
	// Only for temporary users that will never be stored
	public User() {
		this.username = "";
		this.password = "";
		this.roleID = 1;
		this.active = 1;
	}
	
	// Normally only this should be called
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.roleID = 1;
		this.active = 1;
	}
	// Apply to new account
	public void applyForAccount() {
		// Gets the next unused id number from ACCTTABLE
		int newID = accdao.getNewAcctID();
		// Reserve the account in ACCTTABLE with that id for applicant
		accdao.addAccount(new Account());
		
		Application app = new Application(this.id, newID);
		app.submit();
	}
	// Apply to existing account
	public void applyForAccount(int acctID) {
		Application app = new Application(this.id, acctID);
		app.submit();
	}
	/**
	 * Submits an Application to the same Account for every name in nameList
	 * @param nameList
	 */
	public void applyForJointAccount(ArrayList<String> nameList) {
		User user = null;
		// Gets the next unused id number from ACCTTABLE
		int newID = accdao.getNewAcctID();
		// Reserve the account in ACCTTABLE with that id for applicant
		Account reserved = new Account();
		accdao.addAccount(reserved);
		
		Application app = new Application(this.id, newID);
		app.submit();
		// Search USERTABLE for a user with the same name as applicant's request
		for (String name:nameList) {
			user = userdao.getUser(name); // sets joint to null if not found
			if (user != null) { // if that user is found, make them apply to 
				app = new Application(user.getID(), newID);
				app.submit(); // the same account that our applicant created
			}
		}
	}

	public int getID() {						return id;					}
	public void setID(int id) {					this.id = id;				}
	public String getUsername() {				return username;			}
	public void setUsername(String username) {	this.username = username;	}
	public String getPassword() {				return password;			}
	public void setPassword(String password) {	this.password = password;	}
	public int getRoleID() {					return this.roleID;			}
	public void setRoleID(int roleID) {			this.roleID = roleID;		}
	public int getActive() {					return active;				}
	public void setActive(int active) {			this.active = active;		}

	public String toString() {
		return    "Username: " + this.getUsername() + " | "
				+ "Password: " + this.getPassword() + "\n";
	}

	@Override
	public int compare(User arg0, User arg1) {
		return arg0.username.compareTo(arg1.username);
	}
}
