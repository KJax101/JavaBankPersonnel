package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

/**
 * This class makes sql requests to our database when our Application class needs it

 */
public class ApplicationDAO {
	// Likely not going to be used
	public Application getApplication(int id) {
		String sql = "SELECT * FROM USER_ACCT_APPLICATION WHERE APP_ID = ? ";
		
		Application app = new Application();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id); // change ? to int id
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				app.setID(rs.getInt(1));
				app.setStatusID(rs.getInt(2));
				app.setUserID(rs.getInt(3));
				app.setAccountID(rs.getInt(4));
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return app;
	}
	
	public ArrayList<Application> getApplicationList() {
		String sql = "SELECT * FROM USER_ACCT_APPLICATION";
		
		ArrayList<Application> appList = new ArrayList<Application>();
		Application app = null;
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
	
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				app = new Application();
				app.setID(rs.getInt(1));
				app.setStatusID(rs.getInt(2));
				app.setUserID(rs.getInt(3));
				app.setAccountID(rs.getInt(4));
				appList.add(app);
			}
			conn.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appList;
	}

	public int setStatus(int status, int id) {
		String sql = "UPDATE USER_ACCT_APPLICATION SET STATUS_ID=? "
				+ "WHERE APP_ID=? ";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			save = conn.setSavepoint("Before_updating_appID#"+id);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ps.setInt(2, id);
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0)
				System.out.println(rowsUpdated + " Application updated.");
			else
				System.out.println("Failed to update Application.");

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

	public int addApplication(Application app) {
		String sql = "INSERT INTO USER_ACCT_APPLICATION (APP_ID, STATUS_ID, USER_ID, ACCT_ID) "
					+ "VALUES (0, 1, ?, ?)";
		
		int rowsUpdated = -1;
		Connection conn = ConnectionFactory.getInstance().getConnection();
		Savepoint save = null;
		try {
			conn.setAutoCommit(false);
			String s = "Before_adding_application" + app.getID();
			save = conn.setSavepoint(s);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, app.getUserID());
			ps.setInt(2, app.getAccountID());
			
			rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0) // TODO Change this to log
				System.out.println(rowsUpdated + " Application added to database.");
			else
				System.out.println("Failed to add Application to database.");

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
