package uk.gov.justice.services.fileservice.client;

import static java.lang.Integer.parseInt;

import uk.gov.justice.services.common.configuration.Value;

import javax.inject.Inject;

public class FileStorePurgeConfiguration {

    @Inject
    @Value(key = "filestore.purge.files.older.than.number.of.days", defaultValue = "28")
    private String purgeFilesOlderThanNumberOfDays;

    @Inject
    @Value(key = "filestore.maximum.number.of.files.to.purge", defaultValue = "100000")
    private String maximumNumberOfFilesToPurge;

    public int getPurgeFilesOlderThanNumberOfDays() {
        return parseInt(purgeFilesOlderThanNumberOfDays);
    }

    public int getMaximumNumberOfFilesToPurge() {
        return parseInt(maximumNumberOfFilesToPurge);
    }
}
