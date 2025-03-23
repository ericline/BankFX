package com.example.rubank;

/**
 * Represents a Checking account, a specific type of bank account.
 * This class extends the Account class and includes methods for calculating
 * interest and account fees specific to checking accounts.
 * @author Eric Lin, Anish Mande
 */

public class Checking extends Account {
    /**
     * Constructs an Account object with an account number, holder, and balance.
     *
     * @param number  the unique account number
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     */
    public Checking(AccountNumber number, Profile holder, double balance) {
        super(number, holder, balance);
    }

    /**
     * Calculates the monthly interest for the checking account.
     * Interest is compounded monthly based on a fixed rate of 1.15% per year.
     * @return The updated balance after applying the monthly interest.
     */
    @Override
    public double interest() {
        return balance * 0.015 / 12;
    }

    public double dailyInterest() {
        return balance * 0.015 / 365;
    }

    /**
     * Calculates the monthly fee for the checking account.
     * College checking accounts have no fee, while all other checking accounts have a $15 fee.
     * @return The account fee amount.
     */
    public double fee() {
        if (number.getType().name().equals("COLLEGE_CHECKING")) {
            return 0.0;
        }
        return 15.0;

    }
}
