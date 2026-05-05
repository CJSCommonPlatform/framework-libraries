package uk.gov.justice.schema.catalog.generation.effective;

public class EffectiveJsonSchemaGenerationException extends RuntimeException {

    public EffectiveJsonSchemaGenerationException(final String message) {
        super(message);
    }

    public EffectiveJsonSchemaGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
