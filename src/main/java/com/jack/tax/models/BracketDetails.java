package com.jack.tax.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Encapsulates the min/max income ranges for tax brackets
 * for all types of filers.  Each instance represents a tax year.
 */
@Document(collection = "Brackets")
public class BracketDetails implements com.jack.tax.models.interfaces.BracketDetails {

    /**
     * Applicable tax year
     */
    private int taxYear;

    /**
     * Marginal tax rate for the bracket
     */
    private int taxRate;

    /**
     * Min income (USD) - single filer
     */
    private int minIncomeSingle;

    /**
     * Max income (USD) - single filer
     */
    private int maxIncomeSingle;

    /**
     * Min income (USD) - married filing jointly (or surviving spouse) filer
     */
    private int minIncomeMFJ;

    /**
     * Max income (USD) - married filing jointly (or surving spouse) filer
     */
    private int maxIncomeMFJ;

    /**
     * Min income (USD) - married filing separately filer
     */
    private int minIncomeMFS;

    /**
     * Max income (USD) - married filing separately filer
     */
    private int maxIncomeMFS;

    /**
     * Min income (USD) - head of household filer
     */
    private int minIncomeHOH;

    /**
     * Max income (USD) - head of household filer
     */
    private int maxIncomeHOH;

    /**
     * Returns the tax year applicable to the bracket
     */
    @Override
    public int getTaxYear() {
        return taxYear;
    }

    /**
     * Returns the tax rate (%) associated with a bracket
     */
    @Override
    public int getTaxRate() {
        return taxRate;
    }

    /**
     * Returns the minimum income (USD) for a single filer in a tax bracket
     */
    @Override
    public int getMinIncomeSingle() {
        return minIncomeSingle;
    }

    /**
     * Returns the maximum income (USD) for a single filer in a tax bracket
     */
    @Override
    public int getMaxIncomeSingle() {
        return maxIncomeSingle;
    }

    /**
     * Returns the minimum income (USD) for a married-filing-jointly filer in a tax bracket
     */
    @Override
    public int getMinIncomeMFJ() {
        return minIncomeMFJ;
    }

    /**
     * Returns the maximum income (USD) for a married-filing-jointly filer in a tax bracket
     */
    @Override
    public int getMaxIncomeMFJ() {
        return maxIncomeMFJ;
    }

    /**
     * Returns the minimum income (USD) for a married-filing-separately filer in a tax bracket
     */
    @Override
    public int getMinIncomeMFS() {
        return minIncomeMFS;
    }

    /**
     * Returns the maximum income (USD) for a married-filing-separately filer in a tax bracket
     */
    @Override
    public int getMaxIncomeMFS() {
        return maxIncomeMFS;
    }

    /**
     * Returns the minimum income (USD) for a head-of-household filer in a tax bracket
     */
    @Override
    public int getMinIncomeHOH() {
        return minIncomeHOH;
    }

    /**
     * Returns the maximum income (USD) for a head-of-household filer in a tax bracket
     */
    @Override
    public int getMaxIncomeHOH() {
        return minIncomeHOH;
    }
}
