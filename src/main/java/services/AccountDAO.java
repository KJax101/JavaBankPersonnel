package services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;

/**
 * This class makes sql requests to our database when our Account class needs it
 */
public class AccountDAO {
	
	public Account getAccount(int id) {
		String sql = "SELECT * FROM ACCTTABLE WHERE ACCT_ID = ? AND ACTIVE=1 ";
		
		Account acct = new Account();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id); // change ? to int id
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				acct.setID(rs.getInt(1));
				acct.setName(rs.getString(2));
				acct.setBalance(rs.getDouble(3));
				acct.setActive(rs.getInt(4));
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acct;
	}
	
	// Used to reserve new accounts
	public int getNewAcctID() {
		String sql = "SELECT MAX(ACCT_ID) FROM ACCTTABLE";
		
		int newID = -1;
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				newID = rs.getInt(1);
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newID+1;
	}

	// Used to load database
	public HashMap<Integer, Account> getAccountMap() {
		String sql = "SELECT * FROM ACCTTABLE WHERE ACTIVE=1 ";
		
		HashMap<Integer, Account> acctMap = new HashMap<Integer, Account>();
		Account acct = null;
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
	
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				acct = new Account();
				acct.setID(rs.getInt(1));
				acct.setName(rs.getString(2));
				acct.setBalance(rs.getDouble(3));
				acct.setActive(rs.getInt(4));
				acctMap.put(acct.getID(), acct);
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acctMap;
	}

	// Used for transactions
	public int setAccount(Account acct) {
		String sql = "UPDATE ACCTTABLE SET ACCT_NAME=?, "
				+ "BALANCE=? WHERE ACCT_ID=? ";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			save = conn.setSavepoint("Before_updating_ID#"+acct.getID());
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, acct.getName());
			ps.setDouble(2, acct.getBalance());
			ps.setInt(3, acct.getID());
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " Account updated.");
			else
				System.out.println("Failed to update Account.");

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

	// Used only by admin
	public int cancelAccount(int id) {
		String sql = "UPDATE ACCTTABLE SET ACTIVE=0 WHERE ID=? ";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			save = conn.setSavepoint("Before_Canceling_ID#"+id);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " Account cancelled.");
			else
				System.out.println("Failed to cancel Account.");

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

	// Used frequently
	public int addAccount(Account acct) {
		String sql = "INSERT INTO ACCTTABLE (ACCT_ID, ACCT_NAME, BALANCE, ACTIVE) VALUES(?, ?, ?, 1)";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			String s = "Before_adding_ID#" + acct.getID();
			save = conn.setSavepoint(s);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, acct.getID());
			ps.setString(2, acct.getName());
			ps.setDouble(3, acct.getBalance());
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0) // TODO Change this to log
				System.out.println(rowsUpdated + " Account added to database.");
			else
				System.out.println("Failed to add Account to database.");

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

	// Use a stored procedure
	public int transfer(int fromID, int toID, double amount) {
		String sql = "{call TRANSFER(?,?,?)}";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			save = conn.setSavepoint("Before_transfer");
			
			CallableStatement cs = conn.prepareCall(sql);
			cs.setInt(1, fromID);
			cs.setInt(2, toID);
			cs.setDouble(3, amount);
			
			rowsUpdated = cs.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " Account updated."); // make this a log
			else
				System.out.println("Failed to update Account.");

			cs.close();
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
