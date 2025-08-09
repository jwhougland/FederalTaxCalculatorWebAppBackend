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

    public int getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }

    public int getMfj() {
        return mfj;
    }

    public void setMfj(int mfj) {
        this.mfj = mfj;
    }

    public int getMfs() {
        return mfs;
    }

    public void setMfs(int mfs) {
        this.mfs = mfs;
    }

    public int getHoh() {
        return hoh;
    }

    public void setHoh(int hoh) {
        this.hoh = hoh;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StandardDeductionDetails that = (StandardDeductionDetails) o;
        return taxYear == that.taxYear && single == that.single && mfj == that.mfj && mfs == that.mfs && hoh == that.hoh;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxYear, single, mfj, mfs, hoh);
    }
}
