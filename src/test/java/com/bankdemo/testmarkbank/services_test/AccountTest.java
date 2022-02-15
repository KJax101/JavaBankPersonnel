package com.bankdemo.testmarkbank.services_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import services.Account;

public class AccountTest {

	private Account account = new Account();
	
	/**
	 * Tests for Account.java
	 */
	// Withdrawals
	@Test
	public void withdrawFromEmptyAccount() {
		assertFalse(account.withdraw(0.11));
	}
	@Test
	public void zeroWithdraw() {
		assertTrue(account.withdraw(0.00));
	}
	@Test
	public void negativeWithdraw() {
		assertFalse(account.withdraw(-0.77));
	}
	@Test
	public void fractionalWithdraw() {
		assertFalse(account.withdraw(2.599999));
	}
	
	// Deposits
	@Test
	public void smallDeposit() {
		assertTrue(account.deposit(0.11));
	}
	@Test
	public void largeDeposit() {
		assertTrue(account.deposit(2946385.71));
	}
	@Test
	public void zeroDeposit() {
		assertTrue(account.deposit(0.00));
	}
	@Test
	public void negativeDeposit() {
		assertFalse(account.deposit(-0.77));
	}
	@Test
	public void fractionalDeposit() {
		assertFalse(account.deposit(2.599999));
	}
	
	// Deposits then Withdrawals
	@Test
	public void depositThenWithdrawSome() {
		account.deposit(12.34);
		assertTrue(account.withdraw(2.13));
	}
	@Test
	public void depositThenOverdraft() {
		account.deposit(12.34);
		assertFalse(account.withdraw(3000.00));
	}
	@Test
	public void depositThenWithdrawNegative() {
		account.deposit(12.34);
		assertFalse(account.withdraw(-7.89));
	}
	@Test
	public void depositThenWithdrawFraction() {
		account.deposit(12.34);
		assertFalse(account.withdraw(3.141596));
	}

	@Test
	public void testPrintBalance() {
		account.deposit(31.4);
		account.setID(123);
		
		assertEquals("Your balance in Account #123 is: $31.40", account.printBalance());
	}
	@Test
	public void testToString() {
		account.deposit(31.40);
		account.setID(123);
		
		assertEquals("Account #123 has: $31.40\n" + 
				"", account.toString());
	}
	@Test
	public void testToStringAgain() {
		account.deposit(123456789);
		account.setID(12);
		
		assertEquals("Account #12\nBalance: $123,456,789.00\nOwners: myname", account.toString());
	}
	@Test
	public void testGetSetBalance() {
		account.setBalance(1.00);
		assertEquals(1.0, account.getBalance(), 0);
	}
	@Test
	public void testGetSetID() {
		account.setID(123);
		assertEquals(123, account.getID());
	}
	@Test
	public void testCompareTo() {
		account.setID(123);
		assertEquals(0, account.compareTo(account));
	}
	@Test
	public void testCompareToGreater() {
		account.setID(123);
		Account a = new Account();
		a.setID(1000);
		assertEquals(1, a.compareTo(account));
	}
	@Test
	public void testCompareToLess() {
		account.setID(123);
		Account a = new Account();
		a.setID(100);
		assertEquals(-1, a.compareTo(account));
	}
}
