package uk.gov.justice.services.fileservice.it.helpers;

import uk.gov.justice.services.fileservice.repository.MetadataJdbcRepository;

import java.sql.Connection;
import java.util.UUID;

import javax.json.JsonObject;

public class FailingMetadataJdbcRepository extends MetadataJdbcRepository {

    @Override
    public void insert(final UUID fileId, final JsonObject metadata, final Connection connection) {
        throw new MakeMetadataRepositoryInsertFailException("Deliberately failing insert into metadata for testing transactions");
    }
}
