package com.jack.tax.services;

import com.jack.tax.models.BracketDetails;
import com.jack.tax.models.FilingStatus;
import com.jack.tax.models.StandardDeductionDetails;
import com.jack.tax.models.TaxYearDetails;
import com.jack.tax.models.interfaces.InputModel;
import com.jack.tax.repositories.BracketRepository;
import com.jack.tax.repositories.StandardDeductionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the Tax Calculation Service
 */
@ExtendWith(MockitoExtension.class)
public class TaxCalculationServiceTests {

    /**
     * Mocks the interaction with the Standard Deduction Repository
     */
    @Mock
    private StandardDeductionRepository mockitoStandardDeductionRepository;

    /**
     * Mocks the interaction with the Bracket Repository
     */
    @Mock
    private BracketRepository mockitoBracketRepository;

    /**
     * Verifies expected tax years are returned.
     *
     * @param expectedTaxYears Tax years that are expected to be returned by the method under test
     */
    @ParameterizedTest
    @MethodSource("expectedYearsProvider")
    public void getSupportedTaxYears_shouldReturnExpectedYears(List<Integer> expectedTaxYears) {

        // Mock the behavior of the standard deductions repository
        when(mockitoStandardDeductionRepository.findAll())
                .thenReturn(createMockedStandardDeductionDetails());

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        // Call the method under test
        List<Integer> actualTaxYears = taxCalculationService.getSupportedTaxYears();

        // Verify the actual tax years collection contains expected content
        assertIterableEquals(expectedTaxYears, actualTaxYears);
    }

    /**
     * Verifies expected tax year details are returned.
     *
     * @param taxYear The tax year for which we are going to check if expected details are returned
     */
    @ParameterizedTest
    @ValueSource(ints = {2024, 2025})
    public void getTaxYearDetails_shouldReturnExpectedDetails(int taxYear) {

        // Mock the behavior of the standard deductions repository
        when(mockitoStandardDeductionRepository.findAll())
                .thenReturn(createMockedStandardDeductionDetails());

        // Mock the behavior of the bracket details repository
        when(mockitoBracketRepository.findAll())
                .thenReturn(createMockedBracketDetails());

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        // Call the method under test to get the actual tax year details
        TaxYearDetails actualTaxYearDetails = (TaxYearDetails)taxCalculationService.getTaxYearDetails(taxYear);

        // Verify the actual tax year details model contains the expected content
        TaxYearDetails expectedTaxYearDetails = expectedTaxYearDetailsProvider(taxYear);
        assertEquals(expectedTaxYearDetails.getTaxYear(), actualTaxYearDetails.getTaxYear());
        assertEquals(expectedTaxYearDetails.getStandardDeductionDetails(), actualTaxYearDetails.getStandardDeductionDetails());
        assertIterableEquals(expectedTaxYearDetails.getBracketDetails(), actualTaxYearDetails.getBracketDetails());
    }

