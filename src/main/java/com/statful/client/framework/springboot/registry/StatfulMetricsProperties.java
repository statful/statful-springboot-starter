package com.statful.client.framework.springboot.registry;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties("statful.metrics.properties")
public class StatfulMetricsProperties {

    /**
     * A key in this map is the prefix that will match against the metrics, replacing any that match with the value associated (the alias).
     *
     * E.g. If key is "jvm" and value is "virtual" then every metric starting with "jvm" will be replaced with "virtual".
     */
    private Map<String, String> alias = new HashMap<>();

    /**
     * A key in this map is the prefix that will match against the metrics, adding to any that match the tags specified in the value.
     * Note that this action will be executed before replacing the metric name with the alias, meaning that the key should never be
     * the alias intended for the metrics.

     * E.g. If key is "jvm" and value is "env=prod" then every metric starting with "jvm" will contain the tag "env=prod".
     *
     * Tags follow the format "tagKey=tagValue". Multiple tags are separated by ';'
     */
    private Map<String, String> tags = new HashMap<>();

    /**
     * List of prefixes that will match against the metrics. If this is empty all metrics will be accepted. If it contains any string then
     * only metrics that match will be accepted.
     */
    private List<String> acceptedMetrics = new ArrayList<>();

    /**
     * The map getters return a map sorted so the shorter prefixes (more global) are checked last
     */

    public Map<String, String> getAlias() {
        return new TreeMap<>(alias).descendingMap();
    }

    public Map<String, String> getTags() {
        return new TreeMap<>(tags).descendingMap();
    }

    public List<String> getAcceptedMetrics() {
        return acceptedMetrics;
    }

    public void setAlias(Map<String, String> alias) {
        this.alias = alias;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void setAcceptedMetrics(List<String> acceptedMetrics) {
        this.acceptedMetrics = acceptedMetrics;
    }
}
