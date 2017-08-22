package com.statful.client.framework.springboot.config;

import com.statful.client.framework.springboot.config.SpringbootClientConfiguration.Metrics.Tags;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.statful.client.framework.springboot.config.SpringbootClientConfiguration.Metrics;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpringbootClientConfigurationTest {

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private SpringbootClientConfiguration victim;

    @Before
    public void setup() {
        this.victim = new SpringbootClientConfiguration();
    }

    @Test
    public void testTagsAreNotNullEvenIfSetNull() {
        this.victim.setMetrics(new Metrics());
        assertNotNull(this.victim.getMetrics().getTags());
    }

    @Test
    public void testTagsAreReturned() {

        Metrics metrics = new Metrics();
        Tags tags = new Tags();

        tags.setName(NAME);
        tags.setValue(VALUE);

        List<Tags> tagsList = new ArrayList<>();
        tagsList.add(tags);
        metrics.setTags(tagsList);
        this.victim.setMetrics(metrics);

        Metrics result = this.victim.getMetrics();
        assertEquals(NAME, result.getTags().get(0).getName());
        assertEquals(VALUE, result.getTags().get(0).getValue());
    }
}