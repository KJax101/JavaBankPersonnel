package services;

import java.util.ArrayList;
import java.util.Collections;

public class AppDB {

	private ArrayList<Application> appdb;
	private ApplicationDAO appdao = new ApplicationDAO();
	
	public AppDB() {
		appdb = appdao.getApplicationList();
	}
	
	public int setAppStatus(int status, int id) {
		return appdao.setStatus(status, id);
	}
	
	public Application getNextApplication() {
		appdb = appdao.getApplicationList();
		Collections.sort(appdb);
		Application next = appdb.get(0);
		if (next.pending())
			return appdb.get(0);
		else
			return null;
	}
	
	public ArrayList<Application> getAppdb() {			return appdb;		}
	public void setAppdb(ArrayList<Application> appdb) {this.appdb = appdb;	}

	@Override
	public String toString() {
		String s = "=== Application List ===\n";
		appdb = appdao.getApplicationList();
		for (Application app:appdb) {
			if (app.pending())
		    	s += app.toString() + "\n===\n";
		}
		return s;
	}
}
