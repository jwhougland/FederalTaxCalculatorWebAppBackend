package com.jack.tax.models.interfaces;

/**
 * Defines the behavior that a concrete output model must contain.
 */
public interface OutputModel {

    /**
     * Returns the applicable tax year.
     */
    public int getTaxYear();

    /**
     * Sets the tax year.
     * @param taxYear Applicable tax year.
     */
    public void setTaxYear(int taxYear);

    /**
     * Returns the amount of federal tax owed after deductions/credits (USD).
     */
    public double getFederalTaxOwed();

    /**
     * Sets the amount of federal tax owed after deductions/credits (USD).
     * @param federalTaxOwed Federal tax owed (USD).
     */
    public void setFederalTaxOwed(double federalTaxOwed);

    /**
     * Returns the marginal tax rate (%).  In other words, the tax rate tied to the last dollar of income.
     */
    public int getMarginalTaxRate();

    /**
     * Sets the marginal tax rate (%).
     * @param marginalTaxRate The tax rate (%) tied to the last dollar of income.
     */
    public void setMarginalTaxRate(int marginalTaxRate);

    /**
     * Returns the effective tax rate (%).  In other words, the
     * (federal income tax owed divided by gross income) * 100.0.
     */
    public double getEffectiveTaxRate();

    /**
     * Sets the effective tax rate (%).
     * @param effectiveTaxRate Effective tax rate (%).
     */
    public void setEffectiveTaxRate(double effectiveTaxRate);

    /**
     * Returns the take home pay amount (USD).  Computed as gross income minus
     * federal tax owed.
     */
    public double getTakeHomePay();

    /**
     * Sets the take home pay amount (USD).
     * @param takeHomePay Take home pay (USD).
     */
    public void setTakeHomePay(double takeHomePay);
}
