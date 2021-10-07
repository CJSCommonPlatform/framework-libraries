package uk.gov.justice.services.core.json;

import javax.json.JsonObject;

/**
 * Handles message logging for Json schema validation.
 */
public interface JsonValidationLoggerHelper {

    /**
     * Converts {@link JsonSchemaValidationException} to a readable log message
     * @param jsonSchemaValidationException The Exception to log
     * @return log message as String
     */
    String toValidationTrace(final JsonSchemaValidationException jsonSchemaValidationException);

    /**
     * Converts {@link JsonSchemaValidationException} to a {@link JsonObject}
     * @param jsonSchemaValidationException The Exception to log
     * @return log message in {@link JsonObject} format
     */
    JsonObject toJsonObject(final JsonSchemaValidationException jsonSchemaValidationException);

}