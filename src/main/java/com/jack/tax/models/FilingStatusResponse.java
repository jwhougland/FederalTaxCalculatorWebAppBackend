package com.jack.tax.models;

/**
 * Wrapper for a filing status enum
 */
public class FilingStatusResponse implements com.jack.tax.models.interfaces.FilingStatusResponse {

    private String code;
    private String description;

    /**
     * Creates a fully initialized filing status response using the given data.
     *
     * @param filingStatus Filing status enum
     */
    public FilingStatusResponse(FilingStatus filingStatus) {
        this.code = filingStatus.name();
        this.description = filingStatus.getDescription();
    }


    /**
     * Returns an enum value as a string
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * Returns the human-readable description for an enum.
     */
    @Override
    public String getDescription() {
        return description;
    }
}
