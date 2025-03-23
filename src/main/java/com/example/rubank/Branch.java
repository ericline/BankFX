package com.example.rubank;
/**
 Branch class to represent information about a
 branch including its code and country location and zip.
 @author Eric Lin, Anish Mande
 */
public enum Branch {

    EDISON("100","Middlesex","08817"),
    BRIDGEWATER("200","Somerset","08807"),
    PRINCETON("300","Mercer","08542"),
    PISCATAWAY("400","Middlesex","08854"),
    WARREN("500","Somerset","07057");

    private final String branchCode;
    private final String county;
    private final String zip;

    /**
     * Constructs a Branch enum with a branch code, county, and zip code.
     * @param branchCode the unique code identifying the branch
     * @param county the county in which the branch is located
     * @param zip the zip code of the branch location
     */
    Branch(String branchCode, String county, String zip) {
        this.branchCode = branchCode;
        this.county = county;
        this.zip = zip;
    }

    /**
     * Retrieves the unique branch code.
     * @return the branch code as a string
     */
    public String getCode() {
        return branchCode;
    }

    /**
     * Retrieves the county in which the branch is located.
     * @return the county name
     */
    public String getCounty() {
        return county;
    }

    /**
     * Retrieves the zip code of the branch.
     * @return the zip code as a string
     */
    public String getZip() {
        return zip;
    }

    /**
     * Returns the corresponding Branch enum from a given string code.
     * @param code The branch code as a string.
     * @return The corresponding branch enum.
     */
    public static Branch fromCode(String code) {
        for (Branch branch : Branch.values()) {
            if (branch.branchCode.equals(code)) {
                return branch;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    /**
     * Returns the name of the branch in a readable format.
     * The first letter is capitalized, and the rest are in lowercase.
     * @return the formatted branch name
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
