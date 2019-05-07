package uk.gov.justice.json.jolt;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.justice.json.jolt.JsonHelper.readJson;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.converter.exception.ConverterException;

import java.io.FileNotFoundException;

import javax.json.JsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

public class JoltTransformerTest {

    private final JoltTransformer joltTransformer = new JoltTransformer();

    @Before
    public void setUp() {
        final StringToJsonObjectConverter stringToJsonObjectConverter = new StringToJsonObjectConverter();
        setField(joltTransformer, "stringToJsonObjectConverter", stringToJsonObjectConverter);
    }

    @Test
    public void shouldValidateAndThrowExceptionIfSpecDoesNotContainAnyOperations() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-empty.json");

        try {
            final String operationsString = specJson.toString();
            joltTransformer.transformWithJolt(operationsString, inputJson);
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
            joltTransformer.transformWithJolt(specJson.toString(), inputJson);
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
            joltTransformer.transformWithJolt(specJson.toString(), inputJson);
            fail("Input Json is not empty");
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Input JSON is empty"));
        }
    }

    @Test
    public void shouldTransformProvidedInputJson() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec.json");

        joltTransformer.transformWithJolt(specJson.toString(), inputJson);
    }

    @Test
    public void shouldTransformProvidedInputJsonWithSpecContainingAConverterClass() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-with-converter.json");

        final JsonObject transformedJson = joltTransformer.transformWithJolt(specJson.toString(), inputJson);
        assertThat(transformedJson.getString("label"), is("label"));
    }

    @Test(expected = ConverterException.class)
    public void shouldThrowExceptionOnNullResult() throws JsonProcessingException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-invalid.json");

        joltTransformer.transformWithJolt(specJson.toString(), inputJson);
    }
}