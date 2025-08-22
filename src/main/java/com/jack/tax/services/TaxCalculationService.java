package com.jack.tax.services;

import com.jack.tax.models.BracketDetails;
import com.jack.tax.models.FilingStatus;
import com.jack.tax.models.StandardDeductionDetails;
import com.jack.tax.models.interfaces.InputModel;
import com.jack.tax.models.interfaces.OutputModel;
import com.jack.tax.models.interfaces.TaxYearDetails;
import com.jack.tax.repositories.BracketRepository;
import com.jack.tax.repositories.StandardDeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains the business logic for calculating federal taxes owed based on user-entered data.
 */
@Service
public class TaxCalculationService {

    /**
     * Used to perform CRUD operations on standard deduction entities
     */
    private final StandardDeductionRepository standardDeductionRepository;

    /**
     * Used to perform CRUD operations on bracket entities
     */
    private final BracketRepository bracketRepository;

    /**
     * Creates a fully initialized Tax Calculation Service using the given data.
     *
     * @param standardDeductionRepository Used to query standard deduction information
     * @param bracketRepository Used to query tax bracket information
     */
    @Autowired
    public TaxCalculationService(StandardDeductionRepository standardDeductionRepository, BracketRepository bracketRepository) {

        this.standardDeductionRepository = standardDeductionRepository;
        this.bracketRepository = bracketRepository;
    }

    /**
     * Queries persistent storage for tax years that are supported and returns those years in a list.
     */
    public List<Integer> getSupportedTaxYears() {

        List<Integer> taxYears = new ArrayList<>();

        // Query persistent storage for the standard deduction entities because there will be one for each tax year
        // supported by this app
        List<StandardDeductionDetails> standardDeductionDetails = standardDeductionRepository.findAll();

        // Iterate over the standard deduction entities and use its tax year field to build up the collection
        // returned by this method
        standardDeductionDetails.forEach(sd -> taxYears.add(sd.getTaxYear()));

        return taxYears;
    }

    /**
     * Uses the user-entered tax inputs to calculate and return federal tax owed, marginal tax rate,
     * effective tax rate, and take home pay in an output model.
     *
     * @param inputModel User-entered tax inputs.
     */
    public OutputModel calculateTaxes(InputModel inputModel) {

        // Query persistent storage to get tax year details for the selected tax year
        TaxYearDetails taxYearDetails = getTaxYearDetails(inputModel.getSelectedTaxYear());

        // Get the taxable income
        double taxableIncome = getTaxableIncome(inputModel, taxYearDetails.getStandardDeductionDetails());

        // Get the zero-based index for the max tax bracket applicable to the user's situation
        int maxTaxBracketIndex = getMaxTaxBracketIndex(
                inputModel.getSelectedFilingStatus(),
                taxYearDetails.getBracketDetails(),
                taxableIncome);

        // Initialize a value that will hold the amount of federal tax owed (USD)
        double federalTaxOwed = 0.0;

        // Iterate over the tax brackets for the applicable tax year
        for (int bracketIndex = 0; bracketIndex <= maxTaxBracketIndex; bracketIndex++) {

            // Get the bracket details collection element pertinent to the current bracket index
            com.jack.tax.models.interfaces.BracketDetails bracketDetailsForIndex = taxYearDetails
                    .getBracketDetails().get(bracketIndex);


            // Get the applicable income (USD) for the current bracket
            double applicableIncomeForBracket = getApplicableIncomeForBracket(
                    inputModel.getSelectedFilingStatus(),
                    bracketDetailsForIndex,
                    taxableIncome);

            // Compute the amount of tax that this bracket drives
            int marginalTaxRateForBracket = bracketDetailsForIndex.getTaxRate();
            double taxForCurrentBracket = (applicableIncomeForBracket * (marginalTaxRateForBracket / 100.0));

            // Increment the federal tax owed based on the applicable amount of
            // tax that this current bracket drives
            federalTaxOwed += taxForCurrentBracket;
        }

        // Lastly, apply the tax credits to offset the federal tax owed amount
        federalTaxOwed -= inputModel.getTotalCredits();

        // Initialize and populate an output tax model
        OutputModel outputModel = new com.jack.tax.models.OutputModel();
        outputModel.setTaxYear(inputModel.getSelectedTaxYear());
        outputModel.setFederalTaxOwed(federalTaxOwed);
        outputModel.setMarginalTaxRate(taxYearDetails.getBracketDetails().get(maxTaxBracketIndex).getTaxRate());
        outputModel.setEffectiveTaxRate((federalTaxOwed / inputModel.getGrossIncome()) * 100.0);
        outputModel.setTakeHomePay(inputModel.getGrossIncome() - federalTaxOwed);
        return outputModel;
    }

