package com.example.rubank;

import util.List;

import java.text.DecimalFormat;

/**
 Account class to represent information about an
 account holder and the account balance.
 @author Eric Lin, Anish Mande
 */
public abstract class Account implements Comparable<Account> {
    protected AccountNumber number;
    protected Profile holder;
    protected double balance;
    protected List<Activity> activities; //list of account activities (D or W)
    DecimalFormat formatter = new DecimalFormat("$#,##0.00");

    public final void statement(Controller controller) {
        printActivities(controller); //private helper method
        double interest = interest(); //polymorphism based on actual type
        double fee = fee(); //polymorphism based on actual type
        printInterestFee(interest, fee, controller); //private helper method
        printBalance(interest, fee, controller); //private helper method
    }

    //add account activity, D or W
    public void addActivity(Activity activity) {
        if (activities == null) {
            activities = new List<>();
        }
        if (activity.getType() == 'D' || activity.getType() == 'W') {
            activities.add(activity);
        }
    }
    public abstract double interest();
    public abstract double fee();
    public abstract double dailyInterest();

    private void printActivities(Controller controller) {
        if (activities == null || activities.isEmpty()) {
            return; // No activities to print
        }
        controller.print("\t[Activity]");
        for (Activity activity : activities) {
            controller.println("\t\t\t" + activity.toString());
        }
    }
    private void printInterestFee(double interest, double fee, Controller controller) {
        String output = String.format("\t[Interest] $%.2f [Fee] $%.2f", interest, fee);
        controller.println(output);
    }
    private void printBalance(double interest, double fee, Controller controller) {
        balance += interest - fee;
        String output = String.format("\t[Balance] $%.2f", balance);
        controller.println(output);
    }

    /**
     * Gets the interest of an account
     * @return the interest
     */
    public double getInterest() {
        return interest();
    }

    /**
     * Constructs an Account object with an account number, holder, and balance.
     * @param number  the unique account number
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     */
    public Account(AccountNumber number, Profile holder, double balance) {
        this.number = number;
        this.holder = holder;
        this.balance = balance;
    }

    /**
     * Retrieves the account number.
     * @return the account number
     */
    public AccountNumber getNumber() {
        return number;
    }

    /**
     * Retrieves the profile of the account holder.
     * @return the profile of the account holder
     */
    public Profile getHolder() {
        return holder;
    }

    /**
     * Retrieves the account balance.
     * @return the current account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Withdraws a specified amount from the account, reducing the balance.
     * The withdrawal is only processed if the amount is greater than zero and does not exceed the balance.
     * @param amount the amount to withdraw
     */
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }

    /**
     * Deposits a specified amount into the account, increasing the balance.
     * The deposit is only processed if the amount is greater than zero.
     * @param amount the amount to deposit
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    /**
     * Compares this account to another account based on the account number.
     * @param other the account to compare to
     * @return a negative integer, zero, or a positive integer as this account
     *         is less than, equal to, or greater than the specified account.
     */
    @Override
    public int compareTo(Account other) {
        return this.number.compareTo(other.number);
    }

    /**
     * Determines whether this account is equal to another object.
     * Two accounts are considered equal if they have the same account number.
     * @param obj the object to compare
     * @return true if the accounts have the same number, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Account other = (Account)obj;
        return this.number.equals(other.number);
    }

    /**
     * Returns a string representation of the account in the specified format:
     * "Account#[account_number] Holder[holder_name DOB] Balance[$amount] Branch [branch]"
     * @return a formatted string representing the account
     */
    @Override
    public String toString() {
        return String.format("Account#[%s] Holder[%s] Balance[%s] Branch[%s]",
                number, holder.toString(), formatter.format(balance), number.getBranch().name());
    }
}
