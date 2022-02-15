package testmarkbank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;



public class Customer extends services.User implements Serializable {
	
	private static final long serialVersionUID = -8983905758980355135L;
	
	private ArrayList<Integer> accountIDs;
	private ArrayList<String> jointAccountHolders;
	private boolean applying;
		
	public Customer(String username, String password) {
		super.setUsername(username);
		super.setPassword(password);
		this.accountIDs = new ArrayList<Integer>();
		this.jointAccountHolders = new ArrayList<String>();
		this.applying = false;
	}
	
	public ArrayList<Integer> getAccountID() {
		return this.accountIDs;
	}

	public boolean addAccountID(int id) {
		Collections.sort(this.accountIDs);
		int index = Collections.binarySearch(this.accountIDs, id);
		if (index < 0) {
			this.accountIDs.add(id);
			return true; // account successfully added
		} else {
			return false; // account already owned
		}
	}

	public boolean cancelAccount(int id) {
		if (this.accountIDs.size() == 0) {
			System.out.println("This customer has no Accounts!");
			return false;
		}
		Collections.sort(this.accountIDs);
		int index = Collections.binarySearch(this.accountIDs, id);
		if (index < 0) {
			return false; // account not found
		} else {
			this.accountIDs.remove(index);
			return true; // account successfully removed
		}
	}

	public boolean isApplying() {
		return applying;
	}

	public void applyForAccount() {
		if (this.applying == true)
			System.out.println("You already have an application open.");
		this.applying = true;
	}

	public void finishApplication() {
		this.applying = false;
	}
	
	public void applyForJointAccount() {
		this.applying = true;
		this.jointAccountHolders = new ArrayList<String>();
	}
	
	public void addJointAccountHolder(String name) {
		this.jointAccountHolders.add(name);
	}
	
	public ArrayList<String> getJointAccountHolders() {
		return jointAccountHolders;
	}

	public void setJointAccountHolders(ArrayList<String> jointAccountHolders) {
		this.jointAccountHolders = jointAccountHolders;
	}

	public String printJointAccountHolders() {
		String s = "";
		
		for (String name:jointAccountHolders)
			s += name+", ";
		
		s = s.substring(0, s.length() - 2);
		return s;
	}
	
	public boolean hasAccount() {
		if (this.accountIDs.size() > 0)
			return true;
		return false;
	}
		
	@Override
	public String toString() {
		String s = "Username: " + this.getUsername() + "\n"
				 + "Password: " + this.getPassword() + "\n";
		if (this.accountIDs.size() > 0) {
			for (int i = 0; i < accountIDs.size(); i++)
				s += "Account " + (i+1) + ": " + accountIDs.get(i) + "\n";
		} else {
			s += "Applying for Account: " + applying + "\n";
			if (applying == true && jointAccountHolders.size() > 0) {
				s += "With joint users: ";
				for (String name:jointAccountHolders) {
					s += name + ", ";
				}
				s = s.substring(0, s.length() - 2);
			}
		}
		return s.trim();
	}

}
