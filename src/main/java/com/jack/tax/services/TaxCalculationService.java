package com.jack.tax.services;

import com.jack.tax.models.StandardDeductionDetails;
import com.jack.tax.repositories.BracketRepository;
import com.jack.tax.repositories.StandardDeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
