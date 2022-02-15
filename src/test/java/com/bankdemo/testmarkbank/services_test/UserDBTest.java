package com.bankdemo.testmarkbank.services_test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import testmarkbank.model.Customer;
import testmarkbank.model.Employee;
import services.User;
import services.UserDB;

public class UserDBTest {
	private UserDB userdb = new UserDB();
	
	/**
	 * Test UserDB.java
	 */
	@Test
	public void testGetSetUserDB() {
		userdb.setUserDB(new ArrayList<User>());
		assertEquals(new ArrayList<User>(), userdb.getUserDB());
	}
	@Test
	public void testValidateLoginOK() {
		String s = "customer";

		User u = new User();
		u.setUsername(s);
		u.setPassword(s);
		userdb.getUserDB().add(u);
		assertEquals(u, userdb.validateLogin(s,s));
	}
	@Test
	public void testValidateLoginWrongName() {
		String s = "customer";
		User u = new User();
		u.setUsername(s);
		u.setPassword(s);
		userdb.getUserDB().add(u);
		assertEquals(null, userdb.validateLogin("wrong", s));
	}
	@Test
	public void testValidateLoginWrongPass() {
		String s = "customer";
		User u = new User();
		u.setUsername(s);
		u.setPassword(s);
		userdb.getUserDB().add(u);
		assertEquals(null, userdb.validateLogin(s, "wrong"));
	}
	@Test
	public void testValidateIfUserIsAlreadyRegistered() {
		String s = "customer";
		User u = new User();
		u.setUsername(s);
		u.setPassword(s);
		userdb.getUserDB().add(u);
		assertEquals(false, userdb.validateRegistration(u));
	}
	@Test
	public void testValidateIfUserIsNotRegistered() {
		String s = "customer";
		User u = new User();
		u.setUsername(s);
		u.setPassword(s);
		assertEquals(true, userdb.validateRegistration(u));
	}
	@Test
	public void testValidateIfUserLeftPasswordBlank() {
		String s = "customer";
		User u = new User();
		u.setUsername(s);
		u.setPassword("");
		assertEquals(false, userdb.validateRegistration(u));
	}
	@Test
	public void testValidateIfUserLeftUsernameBlank() {
		String s = "customer";
		User u = new User();
		u.setUsername("");
		u.setPassword(s);
		assertEquals(false, userdb.validateRegistration(u));
	}
	@Test
	public void testToString() {
		String s = "customer";
		Customer c = new Customer(s, s);
		userdb.getUserDB().add(c);
		c.applyForAccount();
		
		s = "customer2";
		Customer c2 = new Customer(s, s);
		userdb.getUserDB().add(c2);
		
		Employee e = new Employee();
		userdb.getUserDB().add(e);
		
		assertEquals("=== View Customers ===\nUsername: customer\nPassword: customer\n" + 
				"Applying for Account: true\n===\nUsername: customer2\nPassword: customer2\n" + 
				"Applying for Account: false\n===\n", userdb.toString());
	}
}
