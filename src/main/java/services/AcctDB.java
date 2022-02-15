package services;

import java.util.HashMap;

/**
 * This class stores the Serializable accounts needed for BankApp to function.
 */
public class AcctDB {
	
	private HashMap<Integer, Account> acctdb;
	private AccountDAO accdao = new AccountDAO();

	public AcctDB() {
		this.acctdb = accdao.getAccountMap();
	}
	
	public HashMap<Integer, Account> getAcctDB() {
		return this.acctdb;
	}

	public void update() {
		this.acctdb = accdao.getAccountMap();
	}
	
	@Override
	public String toString() {
		String s = "=== Account Database ===\n";
		
		for (Account acct:acctdb.values()) {
		    s += acct.toString() + "===\n";
		}
		return s;
	}
}