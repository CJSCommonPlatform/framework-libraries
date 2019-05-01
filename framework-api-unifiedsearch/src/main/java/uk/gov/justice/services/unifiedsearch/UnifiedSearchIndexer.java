package uk.gov.justice.services.unifiedsearch;

import javax.json.JsonObject;

public interface UnifiedSearchIndexer {
    void indexData(final JsonObject index);
}
