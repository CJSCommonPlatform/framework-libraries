package uk.gov.justice.json.api;

import javax.json.JsonArray;
import javax.json.JsonObject;

public interface TransformerApi {

    JsonObject transformWithJolt(final JsonArray joltOperations,
                                 final JsonObject inputJson);
}
