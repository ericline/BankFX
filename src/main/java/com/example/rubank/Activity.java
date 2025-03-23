package com.example.rubank;

import util.Date;

import java.text.DecimalFormat;

/**
 * Represents a banking activity such as a deposit or withdrawal.
 * Stores details including date, location, type, amount, and ATM usage.
 * @author Eric Lin, Anish Mande
 */
public class Activity implements Comparable<Activity> {
    private Date date;
    private Branch location; //the location of the activity
    private char type; //D or W
    private double amount;
    private boolean atm; //true if this is made at an ATM (from the text file)

    /**
     * Constructs an Activity object with the specified details.
     * @param date     the date of the transaction
     * @param location the branch location of the transaction
     * @param type     the type of transaction ('D' for deposit, 'W' for withdrawal)
     * @param amount   the transaction amount
     * @param atm      whether the transaction was performed at an ATM
     */
    public Activity(Date date, Branch location, char type, double amount, boolean atm) {
        this.date = date;
        this.location = location;
        this.type = type;
        this.amount = amount;
        this.atm = atm;
    }

    /**
     * Retrieves the transaction type ('D' for deposit, 'W' for withdrawal).
     * @return the transaction type
     */
    public char getType() {
        return type;
    }

    /**
     * Compares this activity with another activity.
     * Sorting is primarily based on date; if dates are equal, amount is compared.
     * @param other the activity to compare with
     * @return a negative, zero, or positive integer as this activity is less than, equal to, or greater than the specified activity
     */
    @Override
    public int compareTo(Activity other) {
        int compareDate = this.date.compareTo(other.date);
        if (compareDate != 0) {
            return compareDate;
        }
        return Double.compare(this.amount, other.amount);
    }

    /**
     * Checks if this activity is equal to another object.
     * Two activities are considered equal if all their attributes match.
     * @param obj the object to compare
     * @return true if both activities are identical, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Activity other = (Activity) obj;
        return this.date.equals(other.date) &&
                this.location.equals(other.location) &&
                this.type == other.type &&
                Double.compare(this.amount, other.amount) == 0 &&
                this.atm == other.atm;
    }

    /**
     * Returns a formatted string representation of the activity.
     * Format: [Date]::[Branch]::[ATM flag]::[Transaction Type]::[Amount]
     * @return a formatted string representing the activity
     */
    @Override
    public String toString() {
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String transactionType = "deposit";
        if (type == 'W')
            transactionType = "withdrawal";
        if (atm) {
            return String.format("%s::%s%s::%s::%s",
                    date.toString(),
                    location.name().toUpperCase(),
                    "[ATM]",
                    transactionType,
                    currencyFormat.format(amount));
        }
        else {
            return String.format("%s::%s::%s::%s",
                    date.toString(),
                    location.name(),
                    transactionType,
                    currencyFormat.format(amount));
        }
    }
}
