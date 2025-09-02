package com.jack.tax.controllers;

import com.jack.tax.models.FilingStatus;
import com.jack.tax.models.InputModel;
import com.jack.tax.models.interfaces.FilingStatusResponse;
import com.jack.tax.models.interfaces.OutputModel;
import com.jack.tax.services.TaxCalculationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API controller for receiving tax calculation requests and
 * providing responses with computed tax information.
 */
@RestController
@RequestMapping("/api")
// Allow React frontend
public class TaxCalculatorApiController {

    /**
     * Contains the business logic for tax calculations.
     */
    private final TaxCalculationService taxCalculationService;

    /**
     * Creates a fully initialized Tax Calculator API Controller using the given data.
     *
     * @param taxCalculationService Contains the business logic for tax calculations.
     */
    @Autowired
    public TaxCalculatorApiController(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }

    /**
     * Returns the tax years the app supports based on what is loaded in persistent storage.
     */
    @GetMapping("/taxYears")
    public List<Integer> getTaxYears() {

        return taxCalculationService.getSupportedTaxYears();
    }

    /**
     * Returns a list of filing statuses so the user can pick one.
     */
    @GetMapping("/filingStatuses")
    public List<FilingStatusResponse> getFilingStatuses() {

        return Arrays.stream(FilingStatus.values())
                .map(com.jack.tax.models.FilingStatusResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Calculates federal tax owed based on the given input data.
     *
     * @param inputModel Encapsulates user entered data about gross income,
     *                  filing status, deductions, and credits for a specified tax year.
     * @return Federal tax owed (USD), marginal tax rate (%), effective tax rate (%), take home pay (USD).
     */
    @PostMapping("/taxCalculation")
    public List<OutputModel> calculateFederalTaxOwed(@Valid @RequestBody InputModel inputModel) {

        // Return a list with one and only one element with tax calculation output content
        return Collections.singletonList(taxCalculationService.calculateTaxes(inputModel));
    }
}
