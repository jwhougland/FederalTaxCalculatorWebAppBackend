package com.jack.tax.models.interfaces;

/**
 * Defines the behavior that a concrete Bracket Details class must contain.
 */
public interface BracketDetails {

    /**
     * Returns the tax year applicable to the bracket
     */
    public int getTaxYear();

    /**
     * Returns the tax rate (%) associated with a bracket
     */
    public int getTaxRate();

    /**
     * Returns the minimum income (USD) for a single filer in a tax bracket
     */
    public int getMinIncomeSingle();

    /**
     * Returns the maximum income (USD) for a single filer in a tax bracket
     */
    public int getMaxIncomeSingle();

    /**
     * Returns the minimum income (USD) for a married-filing-jointly filer in a tax bracket
     */
    public int getMinIncomeMFJ();

    /**
     * Returns the maximum income (USD) for a married-filing-jointly filer in a tax bracket
     */
    public int getMaxIncomeMFJ();

    /**
     * Returns the minimum income (USD) for a married-filing-separately filer in a tax bracket
     */
    public int getMinIncomeMFS();

    /**
     * Returns the maximum income (USD) for a married-filing-separately filer in a tax bracket
     */
    public int getMaxIncomeMFS();

    /**
     * Returns the minimum income (USD) for a head-of-household filer in a tax bracket
     */
    public int getMinIncomeHOH();

    /**
     * Returns the maximum income (USD) for a head-of-household filer in a tax bracket
     */
    public int getMaxIncomeHOH();

}
