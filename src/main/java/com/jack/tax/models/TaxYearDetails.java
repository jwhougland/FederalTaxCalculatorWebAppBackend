package com.jack.tax.models;

import com.jack.tax.models.interfaces.BracketDetails;
import com.jack.tax.models.interfaces.StandardDeductionDetails;

import java.util.List;

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
}
