package services;

import java.text.NumberFormat;
import java.util.Locale;

public class Account implements Comparable<Account> {
	
	private int id;
	private String name;
	private double balance;
	private int active;
	private AccountDAO accdao = new AccountDAO();

	public Account() {
		this.id = 0;
		this.name = "";
		this.balance = 0.0;
		this.active = 1;
	}
/*
	public Account(String name) {
		this.id = 0;
		this.name = name;
		this.balance = 0.0;
		this.active = 1;
	}
*/
	public boolean withdraw(double amount) {
		if (amount >= 0.0 && amount <= getBalance() && (amount * 100) % 1 == 0) {
			setBalance(getBalance() - amount);
			accdao.setAccount(this);
			return true;
		}
		return false;
	}
	
	public boolean deposit(double amount) {
		if (amount >= 0.0 && (amount * 100) % 1 == 0) {
			setBalance(getBalance() + amount);
			accdao.setAccount(this);
			return true;
		}
		return false;
	}
	
	public boolean transfer(int targetID, double amount) {
		if (amount >= 0.0 && (amount * 100) % 1 == 0) {
			if (accdao.transfer(this.id, targetID, amount) > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean cancel() {
		this.active = 0;
		if (accdao.cancelAccount(id) > 0)
			return true;
		return false;
	}
	
	public int getID() {			return id;		}
	public void setID(int id) {		this.id = id;	}
	public String getName() {			return name;		}
	public void setName(String name) {	this.name = name;	}
	public int getActive() {				return active;			}
	public void setActive(int active) {		this.active = active;	}
	public double getBalance() {				return this.balance;	}
	public void setBalance(double balance) {	this.balance = balance;	}
	
	public String printBalance() {
		NumberFormat usd = NumberFormat.getCurrencyInstance(Locale.US);
		return "Account #" + id + " has: " + usd.format(balance);   //Bob #2 has: $100
																	//Account #12\nBalance: $123,456,789.00\nOwners: myname
		//return "Account #" + id + ", " + name + " has: " + usd.format(balance); // use after naming accts is implemented
	}
	
	@Override
	public String toString() {
		String s = printBalance() + "\n";
		return s;
	}

	@Override
	public int compareTo(Account acct) {
		if (this.id < acct.id)
			return -1;
		else if (this.id > acct.id)
			return 1;
		else
			return 0;
	}

}
