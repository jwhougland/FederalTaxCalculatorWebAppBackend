package com.jack.tax.models;

import com.jack.tax.utils.AppConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InputModel
 */
public class InputModelTests {

    /**
     * Used to exercise tests on the annotation-based validations in the class under test.
     */
    private static Validator validator;

    /**
     * A valid gross income value (USD)
     */
    private static final double VALID_GROSS_INCOME = 100000.50;

    /**
     * A valid total deductions value (USD)
     */
    private static final double VALID_TOTAL_DEDUCTIONS = 13000.0;

    /**
     * A valid total credits value (USD)
     */
    private static final double VALID_TOTAL_CREDITS = 100.25;

    /**
     * A valid tax year value
     */
    private static final int VALID_TAX_YEAR = 2025;

    /**
     * A valid filing status
     */
    private static final FilingStatus VALID_FILING_STATUS = FilingStatus.SINGLE;

    /**
     * Field name - grossIncome
     */
    private static final String GROSS_INCOME_FIELD = "grossIncome";

    /**
     * Field name - totalDeductions
     */
    private static final String TOTAL_DEDUCTIONS_FIELD = "totalDeductions";

    /**
     * Field name - totalCredits
     */
    private static final String TOTAL_CREDITS_FIELD = "totalCredits";

    /**
     * Sets up the instance validator field in a consistent way for each test
     */
    @BeforeAll
    public static void setupValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Verifies the getters and setters work as expected when valid values are being applied
     */
    @Test
    public void gettersAndSettersWorkWithValidValues() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        assertEquals(VALID_GROSS_INCOME, inputModel.getGrossIncome());
        assertEquals(VALID_TOTAL_DEDUCTIONS, inputModel.getTotalDeductions());
        assertEquals(VALID_TOTAL_CREDITS, inputModel.getTotalCredits());
        assertEquals(VALID_TAX_YEAR, inputModel.getSelectedTaxYear());
        assertEquals(VALID_FILING_STATUS, inputModel.getSelectedFilingStatus());
    }

    /**
     * Verifies that a validation issue will be reported when the gross income is less than the minimum value allowed
     */
    @Test
    public void testValidationWorksForMinGrossIncome() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the gross income so its value will be less than the minimum allowed
        inputModel.setGrossIncome(getInvalidMinUsdAmount());

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, GROSS_INCOME_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the gross income is greater than the maximum value allowed
     */
    @Test
    public void testValidationWorksForMaxGrossIncome() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the gross income so its value will be greater than the maximum allowed
        inputModel.setGrossIncome(getInvaldMaxUsdAmount());

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, GROSS_INCOME_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the precision for the gross income is greater than allowed
     */
    @Test
    public void testValidationWorksForGrossIncomePrecision() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the gross income so its value will have more precision than what is allowed
        inputModel.setGrossIncome(getInvalidPrecision(inputModel.getGrossIncome()));

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, GROSS_INCOME_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the total deductions is less than the minimum value allowed
     */
    @Test
    public void testValidationWorksForMinTotalDeductions() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the total deductions so its value will be less than the minimum allowed
        inputModel.setTotalDeductions(getInvalidMinUsdAmount());

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, TOTAL_DEDUCTIONS_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the total deductions is greater than the maximum value allowed
     */
    @Test
    public void testValidationWorksForMaxTotalDeductions() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the total deductions so its value will be greater than the maximum allowed
        inputModel.setTotalDeductions(getInvaldMaxUsdAmount());

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, TOTAL_DEDUCTIONS_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the precision for the total deductions is greater than allowed
     */
    @Test
    public void testValidationWorksForTotalDeductionsPrecision() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the total deductions so its value will have more precision than what is allowed
        inputModel.setTotalDeductions(getInvalidPrecision(inputModel.getTotalDeductions()));

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, TOTAL_DEDUCTIONS_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the total credits is less than the minimum value allowed
     */
    @Test
    public void testValidationWorksForMinTotalCredits() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the total credits so its value will be less than the minimum allowed
        inputModel.setTotalCredits(getInvalidMinUsdAmount());

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, TOTAL_CREDITS_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the total credits is greater than the maximum value allowed
     */
    @Test
    public void testValidationWorksForMaxTotalCredits() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the total credits so its value will be greater than the maximum allowed
        inputModel.setTotalCredits(getInvaldMaxUsdAmount());

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, TOTAL_CREDITS_FIELD));
    }

    /**
     * Verifies that a validation issue will be reported when the precision for the total credits is greater than allowed
     */
    @Test
    public void testValidationWorksForTotalCreditsPrecision() {

        // Create an instance of the class under test with valid values
        InputModel inputModel = createInputModelWithValidValues();

        // Run an initial validation.  Everything should pass.
        Set<ConstraintViolation<InputModel>> initialViolations = validator.validate(inputModel);
        assertTrue(initialViolations.isEmpty());

        // Attempt to adjust the total credits so its value will have more precision than what is allowed
        inputModel.setTotalCredits(getInvalidPrecision(inputModel.getTotalCredits()));

        // Run an updated validation.  There should be a problem.
        Set<ConstraintViolation<InputModel>> updatedViolations = validator.validate(inputModel);
        assertFalse(updatedViolations.isEmpty());
        assertTrue(hasValidationIssueForField(updatedViolations, TOTAL_CREDITS_FIELD));
    }

    /**
     * Creates and returns a new input model with valid values applied
     */
    private InputModel createInputModelWithValidValues() {

        InputModel inputModel = new InputModel();
        inputModel.setGrossIncome(VALID_GROSS_INCOME);
        inputModel.setTotalDeductions(VALID_TOTAL_DEDUCTIONS);
        inputModel.setTotalCredits(VALID_TOTAL_CREDITS);
        inputModel.setSelectedTaxYear(VALID_TAX_YEAR);
        inputModel.setSelectedFilingStatus(VALID_FILING_STATUS);
        return inputModel;
    }

    /**
     * Returns an invalid min USD amount
     */
    private double getInvalidMinUsdAmount() {

        return Double.parseDouble(AppConstants.MIN_USD_AMOUNT_AS_STR) - 1.0;
    }

    /**
     * Returns an invalid max USD amount
     */
    private double getInvaldMaxUsdAmount() {

        return Double.parseDouble(AppConstants.MAX_USD_AMOUNT_AS_STR) + 1.0;
    }

    /**
     * Returns a value with too many digits after the decimal point
     * @param inputValue An input value
     */
    private double getInvalidPrecision(double inputValue) {

        return inputValue + 0.001;
    }

    /**
     * Returns a boolean indicating if there is a validation issue for the given field name
     *
     * @param violations Validation violations
     * @param fieldName  Field name to search in the violations object
     */
    private boolean hasValidationIssueForField(Set<ConstraintViolation<InputModel>> violations,
                                               String fieldName) {

        return violations
                .stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
