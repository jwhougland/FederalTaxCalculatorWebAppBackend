package com.jack.tax.models;

import com.jack.tax.models.interfaces.BracketDetails;
import com.jack.tax.models.interfaces.StandardDeductionDetails;

import java.util.List;
import java.util.Objects;

/**
 * Contains the data and behaviors for the details of an applicable tax year.
 */
public class TaxYearDetails implements com.jack.tax.models.interfaces.TaxYearDetails {

    private int taxYear;
    private StandardDeductionDetails standardDeductionDetails;
    private List<BracketDetails> bracketDetails;

    /**
     * Creates a fully initialized tax year details instance.
     */
    public TaxYearDetails() {
        // No processing required
    }

    /**
     * Returns the applicable tax year.
     */
    @Override
    public int getTaxYear() {
        return taxYear;
    }

    /**
     * Sets the tax year value.
     *
     * @param taxYear Applicable tax year.
     */
    @Override
    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    /**
     * Returns the standard deduction details for an applicable tax year.
     */
    @Override
    public StandardDeductionDetails getStandardDeductionDetails() {
        return standardDeductionDetails;
    }

    /**
     * Sets the standard deduction details for an applicable tax year.
     *
     * @param standardDeductionDetails Standard deduction details.
     */
    @Override
    public void setStandardDeductionDetails(StandardDeductionDetails standardDeductionDetails) {
        this.standardDeductionDetails = standardDeductionDetails;
    }

    /**
     * Returns the tax bracket details for an applicable tax year.
     */
    @Override
    public List<BracketDetails> getBracketDetails() {
        return bracketDetails;
    }

    /**
     * Sets the tax bracket details for an applicable tax year.
     *
     * @param bracketDetails Tax bracket details.
     */
    @Override
    public void setBracketDetails(List<BracketDetails> bracketDetails) {
        this.bracketDetails = bracketDetails;
    }

    /**
     * Determines if this TaxYearDetails instance is regarded as equal to the other instance
     * @param o Other TaxYearDetails instance
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaxYearDetails that = (TaxYearDetails) o;
        return taxYear == that.taxYear && Objects.equals(standardDeductionDetails, that.standardDeductionDetails) && Objects.equals(bracketDetails, that.bracketDetails);
    }

    /**
     * Computes a hash code for this TaxYearDetails instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(taxYear, standardDeductionDetails, bracketDetails);
    }
}
