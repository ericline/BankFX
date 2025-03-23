package com.example.rubank;

import org.junit.Before;
import org.junit.Test;
import util.Date;

import static org.junit.Assert.*;

/**
 * Test class to unit test AccountDatabase with JUnit tests.
 * @author Eric Lin Anish Mande
 */

public class AccountDatabaseUnitTest {

    private AccountDatabase database;

    @Before
    public void setUp() {
        database = new AccountDatabase();

        Profile amyLin = new Profile("Amy", "Lin", new Date("1/2/2002"));
        Profile johnDoe = new Profile("John", "Doe", new Date("2/19/2000"));
        Profile janeSmith = new Profile("Jane", "Smith", new Date("3/15/1995"));

        AccountNumber collegeNum = new AccountNumber(Branch.PISCATAWAY, AccountType.COLLEGE_CHECKING);
        AccountNumber checkingNum = new AccountNumber(Branch.EDISON, AccountType.CHECKING);
        AccountNumber moneyMarketNum = new AccountNumber(Branch.BRIDGEWATER, AccountType.MONEY_MARKET);

        CollegeChecking collegeChecking = new CollegeChecking(collegeNum, amyLin, 550, Campus.NEW_BRUNSWICK);
        Checking checkingAccount = new Checking(checkingNum, johnDoe, 1000.00);
        MoneyMarket moneyMarketAccount = new MoneyMarket(moneyMarketNum, janeSmith, 4000.00, 0, database); // Below $5,000

        database.add(collegeChecking);
        database.add(checkingAccount);
        database.add(moneyMarketAccount);
    }

    @Test
    public void testDeposit_anyAccountType() {
        assertTrue(database.deposit(database.get(1).getNumber().toString(), 500.00));
        assertEquals(1500.00, database.get(1).getBalance(), 0.01);
    }

    @Test
    public void testDeposit_moneyMarketAccount_thresholdIncrease() {
        assertTrue(database.deposit(database.get(2).getNumber().toString(), 1500.00));
        assertEquals(5500.00, database.get(2).getBalance(), 0.01);
        MoneyMarket mmAccount = (MoneyMarket) database.get(2);
        assertTrue(mmAccount.isLoyal);
    }

    @Test
    public void testWithdraw_validWithdrawal() {
        assertTrue(database.withdraw(database.get(1).getNumber().toString(), 200.00));
        assertEquals(800.00, database.get(1).getBalance(), 0.01);
    }

    @Test
    public void testWithdraw_insufficientFunds() {
        boolean result = database.withdraw(database.get(1).getNumber().toString(), 2000.00);
        assertFalse("Expected withdrawal to fail due to insufficient funds", result);
        assertEquals(1000.00, database.get(1).getBalance(), 0.01); // Balance should remain unchanged
    }

    @Test
    public void testWithdraw_moneyMarketAccount_thresholdDecrease() {
        assertTrue(database.withdraw(database.get(2).getNumber().toString(), 3000.00));
        MoneyMarket mmAccount = (MoneyMarket) database.get(2);
        assertFalse(mmAccount.isLoyal);
        double expectedBalance = 1000.00;
        assertEquals(expectedBalance, mmAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollegeCheckingAccount() {
        assertEquals(3, database.size());
        Account account = database.get(0);
        assertTrue(account instanceof CollegeChecking);
        assertEquals(550.00, account.getBalance(), 0.01);
        assertEquals(AccountType.COLLEGE_CHECKING, account.getNumber().getType());
    }
}
