package uk.gov.justice.json.jolt;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import com.google.common.io.Resources;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonAgainstSchemaValidator {

    @Inject
    private ValidationExceptionHandler validationHandler;

    public List<String> validateAgainstSchema(final String schemaFileName,
                                              final String jsonString) throws TransformationException {

        try (final InputStream inputStream = Resources.getResource(schemaFileName).openStream()) {

            final JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            final Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(jsonString));

        } catch (final ValidationException validationException) {
            return validationHandler.handleValidationException(validationException);
        } catch (final IOException | IllegalArgumentException e) {
            throw new TransformationException("Error validating payload against schema, File not readable or available", e);
        }
        return emptyList();
    }
}
