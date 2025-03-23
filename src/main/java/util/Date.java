package util;
import java.util.Calendar;

/**
 Date class to represent the year, month, and day
 of a given date.
 @author Eric Lin, Anish Mande
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;
    public static final int QUADYEAR = 4;
    public static final int CENTURY = 100;
    public static final int QUADCENTURY = 400;

    /**
     * Constructor to create a Date object from a String in the format MM/DD/YYYY.
     * @param date A string representing the date in MM/DD/YYYY format.
     */
    public Date(String date) {
        String[] tokens = date.split("/");
        this.month = Integer.parseInt(tokens[0]);
        this.day = Integer.parseInt(tokens[1]);
        this.year = Integer.parseInt(tokens[2]);
    }

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date() {
        Calendar calendar = Calendar.getInstance();
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.year = calendar.get(Calendar.YEAR);
    }

    /**
     * Retrieves the year of the date.
     * @return The year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Retrieves the month of the date.
     * @return The month.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Retrieves the day of the date.
     * @return The day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Checks if the date is a valid calendar date.
     * Uses Java's LocalDate class for validation.
     * @return true if the date is valid, false otherwise.
     */
    public boolean isValid() {
            // Invalid year, month, or day
            if (year <= 0 || month < 1 || month > 12 || day < 1 || day > 31) {
                return false;
            }

            // Define days per month (non-leap year)
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            // Adjust for leap years in February
            if (isLeapYear(year)) {
                daysInMonth[1] = 29;
            }

            // Check if day is valid for the given month
            return day <= daysInMonth[month - 1];
    }

    /**
     * Returns the last valid day of a given month in a given year.
     * Accounts for leap years in February.
     * @param month The month (1-12).
     * @param year The year (e.g., 2024).
     * @return The last valid day of the given month.
     */
    public static int getLastDayOfMonth(int month, int year) {
        return switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> 31; // Months with 31 days
            case 4, 6, 9, 11 -> 30; // Months with 30 days
            case 2 -> (isLeapYear(year)) ? 29 : 28;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }
    /**
     * Checks if year is leap year.
     * @return true if the year is a leap year, false otherwise.
     */
    private static boolean isLeapYear(int year) {
        return (year % QUADYEAR == 0 && (year % CENTURY != 0 || year % QUADCENTURY == 0));
    }

    /**
     * Checks if date = dob, if they are over 18 years old.
     * @return true if over 18 years old, false otherwise.
     */
    public boolean isOver18()
    {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        birthDate.set(year, month - 1, day);
        today.add(Calendar.YEAR, -18);

        return !birthDate.after(today);
    }

    /**
     * Checks if date = dob, if they are over 18 years old.
     * @return true if over 18 years old, false otherwise.
     */
    public boolean isOver24()
    {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        birthDate.set(year, month - 1, day);
        today.add(Calendar.YEAR, -24);

        return !birthDate.after(today);
    }

    /**
     * Compares this Date object with another Date object.
     * Dates are compared by year, then month, then day.
     * @param other The Date object to be compared.
     * @return A negative integer, zero, or a positive integer
     *         if this date is earlier than, equal to, or later than the other date.
     */
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    /**
     * Converts the Date object to a string representation in the format YYYY/MM/DD.
     * @return A string representation of the date.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Compares if two dates are equal.
     * @param obj the object to compare
     * @return true if the accounts have the same number, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Date date = (Date) obj;

        if (this.year != date.year)
            return false;
        if (this.month != date.month)
            return false;
        return this.day == date.day;
    }
}
