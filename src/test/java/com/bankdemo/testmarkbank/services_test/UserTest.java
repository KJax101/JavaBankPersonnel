package com.bankdemo.testmarkbank.services_test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import services.User;

public class UserTest {
	private User user = new User();
	
	/**
	 * Test User.java
	 */
	@Test
	public void testGetSetUsername() {
		user.setUsername("name");
		assertEquals("name", user.getUsername());
	}
	@Test
	public void testGetSetPassword() {
		user.setPassword("pass");
		assertEquals("pass", user.getPassword());
	}
	@Test
	public void testToString() {
		user.setUsername("name");
		user.setPassword("pass");
		assertEquals("Username: name\nPassword: pass\n", user.toString());
	}
	@Test
	public void testCompare() {
		user.setUsername("name");
		User user2 = new User("name","");
		assertEquals(0, user.compare(user, user2));
	}

}