    /**
     * Verifies that the expected standard deduction amount (USD) is returned based on the given data
     *
     * @param taxYear       Tax year
     * @param filingStatus  Filing status
     */
    @ParameterizedTest
    @CsvSource({
        "2024, SINGLE",
        "2024, MARRIED_FILING_JOINTLY",
        "2024, SURVIVING_SPOUSE",
        "2024, MARRIED_FILING_SEPARATELY",
        "2024, HEAD_OF_HOUSEHOLD",
        "2025, SINGLE",
        "2025, MARRIED_FILING_JOINTLY",
        "2025, SURVIVING_SPOUSE",
        "2025, MARRIED_FILING_SEPARATELY",
        "2025, HEAD_OF_HOUSEHOLD"
    })
    public void getStandardDeductionForFilingStatus_returnsExpectedStandardDeduction(int taxYear, FilingStatus filingStatus) {

        // Get standard deduction details that will be provided as an input to the method under test
        StandardDeductionDetails standardDeductionDetails = createMockedStandardDeductionDetails()
                .stream()
                .filter(sd -> sd.getTaxYear() == taxYear)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find expected standard deduction details" +
                        " for tax year " + taxYear));

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        // Call the method under test
        double actualStandardDeduction = taxCalculationService.getStandardDeductionForFilingStatus(
                filingStatus, standardDeductionDetails);

        // Take action based on the filing status when doing the verification on the standard deduction amount
        // returned by the method under test
        switch(filingStatus) {
            case FilingStatus.SINGLE ->
                    assertEquals(standardDeductionDetails.getSingle(), actualStandardDeduction);
            case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE ->
                    assertEquals(standardDeductionDetails.getMfj(), actualStandardDeduction);
            case FilingStatus.MARRIED_FILING_SEPARATELY ->
                    assertEquals(standardDeductionDetails.getMfs(), actualStandardDeduction);
            case FilingStatus.HEAD_OF_HOUSEHOLD ->
                    assertEquals(standardDeductionDetails.getHoh(), actualStandardDeduction);
        }
    }

    /**
     * Verifies the method under test returns the expected taxable income (USD) using the given inputs
     *
     * @param taxYear                 Tax year
     * @param grossIncome             Gross income (USD)
     * @param totalDeductions         Total deductions (USD)
     * @param filingStatus            Filing status enum
     * @param expectedTaxableIncome   Expected taxable income (USD)
     */
    @ParameterizedTest
    @CsvSource({
            // taxYear, grossIncome, totalDeductions, filingStatus, expectedTaxableIncome
            "2024, 50000, 10000, SINGLE, 35400",  // Standard deduction is better
            "2024, 50000, 20000, SINGLE, 30000",  // Itemizing is better
            "2024, 10000, 5000, SINGLE, 0",       // Gross income less than standard deduction
            "2024, 10000, 15000, SINGLE, 0",      // Gross income minus deductions less than 0
            "2025, 60000, 20000, MARRIED_FILING_JOINTLY, 28500",  // Standard deduction is better
            "2025, 50000, 40000, SINGLE, 10000"                   // Itemizing is better
    })
    public void getTaxableIncome_shouldReturnCorrectAmount(
            int taxYear,
            double grossIncome,
            double totalDeductions,
            FilingStatus filingStatus,
            double expectedTaxableIncome) {

        // Create an input model that will be provided as an input to the method under test
        InputModel input = new com.jack.tax.models.InputModel();
        input.setGrossIncome(grossIncome);
        input.setTotalDeductions(totalDeductions);
        input.setSelectedFilingStatus(filingStatus);

        // Get standard deduction details that will be provided as an input to the method under test
        StandardDeductionDetails standardDeductionDetails = createMockedStandardDeductionDetails()
                .stream()
                .filter(sd -> sd.getTaxYear() == taxYear)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find expected standard deduction details" +
                        " for tax year " + taxYear));

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        // Call the method under test
        double actualTaxableIncome = taxCalculationService.getTaxableIncome(input, standardDeductionDetails);

        // Verify the actual taxable income equals the expected amount
        assertEquals(expectedTaxableIncome, actualTaxableIncome, 0.01);
    }

    /**
     * Verifies that the expected max income is returned for each tax bracket for a given year and filing status
     *
     * @param taxYear       Tax year
     * @param filingStatus  Filing status
     */
    @ParameterizedTest
    @CsvSource({
            "2024, SINGLE",
            "2024, MARRIED_FILING_JOINTLY",
            "2024, SURVIVING_SPOUSE",
            "2024, MARRIED_FILING_SEPARATELY",
            "2024, HEAD_OF_HOUSEHOLD",
            "2025, SINGLE",
            "2025, MARRIED_FILING_JOINTLY",
            "2025, SURVIVING_SPOUSE",
            "2025, MARRIED_FILING_SEPARATELY",
            "2025, HEAD_OF_HOUSEHOLD"
    })
    public void getMaxIncomeForBracket_returnsExpectedMaxIncome(int taxYear, FilingStatus filingStatus) {

        // Search for the tax brackets applicable to the given tax year, and
        // sort in ascending order based on the marginal tax rate
        List<? extends com.jack.tax.models.interfaces.BracketDetails> allBracketDetails = createMockedBracketDetails()
                .stream()
                .filter(bd -> bd.getTaxYear() == taxYear)
                .sorted(Comparator.comparingInt(BracketDetails::getTaxRate))
                .toList();

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        // Iterate through the bracket details applicable to the given tax year
        for (com.jack.tax.models.interfaces.BracketDetails individualBracketDetails : allBracketDetails) {

            // Call the method under test to get the actual max income for a bracket
            double actualMaxIncomeForBracket = taxCalculationService
                    .getMaxIncomeForBracket(filingStatus, individualBracketDetails);

            // Take action with the max income for bracket verification based on the filing status
            switch(filingStatus) {
                case FilingStatus.SINGLE ->
                    assertEquals(individualBracketDetails.getMaxIncomeSingle(), actualMaxIncomeForBracket);
                case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE ->
                    assertEquals(individualBracketDetails.getMaxIncomeMFJ(), actualMaxIncomeForBracket);
                case FilingStatus.MARRIED_FILING_SEPARATELY ->
                    assertEquals(individualBracketDetails.getMaxIncomeMFS(), actualMaxIncomeForBracket);
                case FilingStatus.HEAD_OF_HOUSEHOLD ->
                    assertEquals(individualBracketDetails.getMaxIncomeHOH(), actualMaxIncomeForBracket);
            }
        }
    }

    /**
     * Verifies that the expected min income is returned for each tax bracket for a given year and filing status
     *
     * @param taxYear       Tax year
     * @param filingStatus  Filing status
     */
    @ParameterizedTest
    @CsvSource({
            "2024, SINGLE",
            "2024, MARRIED_FILING_JOINTLY",
            "2024, SURVIVING_SPOUSE",
            "2024, MARRIED_FILING_SEPARATELY",
            "2024, HEAD_OF_HOUSEHOLD",
            "2025, SINGLE",
            "2025, MARRIED_FILING_JOINTLY",
            "2025, SURVIVING_SPOUSE",
            "2025, MARRIED_FILING_SEPARATELY",
            "2025, HEAD_OF_HOUSEHOLD"
    })
    public void getMinIncomeForBracket_returnsExpectedMinIncome(int taxYear, FilingStatus filingStatus) {

        // Search for the tax brackets applicable to the given tax year, and
        // sort in ascending order based on the marginal tax rate
        List<? extends com.jack.tax.models.interfaces.BracketDetails> allBracketDetails = createMockedBracketDetails()
                .stream()
                .filter(bd -> bd.getTaxYear() == taxYear)
                .sorted(Comparator.comparingInt(BracketDetails::getTaxRate))
                .toList();

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        // Iterate through the bracket details applicable to the given tax year
        for (com.jack.tax.models.interfaces.BracketDetails individualBracketDetails : allBracketDetails) {

            // Call the method under test to get the actual min income for a bracket
            double actualMinIncomeForBracket = taxCalculationService
                    .getMinIncomeForBracket(filingStatus, individualBracketDetails);

            // Take action with the max income for bracket verification based on the filing status
            switch(filingStatus) {
                case FilingStatus.SINGLE ->
                        assertEquals(individualBracketDetails.getMinIncomeSingle(), actualMinIncomeForBracket);
                case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE ->
                        assertEquals(individualBracketDetails.getMinIncomeMFJ(), actualMinIncomeForBracket);
                case FilingStatus.MARRIED_FILING_SEPARATELY ->
                        assertEquals(individualBracketDetails.getMinIncomeMFS(), actualMinIncomeForBracket);
                case FilingStatus.HEAD_OF_HOUSEHOLD ->
                        assertEquals(individualBracketDetails.getMinIncomeHOH(), actualMinIncomeForBracket);
            }
        }
    }

    /**
     * Verifies that when the taxable income exceeds the max income for a bracket that the user is
     * subject to taxation against the whole bracket
     *
     * @param taxYear      Tax year
     * @param filingStatus Filing status
     */
    @ParameterizedTest
    @CsvSource({
            "2024, SINGLE",
            "2024, MARRIED_FILING_JOINTLY",
            "2024, SURVIVING_SPOUSE",
            "2024, MARRIED_FILING_SEPARATELY",
            "2024, HEAD_OF_HOUSEHOLD",
            "2025, SINGLE",
            "2025, MARRIED_FILING_JOINTLY",
            "2025, SURVIVING_SPOUSE",
            "2025, MARRIED_FILING_SEPARATELY",
            "2025, HEAD_OF_HOUSEHOLD"
    })
    public void getApplicableIncomeForBracket_taxableIncomeExceedsMaxBracketIncome_taxedForWholeBracket(
            int taxYear, FilingStatus filingStatus) {

        // Search for the tax brackets applicable to the given tax year, and
        // sort in ascending order based on the marginal tax rate
        List<? extends com.jack.tax.models.interfaces.BracketDetails> allBracketDetails = createMockedBracketDetails()
                .stream()
                .filter(bd -> bd.getTaxYear() == taxYear)
                .sorted(Comparator.comparingInt(BracketDetails::getTaxRate))
                .toList();

        // Get the first bracket's details
        com.jack.tax.models.interfaces.BracketDetails firstBracketsDetails = allBracketDetails.get(0);

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        double taxableIncome = 0.0;
        double expectedTaxableIncomeForBracket = -1.0;
        double actualTaxableIncomeForBracket = -2.0;

        switch (filingStatus) {
            case FilingStatus.SINGLE:
                // Make it so the taxable income exceeds the first bracket's max by one dollar
                taxableIncome = firstBracketsDetails.getMaxIncomeSingle() + 1.0;

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = firstBracketsDetails.getMaxIncomeSingle() -
                        firstBracketsDetails.getMinIncomeSingle();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.SINGLE, firstBracketsDetails, taxableIncome);
                break;

            case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE:
                // Make it so the taxable income exceeds the first bracket's max by one dollar
                taxableIncome = firstBracketsDetails.getMaxIncomeMFJ() + 1.0;

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = firstBracketsDetails.getMaxIncomeMFJ() -
                        firstBracketsDetails.getMinIncomeMFJ();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.MARRIED_FILING_JOINTLY, firstBracketsDetails, taxableIncome);
                break;

            case FilingStatus.MARRIED_FILING_SEPARATELY:
                // Make it so the taxable income exceeds the first bracket's max by one dollar
                taxableIncome = firstBracketsDetails.getMaxIncomeMFS() + 1.0;

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = firstBracketsDetails.getMaxIncomeMFS() -
                        firstBracketsDetails.getMinIncomeMFS();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.MARRIED_FILING_SEPARATELY, firstBracketsDetails, taxableIncome);
                break;

            case FilingStatus.HEAD_OF_HOUSEHOLD:
                // Make it so the taxable income exceeds the first bracket's max by one dollar
                taxableIncome = firstBracketsDetails.getMaxIncomeHOH() + 1.0;

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = firstBracketsDetails.getMaxIncomeHOH() -
                    firstBracketsDetails.getMinIncomeHOH();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.HEAD_OF_HOUSEHOLD, firstBracketsDetails, taxableIncome);
                break;
        }

        assertEquals(expectedTaxableIncomeForBracket, actualTaxableIncomeForBracket);
    }

    /**
     * Verifies that when the taxable income is in the middle of a bracket that the user is
     * subject to taxation on a prorated basis for the bracket
     *
     * @param taxYear      Tax year
     * @param filingStatus Filing status
     */
    @ParameterizedTest
    @CsvSource({
            "2024, SINGLE",
            "2024, MARRIED_FILING_JOINTLY",
            "2024, SURVIVING_SPOUSE",
            "2024, MARRIED_FILING_SEPARATELY",
            "2024, HEAD_OF_HOUSEHOLD",
            "2025, SINGLE",
            "2025, MARRIED_FILING_JOINTLY",
            "2025, SURVIVING_SPOUSE",
            "2025, MARRIED_FILING_SEPARATELY",
            "2025, HEAD_OF_HOUSEHOLD"
    })
    public void getApplicableIncomeForBracket_taxableIncomeWithinBracket_taxedForProRatedAmountInBracket(
            int taxYear, FilingStatus filingStatus) {

        // Search for the tax brackets applicable to the given tax year, and
        // sort in ascending order based on the marginal tax rate
        List<? extends com.jack.tax.models.interfaces.BracketDetails> allBracketDetails = createMockedBracketDetails()
                .stream()
                .filter(bd -> bd.getTaxYear() == taxYear)
                .sorted(Comparator.comparingInt(BracketDetails::getTaxRate))
                .toList();

        // Get the first bracket's details
        com.jack.tax.models.interfaces.BracketDetails firstBracketsDetails = allBracketDetails.get(0);

        // Create an instance of the class under test
        TaxCalculationService taxCalculationService = createTaxCalculationServiceWithMockedDependencies();

        double taxableIncome = 0.0;
        double expectedTaxableIncomeForBracket = -1.0;
        double actualTaxableIncomeForBracket = -2.0;

        switch (filingStatus) {
            case FilingStatus.SINGLE:
                // Make it so the taxable income exceeds is in the middle of the bracket
                taxableIncome = ((firstBracketsDetails.getMaxIncomeSingle() +
                        firstBracketsDetails.getMinIncomeSingle()) / 2.0);

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = taxableIncome -
                        firstBracketsDetails.getMinIncomeSingle();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.SINGLE, firstBracketsDetails, taxableIncome);
                break;

            case FilingStatus.MARRIED_FILING_JOINTLY, FilingStatus.SURVIVING_SPOUSE:
                // Make it so the taxable income exceeds is in the middle of the bracket
                taxableIncome = ((firstBracketsDetails.getMaxIncomeMFJ() +
                        firstBracketsDetails.getMinIncomeMFJ()) / 2.0);

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = taxableIncome -
                        firstBracketsDetails.getMinIncomeMFJ();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.MARRIED_FILING_JOINTLY, firstBracketsDetails, taxableIncome);
                break;

            case FilingStatus.MARRIED_FILING_SEPARATELY:
                // Make it so the taxable income exceeds is in the middle of the bracket
                taxableIncome = ((firstBracketsDetails.getMaxIncomeMFS() +
                        firstBracketsDetails.getMinIncomeMFS()) / 2.0);

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = taxableIncome -
                        firstBracketsDetails.getMinIncomeMFS();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.MARRIED_FILING_SEPARATELY, firstBracketsDetails, taxableIncome);
                break;

            case FilingStatus.HEAD_OF_HOUSEHOLD:
                // Make it so the taxable income exceeds is in the middle of the bracket
                taxableIncome = ((firstBracketsDetails.getMaxIncomeHOH() +
                        firstBracketsDetails.getMinIncomeHOH()) / 2.0);

                // Compute the expected taxable income for bracket
                expectedTaxableIncomeForBracket = taxableIncome -
                        firstBracketsDetails.getMinIncomeHOH();

                // Call the method under test
                actualTaxableIncomeForBracket = taxCalculationService.getApplicableIncomeForBracket(
                        FilingStatus.HEAD_OF_HOUSEHOLD, firstBracketsDetails, taxableIncome);
                break;
        }

        assertEquals(expectedTaxableIncomeForBracket, actualTaxableIncomeForBracket);
    }

    /**
     * Supplies the expected tax years, derived directly from the mocked standard deduction details.
     */
    private static Stream<Arguments> expectedYearsProvider() {
        List<Integer> expectedYears = createMockedStandardDeductionDetails().stream()
                .map(StandardDeductionDetails::getTaxYear)
                .sorted(Collections.reverseOrder())
                .toList();
        return Stream.of(Arguments.of(expectedYears));
    }

    /**
     * Creates and returns a collection of mock standard deduction details
     */
    private static List<StandardDeductionDetails> createMockedStandardDeductionDetails() {

        StandardDeductionDetails details2024 = new StandardDeductionDetails();
        details2024.setTaxYear(2024);
        details2024.setSingle(14600);
        details2024.setMfj(29200);
        details2024.setMfs(14600);
        details2024.setHoh(21900);

        StandardDeductionDetails details2025 = new StandardDeductionDetails();
        details2025.setTaxYear(2025);
        details2025.setSingle(15750);
        details2025.setMfj(31500);
        details2025.setMfs(15750);
        details2025.setHoh(23625);

        return List.of(details2024, details2025);
    }

    /**
     * Supplies the expected tax year details for the given tax year
     *
     * @param taxYear Tax year for which we need to create a tax year details model
     */
    private static TaxYearDetails expectedTaxYearDetailsProvider(int taxYear) {

        // Search for the standard deduction details for the given tax year
        StandardDeductionDetails standardDeductionDetails = createMockedStandardDeductionDetails()
                .stream()
                .filter(sd -> sd.getTaxYear() == taxYear)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not locate standard deduction details for" +
                        " tax year " + taxYear));

        // Search for the tax brackets applicable to the given tax year
        List<? extends com.jack.tax.models.interfaces.BracketDetails> bracketDetails = createMockedBracketDetails()
                .stream()
                .filter(bd -> bd.getTaxYear() == taxYear)
                .sorted(Comparator.comparingInt(BracketDetails::getTaxRate))
                .toList();

        TaxYearDetails taxYearDetails = new TaxYearDetails();
        taxYearDetails.setTaxYear(taxYear);
        taxYearDetails.setStandardDeductionDetails(standardDeductionDetails);
        taxYearDetails.setBracketDetails((List<com.jack.tax.models.interfaces.BracketDetails>) bracketDetails);
        return taxYearDetails;
    }

    /**
     * Creates and returns a list of mock tax bracket details
     */
    private static List<BracketDetails> createMockedBracketDetails() {

        BracketDetails taxRate10Year2024 = new BracketDetails();
        taxRate10Year2024.setTaxYear(2024);
        taxRate10Year2024.setTaxRate(10);
        taxRate10Year2024.setMinIncomeSingle(0);
        taxRate10Year2024.setMaxIncomeSingle(11600);
        taxRate10Year2024.setMinIncomeMFJ(0);
        taxRate10Year2024.setMaxIncomeMFJ(23200);
        taxRate10Year2024.setMinIncomeMFS(0);
        taxRate10Year2024.setMaxIncomeMFS(11600);
        taxRate10Year2024.setMinIncomeHOH(0);
        taxRate10Year2024.setMaxIncomeHOH(16550);

        BracketDetails taxRate12Year2024 = new BracketDetails();
        taxRate12Year2024.setTaxYear(2024);
        taxRate12Year2024.setTaxRate(12);
        taxRate12Year2024.setMinIncomeSingle(11601);
        taxRate12Year2024.setMaxIncomeSingle(47150);
        taxRate12Year2024.setMinIncomeMFJ(23200);
        taxRate12Year2024.setMaxIncomeMFJ(94300);
        taxRate12Year2024.setMinIncomeMFS(11601);
        taxRate12Year2024.setMaxIncomeMFS(47150);
        taxRate12Year2024.setMinIncomeHOH(16551);
        taxRate12Year2024.setMaxIncomeHOH(63100);

        BracketDetails taxRate22Year2024 = new BracketDetails();
        taxRate22Year2024.setTaxYear(2024);
        taxRate22Year2024.setTaxRate(22);
        taxRate22Year2024.setMinIncomeSingle(47151);
        taxRate22Year2024.setMaxIncomeSingle(100525);
        taxRate22Year2024.setMinIncomeMFJ(94301);
        taxRate22Year2024.setMaxIncomeMFJ(201050);
        taxRate22Year2024.setMinIncomeMFS(47151);
        taxRate22Year2024.setMaxIncomeMFS(100525);
        taxRate22Year2024.setMinIncomeHOH(63101);
        taxRate22Year2024.setMaxIncomeHOH(100500);

        BracketDetails taxRate24Year2024 = new BracketDetails();
        taxRate24Year2024.setTaxYear(2024);
        taxRate24Year2024.setTaxRate(24);
        taxRate24Year2024.setMinIncomeSingle(100526);
        taxRate24Year2024.setMaxIncomeSingle(191950);
        taxRate24Year2024.setMinIncomeMFJ(201051);
        taxRate24Year2024.setMaxIncomeMFJ(383900);
        taxRate24Year2024.setMinIncomeMFS(100526);
        taxRate24Year2024.setMaxIncomeMFS(191950);
        taxRate24Year2024.setMinIncomeHOH(100501);
        taxRate24Year2024.setMaxIncomeHOH(191950);

        BracketDetails taxRate32Year2024 = new BracketDetails();
        taxRate32Year2024.setTaxYear(2024);
        taxRate32Year2024.setTaxRate(32);
        taxRate32Year2024.setMinIncomeSingle(191951);
        taxRate32Year2024.setMaxIncomeSingle(243725);
        taxRate32Year2024.setMinIncomeMFJ(383901);
        taxRate32Year2024.setMaxIncomeMFJ(487450);
        taxRate32Year2024.setMinIncomeMFS(191951);
        taxRate32Year2024.setMaxIncomeMFS(243725);
        taxRate32Year2024.setMinIncomeHOH(191951);
        taxRate32Year2024.setMaxIncomeHOH(243700);

        BracketDetails taxRate35Year2024 = new BracketDetails();
        taxRate35Year2024.setTaxYear(2024);
        taxRate35Year2024.setTaxRate(35);
        taxRate35Year2024.setMinIncomeSingle(243726);
        taxRate35Year2024.setMaxIncomeSingle(609350);
        taxRate35Year2024.setMinIncomeMFJ(487451);
        taxRate35Year2024.setMaxIncomeMFJ(731200);
        taxRate35Year2024.setMinIncomeMFS(243726);
        taxRate35Year2024.setMaxIncomeMFS(365600);
        taxRate35Year2024.setMinIncomeHOH(243701);
        taxRate35Year2024.setMaxIncomeHOH(609350);

        BracketDetails taxRate37Year2024 = new BracketDetails();
        taxRate37Year2024.setTaxYear(2024);
        taxRate37Year2024.setTaxRate(37);
        taxRate37Year2024.setMinIncomeSingle(609351);
        taxRate37Year2024.setMaxIncomeSingle(2147483647);
        taxRate37Year2024.setMinIncomeMFJ(731201);
        taxRate37Year2024.setMaxIncomeMFJ(2147483647);
        taxRate37Year2024.setMinIncomeMFS(365601);
        taxRate37Year2024.setMaxIncomeMFS(2147483647);
        taxRate37Year2024.setMinIncomeHOH(609351);
        taxRate37Year2024.setMaxIncomeHOH(2147483647);

        BracketDetails taxRate10Year2025 = new BracketDetails();
        taxRate10Year2025.setTaxYear(2025);
        taxRate10Year2025.setTaxRate(10);
        taxRate10Year2025.setMinIncomeSingle(0);
        taxRate10Year2025.setMaxIncomeSingle(11925);
        taxRate10Year2025.setMinIncomeMFJ(0);
        taxRate10Year2025.setMaxIncomeMFJ(23850);
        taxRate10Year2025.setMinIncomeMFS(0);
        taxRate10Year2025.setMaxIncomeMFS(11925);
        taxRate10Year2025.setMinIncomeHOH(0);
        taxRate10Year2025.setMaxIncomeHOH(17000);

        BracketDetails taxRate12Year2025 = new BracketDetails();
        taxRate12Year2025.setTaxYear(2025);
        taxRate12Year2025.setTaxRate(12);
        taxRate12Year2025.setMinIncomeSingle(11926);
        taxRate12Year2025.setMaxIncomeSingle(48475);
        taxRate12Year2025.setMinIncomeMFJ(23851);
        taxRate12Year2025.setMaxIncomeMFJ(96950);
        taxRate12Year2025.setMinIncomeMFS(11926);
        taxRate12Year2025.setMaxIncomeMFS(48475);
        taxRate12Year2025.setMinIncomeHOH(17001);
        taxRate12Year2025.setMaxIncomeHOH(64850);

        BracketDetails taxRate22Year2025 = new BracketDetails();
        taxRate22Year2025.setTaxYear(2025);
        taxRate22Year2025.setTaxRate(22);
        taxRate22Year2025.setMinIncomeSingle(48476);
        taxRate22Year2025.setMaxIncomeSingle(103350);
        taxRate22Year2025.setMinIncomeMFJ(96951);
        taxRate22Year2025.setMaxIncomeMFJ(206700);
        taxRate22Year2025.setMinIncomeMFS(48476);
        taxRate22Year2025.setMaxIncomeMFS(103350);
        taxRate22Year2025.setMinIncomeHOH(64851);
        taxRate22Year2025.setMaxIncomeHOH(103350);

        BracketDetails taxRate24Year2025 = new BracketDetails();
        taxRate24Year2025.setTaxYear(2025);
        taxRate24Year2025.setTaxRate(24);
        taxRate24Year2025.setMinIncomeSingle(103351);
        taxRate24Year2025.setMaxIncomeSingle(197300);
        taxRate24Year2025.setMinIncomeMFJ(206701);
        taxRate24Year2025.setMaxIncomeMFJ(394600);
        taxRate24Year2025.setMinIncomeMFS(103351);
        taxRate24Year2025.setMaxIncomeMFS(197300);
        taxRate24Year2025.setMinIncomeHOH(103351);
        taxRate24Year2025.setMaxIncomeHOH(197300);

        BracketDetails taxRate32Year2025 = new BracketDetails();
        taxRate32Year2025.setTaxYear(2025);
        taxRate32Year2025.setTaxRate(32);
        taxRate32Year2025.setMinIncomeSingle(197301);
        taxRate32Year2025.setMaxIncomeSingle(250525);
        taxRate32Year2025.setMinIncomeMFJ(394601);
        taxRate32Year2025.setMaxIncomeMFJ(501050);
        taxRate32Year2025.setMinIncomeMFS(197301);
        taxRate32Year2025.setMaxIncomeMFS(250525);
        taxRate32Year2025.setMinIncomeHOH(197301);
        taxRate32Year2025.setMaxIncomeHOH(250500);

        BracketDetails taxRate35Year2025 = new BracketDetails();
        taxRate35Year2025.setTaxYear(2025);
        taxRate35Year2025.setTaxRate(35);
        taxRate35Year2025.setMinIncomeSingle(250526);
        taxRate35Year2025.setMaxIncomeSingle(626350);
        taxRate35Year2025.setMinIncomeMFJ(501051);
        taxRate35Year2025.setMaxIncomeMFJ(751600);
        taxRate35Year2025.setMinIncomeMFS(250526);
        taxRate35Year2025.setMaxIncomeMFS(375800);
        taxRate35Year2025.setMinIncomeHOH(250501);
        taxRate35Year2025.setMaxIncomeHOH(626350);

        BracketDetails taxRate37Year2025 = new BracketDetails();
        taxRate37Year2025.setTaxYear(2025);
        taxRate37Year2025.setTaxRate(37);
        taxRate37Year2025.setMinIncomeSingle(626351);
        taxRate37Year2025.setMaxIncomeSingle(2147483647);
        taxRate37Year2025.setMinIncomeMFJ(751601);
        taxRate37Year2025.setMaxIncomeMFJ(2147483647);
        taxRate37Year2025.setMinIncomeMFS(375801);
        taxRate37Year2025.setMaxIncomeMFS(2147483647);
        taxRate37Year2025.setMinIncomeHOH(626351);
        taxRate37Year2025.setMaxIncomeHOH(2147483647);

        return List.of(
                taxRate10Year2024, taxRate12Year2024, taxRate22Year2024, taxRate24Year2024,
                taxRate32Year2024, taxRate35Year2024, taxRate37Year2024,
                taxRate10Year2025, taxRate12Year2025, taxRate22Year2025, taxRate24Year2025,
                taxRate32Year2025, taxRate35Year2025, taxRate37Year2025
        );
    }

    /**
     * Creates and returns a Tax Calculation Service instance initialized with mocked dependencies
     */
    private TaxCalculationService createTaxCalculationServiceWithMockedDependencies() {

        return new TaxCalculationService(mockitoStandardDeductionRepository, mockitoBracketRepository);
    }
}
