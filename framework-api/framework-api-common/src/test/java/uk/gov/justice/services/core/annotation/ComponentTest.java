package uk.gov.justice.services.core.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_API;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_CONTROLLER;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_HANDLER;
import static uk.gov.justice.services.core.annotation.Component.EVENT_API;
import static uk.gov.justice.services.core.annotation.Component.EVENT_INDEXER;
import static uk.gov.justice.services.core.annotation.Component.EVENT_LISTENER;
import static uk.gov.justice.services.core.annotation.Component.EVENT_PROCESSOR;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.core.annotation.Component.contains;
import static uk.gov.justice.services.core.annotation.Component.valueOf;

import org.junit.jupiter.api.Test;

public class ComponentTest {

    @Test
    public void shouldConstructComponentByTierAndPillar() {
        assertThat(valueOf("command", "api"), is(COMMAND_API));
        assertThat(valueOf("command", "controller"), is(COMMAND_CONTROLLER));
        assertThat(valueOf("command", "handler"), is(COMMAND_HANDLER));
        assertThat(valueOf("event", "listener"), is(EVENT_LISTENER));
        assertThat(valueOf("event", "processor"), is(EVENT_PROCESSOR));
        assertThat(valueOf("event", "indexer"), is(EVENT_INDEXER));
        assertThat(valueOf("query", "api"), is(QUERY_API));
        assertThat(valueOf("event", "api"), is(EVENT_API));
    }

    @Test
    public void shouldThrowExceptionIfPillarInvalid() {

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                valueOf("invalidPillar", "api")
        );

        assertThat(illegalArgumentException.getMessage(), is("No component matches pillar: invalidPillar, tier: api"));
    }

    @Test
    public void shouldThrowExceptionIfTierInvalid() {

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                valueOf("commands", "invalidTier")
        );

        assertThat(illegalArgumentException.getMessage(), is("No component matches pillar: commands, tier: invalidTier"));
    }

    @Test
    public void shouldReturnTrueIfContainsGivenString() {

        assertTrue(contains("COMMAND_API"));
        assertTrue(contains("COMMAND_CONTROLLER"));
        assertTrue(contains("COMMAND_HANDLER"));
        assertTrue(contains("EVENT_LISTENER"));
        assertTrue(contains("EVENT_PROCESSOR"));
        assertTrue(contains("EVENT_INDEXER"));
        assertTrue(contains("QUERY_API"));
        assertTrue(contains("QUERY_CONTROLLER"));
        assertTrue(contains("QUERY_VIEW"));
        assertTrue(contains("EVENT_API"));
    }

    @Test
    public void shouldReturnFalseIfDoesNotContainGivenString() {
        assertFalse(contains("COMMAND_API_aaa"));
        assertFalse(contains("UNKNOWN"));
    }
}