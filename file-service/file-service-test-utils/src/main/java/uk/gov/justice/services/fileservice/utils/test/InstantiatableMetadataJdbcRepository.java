package uk.gov.justice.services.fileservice.utils.test;

import static uk.gov.justice.services.fileservice.utils.test.DatabaseDialect.ANSI_SQL;
import static uk.gov.justice.services.fileservice.utils.test.DatabaseDialect.POSTGRES;

import uk.gov.justice.services.fileservice.repository.AnsiMetadataSqlProvider;
import uk.gov.justice.services.fileservice.repository.MetadataJdbcRepository;
import uk.gov.justice.services.fileservice.repository.PostgresMetadataSqlProvider;

public class InstantiatableMetadataJdbcRepository extends MetadataJdbcRepository {

    public InstantiatableMetadataJdbcRepository(final DatabaseDialect databaseDialect) {

        if(databaseDialect == POSTGRES) {
            this.metadataSqlProvider = new PostgresMetadataSqlProvider();
        } else if (databaseDialect == ANSI_SQL) {
            this.metadataSqlProvider = new AnsiMetadataSqlProvider();
        }
    }
}
