package uk.gov.justice.services.core.unifiedsearch;

import javax.json.JsonObject;

public interface UnifiedSearchIndexer {
    void indexData(final JsonObject index);
}
