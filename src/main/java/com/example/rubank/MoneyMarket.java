package com.example.rubank;

/**
 * Represents a Money Market savings account, a specialized type of savings account.
 * This account offers higher interest rates based on balance and imposes fees
 * based on balance and the number of withdrawals.
 * @author Eric Lin, Anish Mande
 */

public class MoneyMarket extends Savings {
    private int withdrawals;

    /**
     * Constructs a Money Market savings account with an account number, holder, balance, and withdrawal count.
     * @param number       The unique account number associated with this money market account.
     * @param holder       The profile of the account holder.
     * @param balance      The initial balance of the account.
     * @param withdrawals  The initial number of withdrawals made.
     */
    public MoneyMarket(AccountNumber number, Profile holder, double balance, int withdrawals, AccountDatabase db) {
        super(number, holder, balance, db);
        this.withdrawals = withdrawals; //number of withdrawals
    }

    /**
     * Determines whether the account holder qualifies as a loyal customer.
     * An account holder is considered loyal if their balance is at least $5000.
     */
    @Override
    public void checkLoyalty() {
        this.isLoyal = balance >= 5000;
    }

    /**
     * Withdraws a specified amount from the account, updating withdrawal count.
     * If the withdrawal is successful, the loyalty status is reassessed.
     * @param amount The amount to withdraw.
     */
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            super.withdraw(amount);
            withdrawals++;
        }
        checkLoyalty();
    }

    /**
     * Deposits a specified amount into the account and reassesses loyalty status.
     * @param amount The amount to deposit.
     */
    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            super.deposit(amount);
        }
        checkLoyalty();
    }

    /**
     * Calculates the monthly interest earned on the balance.
     * Base rate is 3.5%, with an additional 0.25% for balances of $5000 or more.
     * @return The calculated interest amount.
     */
    @Override
    public double interest() {
        checkLoyalty();
        double baseRate = 0.035;
        if (isLoyal) {
            baseRate = 0.0375;
        }

        return balance * baseRate / 12;
    }

    @Override
    public double dailyInterest() {
        double interest = interest();
        return interest * 12 / 365;
    }

    public int getWithdrawals() {
        return withdrawals;
    }

    /**
     * Calculates the monthly account fee.
     * If the balance is below $2000, a $25 fee is applied.
     * If more than 3 withdrawals are made, an additional $10 fee is applied.
     * @return The total fee amount.
     */
    @Override
    public double fee() {
        double total = 0;
        if (balance < 2000) {
            total += 25.0;
        }
        if (withdrawals > 3) {
            total += 10.0;
            return total;
        }
        else {
            return total;
        }
    }

    /**
     * Returns a string representation of the money market account,
     * including the number of withdrawals.
     * @return A formatted string representation of the account details.
     */
    @Override
    public String toString() {
        return super.toString() + " Withdrawal[" + withdrawals + "]";
    }
}
