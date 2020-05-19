package uk.gov.justice.services.core.json;

import uk.gov.justice.services.core.mapping.MediaType;

import java.util.Optional;

public interface JsonSchemaValidator {

    void validate(final String payload, final String actionName);

    void validate(final String payload, final String actionName, final Optional<MediaType> mediaType);
}
