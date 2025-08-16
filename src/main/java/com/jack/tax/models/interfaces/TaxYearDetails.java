package com.jack.tax.models.interfaces;

import java.util.List;

/**
 * Contains the behaviors that a concrete Tax Year Details class must contain.
 */
public interface TaxYearDetails {

    /**
     * Returns the applicable tax year.
     */
    public int getTaxYear();

    /**
     * Sets the tax year value.
     * @param taxYear Applicable tax year.
     */
    public void setTaxYear(int taxYear);

    /**
     * Returns the standard deduction details for an applicable tax year.
     */
    public StandardDeductionDetails getStandardDeductionDetails();

    /**
     * Sets the standard deduction details for an applicable tax year.
     * @param standardDeductionDetails Standard deduction details.
     */
    public void setStandardDeductionDetails(StandardDeductionDetails standardDeductionDetails);

    /**
     * Returns the tax bracket details for an applicable tax year.
     */
    public List<BracketDetails> getBracketDetails();

    /**
     * Sets the tax bracket details for an applicable tax year.
     * @param bracketDetails Tax bracket details.
     */
    public void setBracketDetails(List<BracketDetails> bracketDetails);
}
