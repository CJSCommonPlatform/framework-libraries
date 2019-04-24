package uk.gov.justice.json.jolt;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.justice.json.jolt.JsonHelper.readJson;

import java.io.FileNotFoundException;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.Test;

public class JoltTransformerTest {

    private final JoltTransformer joltTransformer = new JoltTransformer();

    @Test
    public void shouldValidateAndThrowExceptionIfSpecDoesNotContainAnyOperations() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-empty.json");

        try {
            final JsonArray operations = specJson.getJsonArray("operations");
            joltTransformer.transformWithJolt(operations, inputJson);
            fail("Input contains operations, expecting no operations!");
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Input specification does not contain any operations"));
        }
    }

    @Test
    public void shouldValidateAndThrowExceptionIfEmptySpecJson() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-empty.json");

        try {
            final JsonArray operations = specJson.getJsonArray("operations");
            joltTransformer.transformWithJolt(operations, inputJson);
            fail("Input specification is not empty, expecting empty specification!");
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Input specification is empty"));
        }
    }

    @Test
    public void shouldValidateAndThrowExceptionIfEmptyInputJson() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase-empty.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec.json");

        try {
            final JsonArray operations = specJson.getJsonArray("operations");
            joltTransformer.transformWithJolt(operations, inputJson);
            fail("Input Json is not empty");
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Input JSON is empty"));
        }
    }

    @Test
    public void shouldTransformProvidedInputJson() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec.json");

        final JsonArray operations = specJson.getJsonArray("operations");
        joltTransformer.transformWithJolt(operations, inputJson);
    }

    @Test
    public void shouldTransformProvidedInputJsonWithSpecContainingAConverterClass() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-with-converter.json");
        final JsonArray operations = specJson.getJsonArray("operations");

        final JsonObject transformedJson = joltTransformer.transformWithJolt(operations, inputJson);
        assertThat(transformedJson.getString("label"), is("label"));
    }
}