package com.bankdemo.testmarkbank.services_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Scanner;

import org.junit.Test;

import services.AcctDB;
import services.AppDB;
import services.User;
import services.UserAcctDB;
import services.UserDB;
import services.UserInterface;
import testmarkbank.model.Customer;

public class UserInterfaceTest {
	private static UserDB userdb = new UserDB();
	private static AcctDB acctdb = new AcctDB();
	private static AppDB appdb = new AppDB();
	private static UserAcctDB useracct = new UserAcctDB();
	private static UserInterface ui = new UserInterface(userdb, acctdb, appdb, useracct);
	Scanner scanner = new Scanner(System.in);
	
	// My menus - I recommend collapsing them
	private final String[] loggedOutPrompt = {
			"=== Main Menu ===", 
			"1. Log in", 
			"2. Register new account", 
			"0. Save and Exit",
			"Enter a number: "};
	private final String[] custNoAcctPrompt = {
			"=== Main Menu ===", 
			"1. Log out", 
			"2. Apply for an Account",
			"3. Apply for a Joint Account",
			"0. Save and Exit",
			"Enter a number: "};
	private final String[] custWithAcctPrompt = { // The tabs look correct on console
			"=== Main Menu ===		=== Account ===", 
			"1. Log out			4. Withdraw", 
			"2. Apply for an Account		5. Deposit", 
			"3. Apply for a Joint Account	6. Transfer",
			"0. Save and Exit		7. View account info",
			"Enter a number: "};
	private final String[] employeePrompt = { // The tabs look correct on console
			"=== EMPLOYEE ===		== Handle applications ==",
			"1. Log out			4. Approve next application",
			"2. View all customers		5. View next application",
			"3. View all accounts		6. Deny next application",
			"0. Save and Exit		7. View all applicants",
			"Enter a number: "};
	private final String[] adminPrompt = { // The tabs look correct on console
				"=== BANK ADMIN ===		=== Customer Accounts ===",
				"1. Log out			7. Select a customer by name",
				"2. View all customers		8. Withdraw from selected customer",
				"3. View all accounts		9. Deposit to selected customer",
				"== Handle applications ==	10. Transfer from selected customer",
				"4. Approve next application	11. Cancel an account",
				"5. View next application	0. Save and Exit",
				"6. Deny next application",
				"Enter a number: "};
	
	/**
	 * Test UserInterface.java
	 */
	@Test
	public void testSanitizeOK() throws ParseException {
		assertEquals(1, ui.sanitize("1"));
	}
	@Test
	public void testSanitizeDecimal() throws ParseException {
		assertEquals(-1, ui.sanitize("1.001"));
	}
	@Test
	public void testSanitizeNegative() throws ParseException {
		assertEquals(-1, ui.sanitize("-1"));
	}
	@Test
	public void testSanitizeEmpty() throws ParseException {
		assertEquals(-1, ui.sanitize(""));
	}
	@Test
	public void testSanitizeOverflow() throws ParseException {
		assertEquals(-1, ui.sanitize("100000000000000000000000"));
	}
	@Test
	public void testSanitizeGarbage() throws ParseException {
		assertEquals(-1, ui.sanitize("Q@#R2r2rsfdy7*&*H&df;"));
	}
	@Test
	public void testSanitizeRegex() throws ParseException {
		assertEquals(-1, ui.sanitize("\\d"));
	}
	@Test
	public void testSanitizeDoubleDecimal() throws ParseException {
		assertEquals(-1, ui.sanitize("100.01.10"));	
	}

	@Test
	public void testViewNextApplicationOK() {
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.viewNextApplication());
	}
	@Test
	public void testViewNextJointApplicationOK() {
		Customer customer = new Customer("customer","customer");
		ui.getUserdb().getUserDB().add(customer);
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		cust.addJointAccountHolder("customer");
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.viewNextApplication());
	}
	@Test
	public void testViewNextJointApplicationOKEvenIfRequestUnregistered() {
		ui.setAcctdb(acctdb);
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		cust.addJointAccountHolder("customer");
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.viewNextApplication());
	}
	@Test
	public void testViewNextApplicationNoApplicant() {
		assertFalse(ui.viewNextApplication());
	}
//--------------------------------------------------------------------
	@Test
	public void testApproveNextJointApplicationOK() {
		Customer customer = new Customer("customer","customer");
		ui.getUserdb().getUserDB().add(customer);
		ui.setAcctdb(acctdb);
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		cust.addJointAccountHolder("customer");
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.approveNextApplication());
	}
	@Test
	public void testApproveNextJointApplicationWithoutJointHolder() {
		Customer customer = new Customer("customer","customer");
		ui.getUserdb().getUserDB().add(customer);
		ui.setAcctdb(acctdb);
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.approveNextApplication());
	}
	@Test
	public void testApproveNextJointApplicationOKEvenIfRequestUnregistered() {
		ui.setAcctdb(acctdb);
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		cust.addJointAccountHolder("customer");
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.approveNextApplication());
	}
	@Test
	public void testApproveNextJointApplicationOKEvenIfRequestDuplicate() {
		Customer customer = new Customer("customer","customer");
		ui.getUserdb().getUserDB().add(customer);
		ui.setAcctdb(acctdb);
		Customer cust = new Customer("myname","mypass");
		cust.applyForAccount();
		cust.addJointAccountHolder("customer");
		cust.addJointAccountHolder("customer");
		cust.addJointAccountHolder("customer");
		ui.getUserdb().getUserDB().add(cust);
		assertTrue(ui.approveNextApplication());
	}
	@Test
	public void testApproveNextApplicationNoApplicant() {
		assertFalse(ui.approveNextApplication());
	}
//--------------------------------------------------------------------
	@Test
	public void testDenyNextApplicationOK() {
		Customer cust = new Customer("myname","mypass");
		ui.getUserdb().getUserDB().add(cust);
		ui.setAcctdb(acctdb);
		cust.applyForAccount();
		assertTrue(ui.denyNextApplication());
	}
	@Test
	public void testDenyNextApplicationNoApplicant() {
		Customer cust = new Customer("myname","mypass");
		ui.getUserdb().getUserDB().add(cust);
		ui.setAcctdb(acctdb);
		assertFalse(ui.denyNextApplication());
	}
	@Test
	public void testGetSetUser() {
		User user = new User();
		ui.setUser(user);
		assertEquals(user, ui.getUser());
	}
	@Test
	public void testGetSetUserdb() {
		ui.setUserdb(userdb);
		assertEquals(userdb, ui.getUserdb());
	}
	@Test
	public void testGetSetAcctdb() {
		ui.setAcctdb(acctdb);
		assertEquals(acctdb, ui.getAcctdb());
	}

}
