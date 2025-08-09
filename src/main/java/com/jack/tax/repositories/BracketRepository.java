package com.jack.tax.repositories;

import com.jack.tax.models.BracketDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Used to perform CRUD operations on Bracket Details entities
 */
public interface BracketRepository extends MongoRepository<BracketDetails, org.bson.types.ObjectId> {
    // Empty on purpose
}
