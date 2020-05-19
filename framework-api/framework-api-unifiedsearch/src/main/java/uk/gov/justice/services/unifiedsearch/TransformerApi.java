package uk.gov.justice.services.unifiedsearch;

import javax.json.JsonObject;

public interface TransformerApi {

    JsonObject transformWithJolt(final String joltOperations, final JsonObject inputJson);
}
