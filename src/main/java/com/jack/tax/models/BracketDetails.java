package com.jack.tax.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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
     * Sets the tax year field with the given data
     * @param taxYear Tax year to set
     */
    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    /**
     * Returns the marginal tax rate (%) associated with a bracket
     */
    @Override
    public int getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the marginal tax rate (%) with the given data
     * @param taxRate Marginal tax rate (%) to set
     */
    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * Returns the minimum income (USD) for a single filer in a tax bracket
     */
    @Override
    public int getMinIncomeSingle() {
        return minIncomeSingle;
    }

    /**
     * Sets the min income (USD) for a single filer for a tax bracket with the given data
     * @param minIncomeSingle Min income (USD) for a single filer to set
     */
    public void setMinIncomeSingle(int minIncomeSingle) {
        this.minIncomeSingle = minIncomeSingle;
    }

    /**
     * Returns the maximum income (USD) for a single filer in a tax bracket
     */
    @Override
    public int getMaxIncomeSingle() {
        return maxIncomeSingle;
    }

    /**
     * Sets the max income (USD) for a single filer for a tax bracket with the given data
     * @param maxIncomeSingle Max income (USD) for a single filer to set
     */
    public void setMaxIncomeSingle(int maxIncomeSingle) {
        this.maxIncomeSingle = maxIncomeSingle;
    }

    /**
     * Returns the minimum income (USD) for a married-filing-jointly filer in a tax bracket
     */
    @Override
    public int getMinIncomeMFJ() {
        return minIncomeMFJ;
    }

    /**
     * Sets the min income (USD) for married-filing-jointly filers for a tax bracket with the given data
     * @param minIncomeMFJ Min income (USD) for married-filing-jointly filers to set
     */
    public void setMinIncomeMFJ(int minIncomeMFJ) {
        this.minIncomeMFJ = minIncomeMFJ;
    }

    /**
     * Returns the maximum income (USD) for a married-filing-jointly filer in a tax bracket
     */
    @Override
    public int getMaxIncomeMFJ() {
        return maxIncomeMFJ;
    }

    /**
     * Sets the max income (USD) for married-filing-jointly filers for a tax bracket with the given data
     * @param maxIncomeMFJ Max income (USD) for married-filing-jointly filers to set
     */
    public void setMaxIncomeMFJ(int maxIncomeMFJ) {
        this.maxIncomeMFJ = maxIncomeMFJ;
    }

    /**
     * Returns the minimum income (USD) for a married-filing-separately filer in a tax bracket
     */
    @Override
    public int getMinIncomeMFS() {
        return minIncomeMFS;
    }

    /**
     * Sets the min income (USD) for a married-filing-separately filer for a tax bracket with the given data
     * @param minIncomeMFS Min income (USD) for a married-filing-separately filer to set
     */
    public void setMinIncomeMFS(int minIncomeMFS) {
        this.minIncomeMFS = minIncomeMFS;
    }

    /**
     * Returns the maximum income (USD) for a married-filing-separately filer in a tax bracket
     */
    @Override
    public int getMaxIncomeMFS() {
        return maxIncomeMFS;
    }

    /**
     * Sets the max income (USD) for a married-filing-separately filer for a tax bracket with the given data
     * @param maxIncomeMFS Max income (USD) for a married-filing-separately filer to set
     */
    public void setMaxIncomeMFS(int maxIncomeMFS) {
        this.maxIncomeMFS = maxIncomeMFS;
    }

    /**
     * Returns the minimum income (USD) for a head-of-household filer in a tax bracket
     */
    @Override
    public int getMinIncomeHOH() {
        return minIncomeHOH;
    }

    /**
     * Sets the min income (USD) for a head-of-household filer for a tax bracket with the given data
     * @param minIncomeHOH Min income (USD) for a head-of-household filer to set
     */
    public void setMinIncomeHOH(int minIncomeHOH) {
        this.minIncomeHOH = minIncomeHOH;
    }

    /**
     * Returns the maximum income (USD) for a head-of-household filer in a tax bracket
     */
    @Override
    public int getMaxIncomeHOH() {
        return maxIncomeHOH;
    }

    /**
     * Sets the max income (USD) for a head-of-household filer for a tax bracket with the given data
     * @param maxIncomeHOH Max income (USD) for a head-of-household filer to set
     */
    public void setMaxIncomeHOH(int maxIncomeHOH) {
        this.maxIncomeHOH = maxIncomeHOH;
    }

    /**
     * Returns a flag indicating if this BracketDetails instance is considered equal to the other one
     * @param o Other BracketDetails instance
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BracketDetails that = (BracketDetails) o;
        return taxYear == that.taxYear && taxRate == that.taxRate && minIncomeSingle == that.minIncomeSingle && maxIncomeSingle == that.maxIncomeSingle && minIncomeMFJ == that.minIncomeMFJ && maxIncomeMFJ == that.maxIncomeMFJ && minIncomeMFS == that.minIncomeMFS && maxIncomeMFS == that.maxIncomeMFS && minIncomeHOH == that.minIncomeHOH && maxIncomeHOH == that.maxIncomeHOH;
    }

    /**
     * Computes the hash code for this bracket details instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(taxYear, taxRate, minIncomeSingle, maxIncomeSingle, minIncomeMFJ, maxIncomeMFJ, minIncomeMFS, maxIncomeMFS, minIncomeHOH, maxIncomeHOH);
    }
}
