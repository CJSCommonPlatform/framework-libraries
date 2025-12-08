package uk.gov.justice.services.common.converter.jackson.jsr353;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.USE_DEFAULTS;
import static javax.json.JsonValue.NULL;
import static javax.json.JsonValue.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.common.converter.jackson.jsr353.JsonIncludes.includeField;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * CUnit tests for the {@link JsonIncludes} class.
 */
@ExtendWith(MockitoExtension.class)
public class JsonIncludesTest {


    @Test
    public void shouldNotIncludeNullIfNonNullConfigured() {
        assertThat(includeField(NULL, NON_NULL), is(false));
    }

    @Test
    public void shouldIncludeNonNullIfNonNullConfigured() {
        assertThat(includeField(TRUE, NON_NULL), is(true));
    }

    @Test
    public void shouldNotIncludeNullIfNonAbsentConfigured() {
        assertThat(includeField(NULL, NON_ABSENT), is(false));
    }

    @Test
    public void shouldIncludeNonNullIfNonAbsentConfigured() {
        assertThat(includeField(TRUE, NON_NULL), is(true));
    }

    @Test
    public void shouldIncludeNonNullIfAlwaysConfigured() {
        assertThat(includeField(TRUE, ALWAYS), is(true));
    }

    @Test
    public void shouldIncludeNonNullIfNonDefaultConfigured() {
        assertThat(includeField(TRUE, NON_DEFAULT), is(true));
    }

    @Test
    public void shouldIncludeNonNullIfUseDefaultsConfigured() {
        assertThat(includeField(TRUE, USE_DEFAULTS), is(true));
    }

    @Test
    public void shouldNotIncludeNullIfNonEmptyConfigured() {
        assertThat(includeField(NULL, NON_EMPTY), is(false));
    }

    @Test
    public void shouldNotIncludeEmptyArrayIfNonEmptyConfigured() {
        assertThat(includeField(getJsonBuilderFactory().createArrayBuilder().build(), NON_EMPTY), is(false));
    }

    @Test
    public void shouldIncludeNonEmptyArrayIfNonEmptyConfigured() {
        assertThat(includeField(getJsonBuilderFactory().createArrayBuilder().add(TRUE).build(), NON_EMPTY), is(true));
    }

    @Test
    public void shouldNotIncludeEmptyStringIfNonEmptyConfigured() {
        assertThat(includeField(getJsonBuilderFactory().createObjectBuilder().add("test", "").build().getJsonString("test"), NON_EMPTY), is(false));
    }

    @Test
    public void shouldIncludeNonEmptyStringIfNonEmptyConfigured() {
        assertThat(includeField(getJsonBuilderFactory().createObjectBuilder().add("test", "blah").build().getJsonString("test"), NON_EMPTY), is(true));
    }

    @Test
    public void shouldIncludeNumberIfNonEmptyConfigured() {
        assertThat(includeField(getJsonBuilderFactory().createObjectBuilder().add("test", 0L).build().getJsonNumber("test"), NON_EMPTY), is(true));
    }
}
