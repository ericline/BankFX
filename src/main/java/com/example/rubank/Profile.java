package com.example.rubank;

import util.Date;

/**
 Profile class to represent identifying information
 about an account holder.
 @author Eric Lin, Anish Mande
 */
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructs a Profile with the specified first name, last name, and date of birth.
     * @param fname First name of the profile holder
     * @param lname Last name of the profile holder
     * @param dob Date of birth of the profile holder
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Retrieves the first name of the profile holder.
     * @return First name as a string
     */
    public String getFname() {
        return fname;
    }

    /**
     * Retrieves the last name of the profile holder.
     * @return Last name as a string
     */
    public String getLname() {
        return lname;
    }

    /**
     * Retrieves the date of birth of the profile holder.
     * @return Date of birth as a Date object
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Compares this profile with another profile for sorting.
     * Sorting priority: last name -> first name -> date of birth.
     * @param other The profile to compare with
     * @return Negative if this profile comes before, positive if it comes after, 0 if equal
     */
    @Override
    public int compareTo(Profile other) {
        int lastNameComparison = this.lname.compareToIgnoreCase(other.lname);
        if (lastNameComparison != 0) {
            return Integer.compare(lastNameComparison, 0);
        }
        int firstNameComparison = this.fname.compareToIgnoreCase(other.fname);
        if (firstNameComparison != 0) {
            return Integer.compare(firstNameComparison, 0);
        }
        return this.dob.compareTo(other.dob);

    }

    /**
     * Returns a string representation of the profile in "First Last DOB" format.
     * @return Formatted string representation of the profile
     */
    @Override
    public String toString() {
        return String.format("%s %s %s", fname, lname, dob);
    }

    /**
     * Compares this Profile to another object for equality.
     * Two Profile objects are considered equal if they have the same first name.
     * @param other The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Profile otherProfile) {
            return this.fname.equalsIgnoreCase(otherProfile.fname) &&
                    this.lname.equalsIgnoreCase(otherProfile.lname) &&
                    this.dob.equals(otherProfile.dob);
        }
        return false;
    }
}
