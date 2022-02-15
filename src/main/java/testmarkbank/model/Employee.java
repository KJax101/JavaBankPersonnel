package testmarkbank.model;

import java.io.Serializable;



public class Employee extends services.User implements Serializable {

	private static final long serialVersionUID = -2929634669006090367L;
	private static final String youGuessedIt = "employee";

	public Employee() {
		// TODO Auto-generated constructor stub
		super.setUsername(youGuessedIt);
		super.setPassword(youGuessedIt);
		super.setRoleID(1);
	}

}
