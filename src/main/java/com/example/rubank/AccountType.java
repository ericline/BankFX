package com.example.rubank;
/**
 AccountType enum to represent the types of accounts
 and their corresponding codes.
 @author Eric Lin, Anish Mande
 */
public enum AccountType {
    CHECKING("01"),
    SAVINGS("02"),
    MONEY_MARKET("03"),
    COLLEGE_CHECKING ("04"),
    CD ("05");

    private String code;

    /**
     * Constructs an AccountType enum with a specified code.
     * @param code the unique code representing the account type
     */
    AccountType(String code) {
        this.code = code;
    }

    /**
     * Retrieves the code associated with the account type.
     * @return the account type code as a string
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the corresponding AccountType enum from a given string code.
     * @param typecode The AccountType code as a string.
     * @return The corresponding AccountType enum.
     */
    public static AccountType fromCode(String typecode) {
        for (AccountType type : AccountType.values()) {
            if (type.code.equals(typecode)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + typecode);
    }
}
