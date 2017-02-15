package uk.gov.justice.services.fileservice.repository;

import static uk.gov.justice.services.messaging.JsonObjects.createObjectBuilder;

import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.util.UtcClock;

import javax.inject.Inject;
import javax.json.JsonObject;

public class MetadataUpdater {

    @Inject
    UtcClock utcClock;

    public JsonObject addCreatedTime(final JsonObject metadata) {
        return createObjectBuilder(metadata)
                .add("createdAt", ZonedDateTimes.toString(utcClock.now()))
                .build();
    }
}
