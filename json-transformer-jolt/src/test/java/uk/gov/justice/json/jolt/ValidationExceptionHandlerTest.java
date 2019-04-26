package uk.gov.justice.json.jolt;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.NullSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.junit.Test;

public class ValidationExceptionHandlerTest {
    @Test
    public void shouldFailToValidateJsonAgainstSchemaWhenThereIsJustOneValidationException() throws IOException {
        final String message = "required key [organisationName] not found";
        final String keyword = "required";
        final String schemaLocation = "#/parties/0";

        final ValidationExceptionHandler validationHandler = new ValidationExceptionHandler();
        final NullSchema.Builder builder = new NullSchema.Builder();
        final Schema violatedSchema = new NullSchema(builder);
        final ValidationException validationException = new ValidationException(violatedSchema, message, keyword, schemaLocation);

        final List<String> exceptionList = validationHandler.handleValidationException(validationException);

        assertThat(exceptionList.size(), is(1));
        assertThat(exceptionList.get(0), is("#: required key [organisationName] not found"));
    }

    @Test
    public void shouldFailToValidateJsonAgainstSchemaWhenThereIsMoreThanOneValidationException() throws IOException {

        final String message1 = "required key [organisationName] not found";
        final String message2 = "required key [_party_type] not found";
        final String keyword = "required";
        final String schemaLocation = "#/parties/0";

        final ValidationExceptionHandler validationHandler = new ValidationExceptionHandler();
        final NullSchema.Builder builder = new NullSchema.Builder();

        final Schema violatedSchema = new NullSchema(builder);

        final ValidationException validationException1 = new ValidationException(violatedSchema, message1, keyword, schemaLocation);
        final ValidationException validationException2 = new ValidationException(violatedSchema, message2, keyword, schemaLocation);

        final List<ValidationException> causingExceptions = new ArrayList<>();
        causingExceptions.add(validationException1);
        causingExceptions.add(validationException2);

        final ValidationException validationExceptions = new ValidationException(violatedSchema, message1, causingExceptions);

        final List<String> exceptionList = validationHandler.handleValidationException(validationExceptions);

        assertThat(exceptionList.size(), is(2));
        assertThat(exceptionList.get(0), is("#: required key [organisationName] not found"));
        assertThat(exceptionList.get(1), is("#: required key [_party_type] not found"));
    }
}