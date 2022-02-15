package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

/**
 * This class makes sql requests to our database when our User class needs it
 */
public class UserDAO {
	
	public User getUser(String name) {
		String sql = "SELECT * FROM USERTABLE WHERE USERNAME = ? AND ACTIVE=1 ";
		
		User user = null;
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name); // change the ? in sql to name
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				user = new User();
				user.setID(rs.getInt(1));
				user.setRoleID(rs.getInt(2));
				user.setUsername(rs.getString(3));
				user.setPassword(rs.getString(4));
				user.setActive(rs.getInt(5));
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User getUser(int id) {
		String sql = "SELECT * FROM USERTABLE WHERE USER_ID = ? AND ACTIVE=1 ";
		
		User user = null;
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id); // change the ? in sql to name
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				user = new User();
				user.setID(rs.getInt(1));
				user.setRoleID(rs.getInt(2));
				user.setUsername(rs.getString(3));
				user.setPassword(rs.getString(4));
				user.setActive(rs.getInt(5));
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public int addUser(User user) {
		String sql = "INSERT INTO USERTABLE (USER_ID, ROLE_ID, USERNAME, PASSWORD, ACTIVE) VALUES (?, ?, ?, ?, 1)";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			String s = "AddUser";
			save = conn.setSavepoint(s);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getRoleID());
			ps.setInt(2, user.getRoleID());
			ps.setString(3, user.getUsername());
			ps.setString(4, user.getPassword());
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " User added to database.");
			else
				System.out.println("Failed to add User to database.");

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
	
	// Used to build the java data structure of our USERTABLE
	public ArrayList<User> getUserList() {
		String sql = "SELECT * FROM USERTABLE WHERE ACTIVE=1 ";
		
		ArrayList<User> userList = new ArrayList<User>();
		User user = null;
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
	
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User();
				user.setID(rs.getInt(1));
				user.setRoleID(rs.getInt(2));
				user.setUsername(rs.getString(3));
				user.setPassword(rs.getString(4));
				user.setActive(rs.getInt(5));
				userList.add(user);
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	// Currently not implemented
	public int setUser(User user) {
		String sql = "UPDATE USERTABLE SET ROLE_ID=?, USERNAME=?, "
				+ "PASSWORD=?, ACTIVE=1 WHERE USER_ID=? ";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			save = conn.setSavepoint("Before_updating_User:"+user.getUsername());
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getRoleID());
			ps.setInt(4, user.getID());
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " User updated.");
			else
				System.out.println("Failed to update User.");

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

	// Currently not implemented
	public int cancelUser(int id) {
		String sql = "UPDATE USERTABLE SET ACTIVE=0 WHERE ID=? ";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			save = conn.setSavepoint("Before_Canceling_User:"+id);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " User cancelled.");
			else
				System.out.println("Failed to cancel User.");

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
