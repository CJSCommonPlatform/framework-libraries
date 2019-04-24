package uk.gov.justice.json.jolt;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.justice.json.jolt.JsonHelper.readJson;

import java.io.IOException;
import java.util.List;

import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonAgainstSchemaValidatorTest {

    @Spy
    private ValidationExceptionHandler validationHandler;

    @InjectMocks
    JsonAgainstSchemaValidator jsonAgainstSchemaValidator;

    @Test
    public void shouldThrowTransformationExceptionWhenFileNotFound() throws IOException {

        final String schemaFileName = "case_details_schema1.json";

        try {
            final JsonObject inputJson = readJson("/case_details.json");
            jsonAgainstSchemaValidator.validateAgainstSchema(schemaFileName, inputJson.toString());
            fail("Should fail on invalid files!");
        } catch (final TransformationException e) {
            assertThat(e.getMessage(), is("Error validating payload against schema, File not readable or available"));
        }
    }

    @Test
    public void shouldFailToValidateJsonAgainstSchemaWhenThereIsJustOneValidationException() throws IOException {

        final String schemaFileName = "case_details_schema.json";
        final JsonObject inputJson = readJson("/case_details_1.json");

        final List<String> exceptionList = jsonAgainstSchemaValidator.validateAgainstSchema(schemaFileName, inputJson.toString());

        assertThat(exceptionList.size(), is(1));
        assertThat(exceptionList.get(0), is("#/parties/0: required key [organisationName] not found"));
    }

    @Test
    public void shouldFailToValidateJsonAgainstSchemaWhenThereIsMoreThanOneValidationException() throws IOException {

        final String schemaFileName = "case_details_schema.json";
        final JsonObject inputJson = readJson("/case_details.json");

        final List<String> exceptionList = jsonAgainstSchemaValidator.validateAgainstSchema(schemaFileName, inputJson.toString());

        assertThat(exceptionList.size(), is(2));
        assertThat(exceptionList.get(0), is("#/parties/0: required key [organisationName] not found"));
        assertThat(exceptionList.get(1), is("#/parties/1: required key [_party_type] not found"));
    }

    @Test
    public void shouldPassValidationOfJsonAgainstSchema() throws IOException {

        final String schemaFileName = "case_details_schema.json";
        final JsonObject inputJson = readJson("/case_details_correct.json");

        final List<String> exceptionList = jsonAgainstSchemaValidator.validateAgainstSchema(schemaFileName, inputJson.toString());

        assertThat(exceptionList.size(), is(0));
    }
}