package com.bankdemo.testmarkbank.services_test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import services.UserInterface;

@RunWith(Suite.class)
@SuiteClasses({ AccountTest.class, AcctDBTest.class, LoggingUtilTest.class, UserDBTest.class, UserInterface.class, UserTest.class })
public class AllTests {

}
