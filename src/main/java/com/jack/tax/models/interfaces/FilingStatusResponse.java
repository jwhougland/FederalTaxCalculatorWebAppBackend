package com.jack.tax.models.interfaces;

/**
 * Defines the behavior a concrete filing status response must contain.
 */
public interface FilingStatusResponse {

    /**
     * Returns an enum value as a string
     */
    public String getCode();

    /**
     * Returns the human-readable description for an enum.
     */
    public String getDescription();
}
