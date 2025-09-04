package com.jack.tax.models;

import com.jack.tax.utils.AppConstants;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * Contains the user-entered inputs needed to get tax calculations started.
 */
@NoArgsConstructor
public class InputModel implements com.jack.tax.models.interfaces.InputModel {

    @DecimalMin(value = AppConstants.MIN_USD_AMOUNT_AS_STR, message = "Gross income must be at least " + AppConstants.MIN_USD_AMOUNT_AS_STR)
    @DecimalMax(value = AppConstants.MAX_USD_AMOUNT_AS_STR, message = "Gross income must be less than or equal to " + AppConstants.MAX_USD_AMOUNT_AS_STR)
    @Digits(integer = AppConstants.MAX_DIGITS_BEFORE_DECIMAL_POINT, fraction = AppConstants.MAX_PRECISION, message = "Must be a valid monetary amount")
    private double grossIncome;

    @NotNull(message = "Filing status must have a value")
    private FilingStatus selectedFilingStatus;

    private int selectedTaxYear;

    @DecimalMin(value = AppConstants.MIN_USD_AMOUNT_AS_STR, message = "Deductions must be at least " + AppConstants.MIN_USD_AMOUNT_AS_STR)
    @DecimalMax(value = AppConstants.MAX_USD_AMOUNT_AS_STR, message = "Deductions must be less than or equal to " + AppConstants.MAX_USD_AMOUNT_AS_STR)
    @Digits(integer = AppConstants.MAX_DIGITS_BEFORE_DECIMAL_POINT, fraction = AppConstants.MAX_PRECISION, message = "Must be a valid monetary amount")
    private double totalDeductions;

    @DecimalMin(value = AppConstants.MIN_USD_AMOUNT_AS_STR, message = "Credits must be at least " + AppConstants.MIN_USD_AMOUNT_AS_STR)
    @DecimalMax(value = AppConstants.MAX_USD_AMOUNT_AS_STR, message = "Credits must be less than or equal to " + AppConstants.MAX_USD_AMOUNT_AS_STR)
    @Digits(integer = AppConstants.MAX_DIGITS_BEFORE_DECIMAL_POINT, fraction = AppConstants.MAX_PRECISION, message = "Must be a valid monetary amount")
    private double totalCredits;

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
