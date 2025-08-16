package com.jack.tax.models;

/**
 * Filing status enumeration
 */
public enum FilingStatus {

    SINGLE("Single"),
    MARRIED_FILING_JOINTLY("Married Filing Jointly"),
    SURVIVING_SPOUSE("Surviving Spouse"),
    MARRIED_FILING_SEPARATELY("Married Filing Separately"),
    HEAD_OF_HOUSEHOLD("Head of Household");

    private final String description;

    /**
     * Initializes the enum with a description
     * @param description Description for the enum
     */
    FilingStatus(String description) {
        this.description = description;
    }

    /**
     * Returns the description for the enum
     */
    public String getDescription() {
        return description;
    }
}
