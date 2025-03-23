package com.example.rubank;

/**
 * Represents a College Checking account, a specialized type of Checking account.
 * This account is associated with a specific campus and has no fees or interest.
 * @author Eric Lin, Anish Mande
 */
public class CollegeChecking extends Checking {
    private Campus campus;


    /**
     * Constructs a College Checking account with an account number, holder, balance, and campus.
     * @param number  The unique account number associated with this college checking account.
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     * @param campus  The campus associated with this college checking account.
     */
    public CollegeChecking(AccountNumber number, Profile holder, double balance, Campus campus) {
        super(number, holder, balance);
        this.campus = campus;
    }

    /**
     * Returns the monthly fee for a college checking account.
     * College checking accounts do not have fees.
     * @return 0.0 since there is no fee.
     */
    @Override
    public double fee() {
        return 0.0; 
    }

    /**
     * Returns a string representation of the college checking account.
     * Includes the base checking account information along with the associated campus.
     * @return A formatted string representation of the account details.
     */
    @Override
    public String toString() {
        return super.toString() + " Campus[" + campus + "]";
    }
}
