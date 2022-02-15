package services;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAcctDB implements Serializable {
	private static final long serialVersionUID = 2508484821504628020L;

	private ArrayList<int[]> useracctdb = new ArrayList<int[]>();
	private UserAcctDAO useracctdao = new UserAcctDAO();
	
	public UserAcctDB() {
		useracctdb = useracctdao.getPairList();
	}
	
	public ArrayList<Integer> getOwners(int acctID) {
		return useracctdao.getOwners(acctID);
	}
	
	public ArrayList<Integer> getAccounts(int userID) {
		return useracctdao.getAccounts(userID);
	}
	
	public void addPair(int userID, int acctID) {
		useracctdao.addPair(userID, acctID);
		useracctdb = useracctdao.getPairList();
	}
	
	public ArrayList<int[]> getUserAcctDB() {				return useracctdb;					}
	public void setUserAcctDB(ArrayList<int[]> useracctdb) {	this.useracctdb = useracctdb;	}

}
