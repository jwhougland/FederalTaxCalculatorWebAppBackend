package com.jack.tax.services;

import com.jack.tax.models.StandardDeductionDetails;
import com.jack.tax.repositories.BracketRepository;
import com.jack.tax.repositories.StandardDeductionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
     * Verifies expected tax years are returned
     */
    @ParameterizedTest
    @MethodSource("expectedYearsProvider")
    public void getSupportedTaxYears_shouldReturnExpectedYears(List<Integer> expectedTaxYears) {

        // Mock the behavior of standard deductions repository
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
     * Creates and returns a Tax Calculation Service instance initialized with mocked dependencies
     */
    private TaxCalculationService createTaxCalculationServiceWithMockedDependencies() {

        return new TaxCalculationService(mockitoStandardDeductionRepository, mockitoBracketRepository);
    }
}
