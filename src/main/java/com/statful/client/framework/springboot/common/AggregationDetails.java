package com.statful.client.framework.springboot.common;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;

public class AggregationDetails {
    private Aggregation aggregation;
    private AggregationFrequency aggregationFreq;

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