    /**
     * Queries persistent storage for tax year details for the given tax year.
     *
     * @param taxYear Tax year for which we want tax details.
     */
    protected TaxYearDetails getTaxYearDetails(int taxYear) {

        // Query persistent storage for all available standard deduction details and bracket details regardless of year
        List<StandardDeductionDetails> allStandardDeductionDetails = standardDeductionRepository.findAll();
        List<BracketDetails> allBracketDetails = bracketRepository.findAll();

        // Do an in-memory query for the standard deduction details that are applicable to the given tax year
        StandardDeductionDetails standardDeductionDetailsForTaxYear = allStandardDeductionDetails
                .stream()
                .filter(details -> details.getTaxYear() == taxYear)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find standard deduction details for tax year " + taxYear));

        // Do an in-memory query for the bracket details that are applicable to the given tax year
        List<com.jack.tax.models.interfaces.BracketDetails> bracketDetailsForTaxYear = allBracketDetails
                .stream()
                .filter(details -> details.getTaxYear() == taxYear)
                .sorted(Comparator.comparingInt(BracketDetails::getTaxRate))
                .map(details -> (com.jack.tax.models.interfaces.BracketDetails)details)
                .toList();

        // Initialize and populate the tax year details model we will return
        TaxYearDetails taxYearDetails = new com.jack.tax.models.TaxYearDetails();
        taxYearDetails.setTaxYear(taxYear);
        taxYearDetails.setStandardDeductionDetails((com.jack.tax.models.interfaces.StandardDeductionDetails)standardDeductionDetailsForTaxYear);
        taxYearDetails.setBracketDetails(bracketDetailsForTaxYear);

        return taxYearDetails;
    }

    /**
     * Uses the given data to compute taxable income (USD).
     * @param inputModel User-entered tax inputs
     * @param standardDeductionDetails Standard deduction details for a given tax year
     */
    protected double getTaxableIncome(InputModel inputModel, com.jack.tax.models.interfaces.StandardDeductionDetails standardDeductionDetails) {

        double taxableIncome = 0.0;
        double taxableIncomeWithItemizedDeductions = inputModel.getGrossIncome() - inputModel.getTotalDeductions();
        double standardDeductionForFilingStatus = getStandardDeductionForFilingStatus(
                inputModel.getSelectedFilingStatus(), standardDeductionDetails);

        // If the gross income exceeds the standard deduction then there will be applicable taxes
        // so these computations are necessary.  Should this evaluate to false then taxable income was
        // already initialized to zero so that is what would be returned in that case.
        if (inputModel.getGrossIncome() > standardDeductionForFilingStatus) {

            // Compute what the taxable income would be with the standard deduction
            double taxableIncomeWithStandardDeduction = inputModel.getGrossIncome() - standardDeductionForFilingStatus;

            // Take the more advantageous taxable income (itemization vs standard deduction)
            taxableIncome = Math.min(taxableIncomeWithItemizedDeductions, taxableIncomeWithStandardDeduction);
        }

        return taxableIncome;
    }

    /**
     * Gets the numerical zero-based index for the max tax bracket applicable to the user based
     * on their taxable income and filing status.
     *
     * @param filingStatus   Filing status enum
     * @param bracketDetails Tax bracket details for a particular tax year
     * @param taxableIncome  Taxable income (USD)
     */
    protected int getMaxTaxBracketIndex(FilingStatus filingStatus,
                                     List<com.jack.tax.models.interfaces.BracketDetails> bracketDetails,
                                     double taxableIncome) {

        // Iterate over the brackets for the given tax year
        for (int bracketIndex = 0; bracketIndex < bracketDetails.size(); bracketIndex++) {

            // If the user's taxable income maxes out in the current tax bracket
            double maxIncomeForBracket = getMaxIncomeForBracket(filingStatus, bracketDetails.get(bracketIndex));
            if (taxableIncome <= maxIncomeForBracket) {
                // We've found the index for the max tax bracket applicable to the user
                return bracketIndex;
            }
        }

        // It is unlikely we would get here, but if we do
        // as a default return the index for the last tax bracket
        return bracketDetails.size() - 1;
    }

