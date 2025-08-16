package com.jack.tax.models.interfaces;

/**
 * Contains the behavior that a concrete standard deduction details class must contain.
 */
public interface StandardDeductionDetails {

    /**
     * Returns the tax year applicable to the standard deduction
     */
    public int getTaxYear();

    /**
     * Returns the standard deduction (USD) for a single filer
     */
    public int getSingle();

    /**
     * Returns the standard deduction (USD) for married-filing-jointly filers
     */
    public int getMfj();

    /**
     * Returns the standard deduction (USD) for a married-filing-separately filer
     */
    public int getMfs();

    /**
     * Returns the standard deduction (USD) for a head-of-household filer
     */
    public int getHoh();
}
