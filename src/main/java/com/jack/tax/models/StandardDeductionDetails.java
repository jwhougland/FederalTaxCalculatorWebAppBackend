package com.jack.tax.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Encapsulates the standard deduction details for a particular tax year
 * for the various filing options.
 */
@Document(collection = "StandardDeductions")
public class StandardDeductionDetails {

    /**
     * Applicable tax year for the standard deduction details
     */
    private int taxYear;

    /**
     * Standard deduction (USD) for single filers
     */
    private int single;

    /**
     * Standard deduction (USD) for married filing jointly (or surviving spouse) filers
     */
    private int mfj;

    /**
     * Standard deduction (USD) for married filing separately filers
     */
    private int mfs;

    /**
     * Standard deduction (USD) for head of household filers
     */
    private int hoh;

    /**
     * Creates a fully initialized standard deduction details model
     */
    public StandardDeductionDetails() {
        // No processing required
    }

    /**
     * Returns the tax year applicable to the standard deduction
     */
    public int getTaxYear() {
        return taxYear;
    }

    /**
     * Returns the standard deduction (USD) for a single filer
     */
    public int getSingle() {
        return single;
    }

    /**
     * Returns the standard deduction (USD) for married-filing-jointly filers
     */
    public int getMfj() { return mfj; }

    /**
     * Returns the standard deduction (USD) for a married-filing-separately filer
     */
    public int getMfs() {
        return mfs;
    }

    /**
     * Returns the standard deduction (USD) for a head-of-household filer
     */
    public int getHoh() {
        return hoh;
    }

    /**
     * Returns the string representation of a standard deduction details instance.
     */
    @Override
    public String toString() {
        return "StandardDeductionDetails{" +
                "taxYear=" + taxYear +
                ", single=" + single +
                ", mfj=" + mfj +
                ", mfs=" + mfs +
                ", hoh=" + hoh +
                '}';
    }

    /**
     * Determines if this standard deduction details instance is considered equal to the other one.
     * @param o Other standard deduction details instance
     * @return True if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StandardDeductionDetails that = (StandardDeductionDetails) o;
        return taxYear == that.taxYear && single == that.single && mfj == that.mfj && mfs == that.mfs && hoh == that.hoh;
    }

    /**
     * Uses this standard deduction details instance's fields to compute and return a hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(taxYear, single, mfj, mfs, hoh);
    }
}