    /**
     * Determines and returns how much the user's taxable income (USD) takes up in the current bracket.
     * @param filingStatus   Filing status enum
     * @param bracketDetails Tax bracket details for a given tax year
     * @param taxableIncome  User's taxable income (USD)
     */
    public double getApplicableIncomeForBracket(FilingStatus filingStatus,
                                                com.jack.tax.models.interfaces.BracketDetails bracketDetails,
                                                double taxableIncome) {

        // Start by getting the min and max incomes for the current bracket
        // that pertain to the user's filing status
        double minIncomeForBracket = getMinIncomeForBracket(filingStatus, bracketDetails);
        double maxIncomeForBracket = getMaxIncomeForBracket(filingStatus, bracketDetails);

        // Case 1 - If the user's taxable income is greater than or equal to the
        // max income for the bracket relative to their filing status then
        // just return the difference between the max and min income for the bracket
        if (taxableIncome >= maxIncomeForBracket) {
            return maxIncomeForBracket - minIncomeForBracket;
        }

        // Case 2 - If the user's taxable income is less than the max income for the bracket
        // but greater than or equal to the min income for the bracket then return the difference
        // between the user's taxable income and the min income for the bracket
        if (taxableIncome >= minIncomeForBracket) {
            return taxableIncome - minIncomeForBracket;
        }

        // We should not get here because it is the caller's responsibility to
        // call this protected method in a scenario where the user's taxable income
        // is greater than or equal to the min income for this bracket
        throw new RuntimeException("Unable to compute the applicable income for a bracket");

    }

    /**
     * Uses the given filing status and standard deduction details for a particular tax year to determine and
     * return the standard deduction amount (USD) that would apply.
     *
     * @param filingStatus Filing status enum
     * @param standardDeductionDetails Standard deduction details for all filing statuses for a given year
     */
    protected double getStandardDeductionForFilingStatus(FilingStatus filingStatus,
                                                         com.jack.tax.models.interfaces.StandardDeductionDetails standardDeductionDetails) {

        // Use the filing status to determine which standard deduction amount would apply
        switch(filingStatus) {

            case FilingStatus.SINGLE -> {
                return standardDeductionDetails.getSingle();
            }
            case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE -> {
                return standardDeductionDetails.getMfj();
            }
            case FilingStatus.MARRIED_FILING_SEPARATELY -> {
                return standardDeductionDetails.getMfs();
            }
            case FilingStatus.HEAD_OF_HOUSEHOLD -> {
                return standardDeductionDetails.getHoh();
            }
            default -> throw new RuntimeException("Unrecognized filing status found when looking up standard deduction amount");
        }
    }

    /**
     * Uses the given filing status and details for a particular tax bracket to look up,
     * and return the min applicable income (USD) for that bracket.
     * @param filingStatus Filing status enum
     * @param bracketDetails Details for a particular tax bracket for a particular tax year
     */
    protected double getMinIncomeForBracket(FilingStatus filingStatus,
                                            com.jack.tax.models.interfaces.BracketDetails bracketDetails) {

        // Use the filing status to determine which min income amount would apply for a bracket
        switch(filingStatus) {

            case FilingStatus.SINGLE -> {
                return bracketDetails.getMinIncomeSingle();
            }
            case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE -> {
                return bracketDetails.getMinIncomeMFJ();
            }
            case FilingStatus.MARRIED_FILING_SEPARATELY -> {
                return bracketDetails.getMinIncomeMFS();
            }
            case FilingStatus.HEAD_OF_HOUSEHOLD -> {
                return bracketDetails.getMinIncomeHOH();
            }
            default -> throw new RuntimeException("Unrecognized filing status found when looking up min bracket income");
        }
    }

    /**
     * Uses the given filing status and details for a particular tax bracket to look up,
     * and return the max applicable income (USD) for that bracket.
     * @param filingStatus Filing status enum
     * @param bracketDetails Details for a particular tax bracket for a particular tax year
     */
    protected double getMaxIncomeForBracket(FilingStatus filingStatus,
                                            com.jack.tax.models.interfaces.BracketDetails bracketDetails) {

        // Use the filing status to determine which max income amount would apply for a bracket
        switch(filingStatus) {

            case FilingStatus.SINGLE -> {
                return bracketDetails.getMaxIncomeSingle();
            }
            case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE -> {
                return bracketDetails.getMaxIncomeMFJ();
            }
            case FilingStatus.MARRIED_FILING_SEPARATELY -> {
                return bracketDetails.getMaxIncomeMFS();
            }
            case FilingStatus.HEAD_OF_HOUSEHOLD -> {
                return bracketDetails.getMaxIncomeHOH();
            }
            default -> throw new RuntimeException("Unrecognized filing status found when looking up max bracket income");
        }
    }
}
