package com.jack.tax.models;

/**
 * Contains the user-entered inputs needed to get tax calculations started.
 */
public class InputModel implements com.jack.tax.models.interfaces.InputModel {

    private double grossIncome;
    private FilingStatus selectedFilingStatus;
    private int selectedTaxYear;
    private double totalDeductions;
    private double totalCredits;

    /**
     * Creates a fully initialized input model
     */
    public InputModel() {
        // No processing required
    }

    /**
     * Returns the gross income (USD) before any taxes / withholdings.
     */
    @Override
    public double getGrossIncome() {
        return grossIncome;
    }

    /**
     * Sets the gross income (USD) before any taxes / withholdings.
     *
     * @param grossIncome User-entered gross income (USD)
     */
    @Override
    public void setGrossIncome(double grossIncome) {
        this.grossIncome = grossIncome;
    }

    /**
     * Returns the selected filing status
     */
    @Override
    public FilingStatus getSelectedFilingStatus() {
        return selectedFilingStatus;
    }

    /**
     * Sets the filing status
     *
     * @param selectedFilingStatus User-entered filing status
     */
    @Override
    public void setSelectedFilingStatus(FilingStatus selectedFilingStatus) {
        this.selectedFilingStatus = selectedFilingStatus;
    }

    /**
     * Returns the selected tax year
     */
    @Override
    public int getSelectedTaxYear() {
        return selectedTaxYear;
    }

    /**
     * Sets the tax year
     *
     * @param selectedTaxYear User-entered tax year
     */
    @Override
    public void setSelectedTaxYear(int selectedTaxYear) {
        this.selectedTaxYear = selectedTaxYear;
    }

    /**
     * Returns the total deductions (USD) the user has entered.
     */
    @Override
    public double getTotalDeductions() {
        return totalDeductions;
    }

    /**
     * Sets the total deductions (USD)
     *
     * @param totalDeductions User-entered total deductions (USD)
     */
    @Override
    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    /**
     * Returns the total credits (USD) the user has entered.
     */
    @Override
    public double getTotalCredits() {
        return totalCredits;
    }

    /**
     * Sets the total credits (USD)
     *
     * @param totalCredits User-entered total credits (USD)
     */
    @Override
    public void setTotalCredits(double totalCredits) {
        this.totalCredits = totalCredits;
    }
}
