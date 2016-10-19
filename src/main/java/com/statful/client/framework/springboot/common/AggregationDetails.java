package com.statful.client.framework.springboot.common;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFreq;

public class AggregationDetails {
    private Aggregation aggregation;
    private AggregationFreq aggregationFreq;

    public AggregationDetails(Aggregation aggregation, AggregationFreq aggregationFreq) {
        this.aggregation = aggregation;
        this.aggregationFreq = aggregationFreq;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public AggregationFreq getAggregationFreq() {
        return aggregationFreq;
    }
}
