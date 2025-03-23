package com.example.rubank;

import org.junit.Test;
import util.Date;

import static org.junit.Assert.*;

/**
 * Test class to unit test Profile class with JUnit tests.
 * @author Eric Lin Anish Mande
 */

public class ProfileUnitTest {
    private Profile makeProfile(String fname, String lname, String dateString) {
        Date dob = new Date(dateString);
        return new Profile(fname, lname, dob);
    }

    @Test
    public void testCompareTo_returnsNegative1() {
        Profile p1 = makeProfile("John", "Doe", "2/19/2000");
        Profile p2 = makeProfile("John", "Smith", "2/19/2000");
        assertTrue(p1.compareTo(p2) < 0);

        Profile p3 = makeProfile("Amy", "Doe", "2/19/2000");
        Profile p4 = makeProfile("John", "Doe", "2/19/2000");
        assertTrue(p3.compareTo(p4) < 0);

        Profile p5 = makeProfile("John", "Doe", "2/18/2000");
        Profile p6 = makeProfile("John", "Doe", "2/19/2000");
        assertTrue(p5.compareTo(p6) < 0);
    }

    @Test
    public void testCompareTo_returnsPositive1() {
        // Case 1: Last name is later alphabetically
        Profile p1 = makeProfile("John", "Smith", "2/19/2000");
        Profile p2 = makeProfile("John", "Doe", "2/19/2000");
        assertTrue(p1.compareTo(p2) > 0);

        Profile p3 = makeProfile("Zach", "Doe", "2/19/2000");
        Profile p4 = makeProfile("Amy", "Doe", "2/19/2000");
        assertTrue(p3.compareTo(p4) > 0);

        Profile p5 = makeProfile("John", "Doe", "2/20/2000");
        Profile p6 = makeProfile("John", "Doe", "2/19/2000");
        assertTrue(p5.compareTo(p6) > 0);
    }

    @Test
    public void testCompareTo_returnsZero() {
        Profile p1 = makeProfile("John", "Doe", "2/19/2000");
        Profile p2 = makeProfile("John", "Doe", "2/19/2000");
        assertEquals(0, p1.compareTo(p2));
    }
}
