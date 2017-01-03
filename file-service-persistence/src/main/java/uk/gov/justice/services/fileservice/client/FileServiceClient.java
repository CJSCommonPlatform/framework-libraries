package uk.gov.justice.services.fileservice.client;


import uk.gov.justice.services.fileservice.common.StorableFile;

import java.util.Optional;
import java.util.UUID;

public interface FileServiceClient {

    void store(final StorableFile storableFile);
    Optional<StorableFile> find(final UUID fileId);
}
