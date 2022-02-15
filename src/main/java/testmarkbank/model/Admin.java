package testmarkbank.model;

import java.io.Serializable;


public class Admin extends services.User implements Serializable {

	private static final long serialVersionUID = -2082260850999897126L;
	private static final String youGuessedIt = "admin";

	public Admin() {
		super.setUsername(youGuessedIt);
		super.setPassword(youGuessedIt);
		super.setRoleID(2);
	}

}
