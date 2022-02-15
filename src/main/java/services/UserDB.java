package services;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * This class stores all the Users of the Bank and adds them
 */
public class UserDB implements Serializable {
	
	private static final long serialVersionUID = -4749332854077553312L;
	private ArrayList<User> userdb;
		UserDAO userdao = new UserDAO();
	
	public UserDB() {
		userdb = userdao.getUserList();
	}

	public ArrayList<User> getUserDB() {
		return this.userdb;
	}

	public void setUserDB(ArrayList<User> db) {
		this.userdb = db;
	}
	
	public User getUser(String name) {
		return userdao.getUser(name);
	}
	
	/**
	 * Checks to see if the user's name is in USERTABLE
	 * then checks if their password matches that entry
	 * @param name 			@param pass
	 * @return the user in USERTABLE with the credentials in @param
	 * 			OR null if the credentials do not match
	 */
	public User validateLogin(String name, String pass) {
		User user = userdao.getUser(name);
		if (user == null) {
			System.out.println("Incorrect username.");
			return null;
		}
		if (!user.getPassword().equals(pass)) {
			System.out.println("Incorrect password.");
			return null;
		}
		return user;
	}
	
	/**
	 * Checks to see if user's name is NOT in USERTABLE,
	 * if not, add them to USERTABLE
	 * @param user
	 * @return true if user can be registered
	 */
	public boolean validateRegistration(User user) {
		User exists = userdao.getUser(user.getUsername());
		if (exists == null){
			userdao.addUser(user);
			userdb = userdao.getUserList();
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		String s = "=== View Customers ===\n";
		for (User user:userdb) {
			if (user.getRoleID() == 1) {
				s += user.toString();
				s += "===\n";
			}
		}
		return s;
	}
}
