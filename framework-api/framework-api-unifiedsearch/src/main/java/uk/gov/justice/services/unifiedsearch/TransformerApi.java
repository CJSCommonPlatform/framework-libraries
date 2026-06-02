package uk.gov.justice.services.unifiedsearch;

import jakarta.json.JsonObject;

public interface TransformerApi {

    JsonObject transformWithJolt(final String joltOperations, final JsonObject inputJson);
}
