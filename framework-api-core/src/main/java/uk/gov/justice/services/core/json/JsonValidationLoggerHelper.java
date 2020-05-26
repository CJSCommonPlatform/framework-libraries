package uk.gov.justice.services.core.json;

import javax.json.JsonObject;

/**
 * Handles message logging for Json schema validation.
 */
public interface JsonValidationLoggerHelper {

    /**
     * Converts {@link JsonSchemaValidationException} to a readable log message
     * @param jsonSchemaValidatonException
     * @return tlog message as String
     */
    String toValidationTrace(final JsonSchemaValidationException jsonSchemaValidatonException);

    /**
     * Converts {@link JsonSchemaValidationException} to a {@link JsonObject}
     * @param jsonSchemaValidatonException
     * @return log message in {@ JsonObject} format
     */
    JsonObject toJsonObject(final JsonSchemaValidationException jsonSchemaValidatonException);

}