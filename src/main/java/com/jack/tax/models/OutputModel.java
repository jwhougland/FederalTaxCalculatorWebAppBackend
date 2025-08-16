package com.jack.tax.models;

/**
 * Defines the data and behavior of the tax calculation response.
 */
public class OutputModel implements com.jack.tax.models.interfaces.OutputModel {

    private int taxYear;
    private double federalTaxOwed;
    private int marginalTaxRate;
    private double effectiveTaxRate;
    private double takeHomePay;

    /**
     * Creates a fully initialized output model.
     */
    public OutputModel() {
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
     * Sets the tax year.
     *
     * @param taxYear Applicable tax year.
     */
    @Override
    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    /**
     * Returns the amount of federal tax owed after deductions/credits (USD).
     */
    @Override
    public double getFederalTaxOwed() {
        return federalTaxOwed;
    }

    /**
     * Sets the amount of federal tax owed after deductions/credits (USD).
     *
     * @param federalTaxOwed Federal tax owed (USD).
     */
    @Override
    public void setFederalTaxOwed(double federalTaxOwed) {
        this.federalTaxOwed = federalTaxOwed;
    }

    /**
     * Returns the marginal tax rate (%).  In other words, the tax rate tied to the last dollar of income.
     */
    @Override
    public int getMarginalTaxRate() {
        return marginalTaxRate;
    }

    /**
     * Sets the marginal tax rate (%).
     *
     * @param marginalTaxRate The tax rate (%) tied to the last dollar of income.
     */
    @Override
    public void setMarginalTaxRate(int marginalTaxRate) {
        this.marginalTaxRate = marginalTaxRate;
    }

    /**
     * Returns the effective tax rate (%).  In other words, the
     * (federal income tax owed divided by gross income) * 100.0.
     */
    @Override
    public double getEffectiveTaxRate() {
        return effectiveTaxRate;
    }

    /**
     * Sets the effective tax rate (%).
     *
     * @param effectiveTaxRate Effective tax rate (%).
     */
    @Override
    public void setEffectiveTaxRate(double effectiveTaxRate) {
        this.effectiveTaxRate = effectiveTaxRate;
    }

    /**
     * Returns the take home pay amount (USD).  Computed as gross income minus
     * federal tax owed.
     */
    @Override
    public double getTakeHomePay() {
        return takeHomePay;
    }

    /**
     * Sets the take home pay amount (USD).
     *
     * @param takeHomePay Take home pay (USD).
     */
    @Override
    public void setTakeHomePay(double takeHomePay) {
        this.takeHomePay = takeHomePay;
    }
}
