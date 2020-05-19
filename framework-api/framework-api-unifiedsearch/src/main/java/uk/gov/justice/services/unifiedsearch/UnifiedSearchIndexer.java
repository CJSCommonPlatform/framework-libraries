package uk.gov.justice.services.unifiedsearch;

import uk.gov.justice.services.messaging.Envelope;

import javax.json.JsonObject;

public interface UnifiedSearchIndexer {
    void indexData(final Envelope<JsonObject> eventWithJoltTransformedPayload);
}
