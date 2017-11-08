package com.statful.client.framework.springboot.common;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum representing the available metric types.
 */
public enum MetricType {
    COUNTER("count"),
    TIMER("time"),
    GAUGE("gauge");

    private final String name;

    private static final Map<String, MetricType> CONVERTER = Stream.of(MetricType.values())
            .collect(Collectors.toMap(MetricType::getName, Function.identity()));

    MetricType(String name) {
        this.name = name;
    }

    public static MetricType fromName(String name) {
        return Optional.ofNullable(CONVERTER.get(name))
                .orElseThrow(IllegalArgumentException::new);
    }
    private String getName() {
        return name;
    }
}
