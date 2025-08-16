package com.jack.tax.models.interfaces;

import com.jack.tax.models.FilingStatus;

/**
 * Defines the behavior that a concrete input model must contain.
 */
public interface InputModel {

    /**
     * Returns the gross income (USD) before any taxes / withholdings.
     */
    public double getGrossIncome();

    /**
     * Sets the gross income (USD) before any taxes / withholdings.
     * @param grossIncome User-entered gross income (USD)
     */
    public void setGrossIncome(double grossIncome);

    /**
     * Returns the selected filing status
     */
    public FilingStatus getSelectedFilingStatus();

    /**
     * Sets the filing status
     * @param filingStatus User-entered filing status
     */
    public void setSelectedFilingStatus(FilingStatus filingStatus);

    /**
     * Returns the selected tax year
     */
    public int getSelectedTaxYear();

    /**
     * Sets the tax year
     * @param taxYear User-entered tax year
     */
    public void setSelectedTaxYear(int taxYear);

    /**
     * Returns the total deductions (USD) the user has entered.
     */
    public double getTotalDeductions();

    /**
     * Sets the total deductions (USD)
     * @param totalDeductions User-entered total deductions (USD)
     */
    public void setTotalDeductions(double totalDeductions);

    /**
     * Returns the total credits (USD) the user has entered.
     */
    public double getTotalCredits();

    /**
     * Sets the total credits (USD)
     * @param totalCredits User-entered total credits (USD)
     */
    public void setTotalCredits(double totalCredits);
}
