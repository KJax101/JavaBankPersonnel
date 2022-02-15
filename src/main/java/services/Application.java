package services;

public class Application implements Comparable<Application> {

	private int id, userID, acctID, statusID;
	private ApplicationDAO appdao = new ApplicationDAO();
	
	public Application() {
		this.userID = 0;
		this.acctID = 0;
		this.statusID = 1;
	}
	
	public Application(int userID) {
		this.userID = userID;
		this.acctID = 0;
		this.statusID = 1;
	}
	
	public Application(int userID, int acctID) {
		this.userID = userID;
		this.acctID = acctID;
		this.statusID = 1;
	}
	
	public User getUser() {
		UserDAO userdao = new UserDAO();
		return userdao.getUser(this.userID);
	}
	
	public Account getAccount() {
		AccountDAO accdao = new AccountDAO();
		return accdao.getAccount(this.acctID);
	}
	
	public String toString() {
		String s = getUser().getUsername() + " is applying for Account #" + this.acctID;
		return s;
	}
	
	public void submit() {
		appdao.addApplication(this);
	}
	public boolean pending() {
		return (this.statusID == 1);
	}
	public void approve() {
		this.statusID = 2;
		appdao.setStatus(2, this.id);
	}
	public void deny() {
		this.statusID = 3;
		appdao.setStatus(3, this.id);
	}
	/**
	 * BEANS
	 */
	public int getID() {					return id;				}
	public void setID(int id) {				this.id = id;			}
	public int getUserID() {				return userID;			}
	public void setUserID(int uid) {		this.userID = uid;		}
	public int getAccountID() {				return acctID;			}
	public void setAccountID(int aid) {		this.acctID = aid;		}
	public int getStatusID() {				return this.statusID;	}
	public void setStatusID(int sid) {		this.statusID = sid;	}

	@Override
	public int compareTo(Application arg0) {
		if (this.statusID > arg0.statusID) {
			return 1;
		} else if (this.statusID < arg0.statusID) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
