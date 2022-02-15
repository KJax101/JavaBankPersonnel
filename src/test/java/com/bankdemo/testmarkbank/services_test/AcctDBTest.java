package com.bankdemo.testmarkbank.services_test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


import services.Account;
import services.AcctDB;
import testmarkbank.model.Customer;

public class AcctDBTest {
	private AcctDB acctdb = new AcctDB();
	
	/**
	 * Test AcctDB.java
	 */
	@Test
	public void testGetSetAcctDB() {
		acctdb.update();
		assertEquals(new HashMap<Long, Account>(), acctdb.getAcctDB());
	}
	@Test
	public void testToString() {
		acctdb.update();
		Account a = new Account();
		int id = 1;
		a.setID(id);
		a.deposit(100);
		ArrayList<Customer> owners = new ArrayList<Customer>();
		owners.add(new Customer("name", "pass"));
		
		assertEquals("=== Account Database ===\nAccount #1\nBalance: $100\nOwners: name\n===\n", acctdb.toString());
	}
}
