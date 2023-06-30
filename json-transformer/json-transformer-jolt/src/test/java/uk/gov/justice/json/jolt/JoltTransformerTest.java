package uk.gov.justice.json.jolt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.json.jolt.JsonHelper.readJson;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.converter.exception.ConverterException;

import java.io.FileNotFoundException;

import javax.json.JsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JoltTransformerTest {

    private final JoltTransformer joltTransformer = new JoltTransformer();

    @BeforeEach
    public void setUp() {
        final StringToJsonObjectConverter stringToJsonObjectConverter = new StringToJsonObjectConverter();
        setField(joltTransformer, "stringToJsonObjectConverter", stringToJsonObjectConverter);
    }

    @Test
    public void shouldValidateAndThrowExceptionIfSpecDoesNotContainAnyOperations() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-empty.json");

        final String operationsString = specJson.toString();
        final IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                () -> joltTransformer.transformWithJolt(operationsString, inputJson));
        assertThat(illegalArgumentException.getMessage(), is("Input specification does not contain any operations"));
    }

    @Test
    public void shouldValidateAndThrowExceptionIfEmptySpecJson() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-empty.json");

        final IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                () -> joltTransformer.transformWithJolt(specJson.toString(), inputJson));
        assertThat(illegalArgumentException.getMessage(), is("Input specification is empty"));
    }

    @Test
    public void shouldValidateAndThrowExceptionIfEmptyInputJson() throws FileNotFoundException {
        final JsonObject inputJson = readJson("/prosecutioncase-empty.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec.json");

        final IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                () -> joltTransformer.transformWithJolt(specJson.toString(), inputJson));
        assertThat(illegalArgumentException.getMessage(), is("Input JSON is empty"));
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

    @Test
    public void shouldThrowExceptionOnNullResult() throws JsonProcessingException {
        final JsonObject inputJson = readJson("/prosecutioncase.json");
        final JsonObject specJson = readJson("/prosecutioncase-to-case-spec-invalid.json");

        assertThrows(ConverterException.class, () -> joltTransformer.transformWithJolt(specJson.toString(), inputJson));
    }
}