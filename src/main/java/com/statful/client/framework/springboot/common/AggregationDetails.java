package com.statful.client.framework.springboot.common;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;

/**
 * Holder class to represent and aggregation and an aggregation frequency.
 */
public class AggregationDetails {
    private Aggregation aggregation;
    private AggregationFrequency aggregationFreq;

    /**
     * Default constructor
     * @param aggregation An {@link Aggregation} aggregation
     * @param aggregationFreq An {@link AggregationFrequency} aggregation frequency
     */
    public AggregationDetails(Aggregation aggregation, AggregationFrequency aggregationFreq) {
        this.aggregation = aggregation;
        this.aggregationFreq = aggregationFreq;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public AggregationFrequency getAggregationFrequency() {
        return aggregationFreq;
    }
}
