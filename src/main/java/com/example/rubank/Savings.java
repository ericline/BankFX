package com.example.rubank;

/**
 * Represents a Savings account, a specific type of bank account.
 * This account may offer loyalty benefits based on customer status.
 * @author Eric Lin, Anish Mande
 */

public class Savings extends Account {
    protected boolean isLoyal; //loyal customer status
    private final AccountDatabase database;

    /**
     * Constructs a Savings account.
     *
     * @param number  Account number
     * @param holder  Account holder's profile
     */
    public Savings(AccountNumber number, Profile holder, double balance, AccountDatabase database) {
        super(number, holder, balance);
        this.database = database;
        checkLoyalty();
    }

    /**
     * Determines whether the account holder qualifies as a loyal customer.
     */
    public void checkLoyalty() {
        isLoyal = database.hasAccountType(holder, AccountType.CHECKING);
    }

    /**
     * Determines loyalty based on status.
     * @param status whether account is loyal.
     */
    public void setLoyal(boolean status)
    {
        isLoyal = status;
    }

    /**
     * Calculates the monthly interest earned on the savings account.
     * Base interest rate is 2.5% annually, with an additional 0.25% for loyal customers.
     */
    @Override
    public double interest() {
        double baseRate = 0.025; // 2.5% annual interest
        if (isLoyal) {
            baseRate = 0.0275; // Loyal customers get an additional 0.25%
        }
        return balance * baseRate / 12;
    }

    @Override
    public double dailyInterest() {
        double interest = interest();
        return interest * 12 / 365;
    }

    /**
     * Determines the monthly account fee.
     * Accounts with a balance of $500 or more do not incur a fee.
     * Otherwise, a $25 fee is applied.
     * @return The account fee amount.
     */
    @Override
    public double fee() {
        return balance >= 500 ? 0.0 : 25.0;
    }

    /**
     * Returns a string representation of the savings account.
     * If the account holder is loyal, "[LOYAL]" is appended to the string.
     * @return A formatted string representation of the account details.
     */
    @Override
    public String toString() {
        if (isLoyal) {
            return super.toString() + " [LOYAL]";
        }
        return super.toString();
    }
}

