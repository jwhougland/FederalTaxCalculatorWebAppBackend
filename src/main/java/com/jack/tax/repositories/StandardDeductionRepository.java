package com.jack.tax.repositories;

import com.jack.tax.models.StandardDeductionDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Used to perform CRUD operations of Standard Deduction entities
 */
public interface StandardDeductionRepository extends MongoRepository<StandardDeductionDetails, Integer> {
    // Empty on purpose
}
