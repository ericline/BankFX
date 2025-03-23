package com.example.rubank;

import util.Date;

/**
 * CertificateDeposit class to child of Savings. It holds
 * all the same information but includes an open date and
 * maturity date calculated using a defined term.
 *
 * @author Eric Lin, Anish Mande
 */

public class CertificateDeposit extends Savings {
    private int term;
    private Date open; // For computing the maturity date (open date + term)
    private Date maturityDate;

    /**
     * Constructs an Account object with an account number, holder, and balance.
     *
     * @param number  the unique account number
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     * @param term    the term of the account
     * @param open    the opening date
     */
    public CertificateDeposit(AccountNumber number, Profile holder, double balance, int term, Date open, AccountDatabase db) {
        super(number, holder, balance, db);
        this.term = term;
        this.open = open;
        this.isLoyal = false;
        this.maturityDate = calculateMaturityDate();
    }

    public double interest(Date closingDate) {
        int daysElapsed = daysBetween(open, closingDate);
        daysElapsed += 1;

        double applicableRate = 0.04;

        if (closingDate.compareTo(maturityDate) >= 0) {
            // If closed after maturity, use full interest rate
            return dailyInterest() * daysElapsed;
        }

        if (daysElapsed / 30.0 <= 6) {
            applicableRate = 0.03;
        } else if (daysElapsed / 30.0 <= 9) {
            applicableRate = 0.0325;
        } else if (daysElapsed / 30.0 < 12) {
            applicableRate = 0.035;
        }
        // Interest earned up to this point
        return balance * (applicableRate / 365) * daysElapsed;
    }

    /**
     * Calculates the monthly interest for the checking account.
     *
     * @return The updated balance after applying the monthly interest.
     */
    @Override
    public double interest() {
        double rate;
        switch (term) {
            case 3:
                rate = 0.03;
                break;
            case 6:
                rate = 0.0325;
                break;
            case 9:
                rate = 0.035;
                break;
            case 12:
                rate = 0.04;
                break;
            default:
                return -1.0;
        }
        return balance * rate / 12;
    }

    public double dailyInterest() {
        double interest = interest();
        return interest * 12 / 365;
    }

    /**
     * Calculates the monthly fee for the checking account.
     *
     * @return The account fee amount.
     */
    @Override
    public double fee() {
        return 0.0;
    }

    /**
     * Calculates the penalty if the CD is closed before maturity.
     * Uses a reduced interest rate based on days elapsed.
     * @param closingDate The date when the account is closed.
     * @return The penalty amount.
     */
    public double calculatePenalty(Date closingDate) {
        double interestEarned = interest(closingDate);
        // Penalty: 10% of earned interest
        return 0.1 * interestEarned;
    }


    private int daysBetween(Date start, Date end) {
        return Math.abs(toJulianDay(end) - toJulianDay(start));
    }

    private int toJulianDay(Date date) {
        int a = (14 - date.getMonth()) / 12;
        int y = date.getYear() + 4800 - a;
        int m = date.getMonth() + 12 * a - 3;
        return date.getDay() + ((153 * m + 2) / 5) + (365 * y) + (y / 4) - (y / 100) + (y / 400) - 32045;
    }

    private Date calculateMaturityDate() {
        int maturityMonth = open.getMonth() + term;
        int maturityYear = open.getYear();
        while (maturityMonth > 12) {
            maturityMonth -= 12;
            maturityYear++;
        }

        int maturityDay = open.getDay();
        Date tentativeDate = new Date(maturityMonth + "/" + maturityDay + "/" + maturityYear);

        if (!tentativeDate.isValid()) {
            maturityDay = Date.getLastDayOfMonth(maturityMonth, maturityYear);
            tentativeDate = new Date(maturityMonth + "/" + maturityDay + "/" + maturityYear);
        }

        return tentativeDate;

    }

    public Date getmaturityDate() {
        return maturityDate;
    }

    @Override
    public String toString() {
        return super.toString() + " Term[" + term + "] " +
                "Date opened[" + open + "] " + "Maturity date[" + maturityDate + "]";
    }
}
