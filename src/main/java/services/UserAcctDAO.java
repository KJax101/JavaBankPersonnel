package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

/**
 * This class grants us access to the table containing user+account relationships
 */
public class UserAcctDAO {
	// Returns the owners of the given account id
	public ArrayList<Integer> getOwners(int id) {
		String sql = "SELECT USER_ID FROM USER_ACCT WHERE ACCT_ID = ? ";
		
		ArrayList<Integer> userIDList = new ArrayList<Integer>();
		
		AccountDAO accdao = new AccountDAO();
		if (accdao.getAccount(id).getActive() == 1) {
			try {
				Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, id);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					userIDList.add(rs.getInt(1));
				}
				conn.close();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userIDList;
	}
	// Returns accounts owned by the given user id
	public ArrayList<Integer> getAccounts(int id) {
		String sql = "SELECT ACCT_ID FROM USER_ACCT WHERE USER_ID = ? ";

		ArrayList<Integer> acctIDList = new ArrayList<Integer>();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
	
			ResultSet rs = ps.executeQuery();

			AccountDAO accdao = new AccountDAO();
			int acctID;
			while (rs.next()) {
				acctID = rs.getInt(1);
				if (accdao.getAccount(acctID).getActive() == 1) {
					acctIDList.add(acctID);
				}
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acctIDList;
	}
	// Returns a table of every user+account pairing
	public ArrayList<int[]> getPairList() {
		String sql = "SELECT * FROM USER_ACCT";
		
		ArrayList<int[]> useracct = new ArrayList<int[]>();
		int[] pair = new int[2];
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
	
			AccountDAO accdao = new AccountDAO();
			ResultSet rs = ps.executeQuery();
			int acctID;
			while (rs.next()) {
				acctID = rs.getInt(2);
				if (accdao.getAccount(acctID).getActive() == 1) {
					pair[0] = rs.getInt(1);
					pair[1] = acctID;
					useracct.add(pair);
				}
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return useracct;
	}
	// Adds a new owner to an account
	public int addPair(int userID, int acctID) {
		String sql = "INSERT INTO USER_ACCT (USER_ID, ACCT_ID) VALUES(?, ?) ";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			String s = "Before_adding_Join";
			save = conn.setSavepoint(s);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userID);
			ps.setInt(2, acctID);
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)  // TODO Change this to log
				System.out.println("Join added to database."); 
			else
				System.out.println("Failed to add Join to database.");

			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			try {
				if (save != null)
					conn.rollback(save);
			} catch (SQLException x) {
				x.printStackTrace();
			}
			e.printStackTrace();
		}
		return rowsUpdated;
	}

}
