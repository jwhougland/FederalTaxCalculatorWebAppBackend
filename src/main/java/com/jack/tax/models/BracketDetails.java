package com.jack.tax.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Encapsulates the min/max income ranges for tax brackets
 * for all types of filers.  Each instance represents a tax year.
 */
@Document(collection = "Brackets")
public class BracketDetails {

    /**
     * Applicable tax year
     */
    private int taxYear;

    /**
     * Marginal tax rate for the bracket
     */
    private int taxRate;

    /**
     * Min income (USD) - single filer
     */
    private int minIncomeSingle;

    /**
     * Max income (USD) - single filer
     */
    private int maxIncomeSingle;

    /**
     * Min income (USD) - married filing jointly (or surviving spouse) filer
     */
    private int minIncomeMFJ;

    /**
     * Max income (USD) - married filing jointly (or surving spouse) filer
     */
    private int maxIncomeMFJ;

    /**
     * Min income (USD) - married filing separately filer
     */
    private int minIncomeMFS;

    /**
     * Max income (USD) - married filing separately filer
     */
    private int maxIncomeMFS;

    /**
     * Min income (USD) - head of household filer
     */
    private int minIncomeHOH;

    /**
     * Max income (USD) - head of household filer
     */
    private int maxIncomeHOH;
}
