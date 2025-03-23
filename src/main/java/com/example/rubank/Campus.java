package com.example.rubank;

/**
 * Enum representing different Rutgers University campuses.
 * Each campus is associated with a unique string code.
 * @author Eric Lin, Anish Mande
 */
public enum Campus {
    NEW_BRUNSWICK("1"),
    NEWARK("2"),
    CAMDEN("3");


    private final String campusCode;

    /**
     * Constructor for the Campus enum.
     * Associates a campus with its respective code.
     * @param campusCode The unique code representing a campus.
     */
    Campus(String campusCode) {
        this.campusCode = campusCode;
    }

    /**
     * Retrieves the corresponding Campus enum from a given string code.
     * @param code The campus code as a string ("1", "2", "3").
     * @return The corresponding Campus enum.
     * @throws IllegalArgumentException If the code does not match any campus.
     */
    public static Campus fromCode(String code) {
        for (Campus campus : Campus.values()) {
            if (campus.campusCode.equals(code)) {
                return campus;
            }
        }
        throw new IllegalArgumentException("Invalid campus code: " + code);
    }
}
