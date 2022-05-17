package uk.gov.justice.services.fileservice.utils.test;

import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.sql.Connection;

import com.google.common.annotations.VisibleForTesting;

public class FileStoreDatabaseBootstrapper {

    static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase-files/file-service-liquibase-db-changelog.xml";

    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper;
    private final FileStoreTestDataSourceProvider fileStoreTestDataSourceProvider;

    public FileStoreDatabaseBootstrapper() {
        this(new LiquibaseDatabaseBootstrapper(), new FileStoreTestDataSourceProvider());
    }

    @VisibleForTesting
    FileStoreDatabaseBootstrapper(
            final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper,
            final FileStoreTestDataSourceProvider fileStoreTestDataSourceProvider) {
        this.liquibaseDatabaseBootstrapper = liquibaseDatabaseBootstrapper;
        this.fileStoreTestDataSourceProvider = fileStoreTestDataSourceProvider;
    }

    public void initDatabase() throws Exception {
        try(final Connection connection = fileStoreTestDataSourceProvider.getDatasource().getConnection()) {
            liquibaseDatabaseBootstrapper.bootstrap(
                    LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                    connection);
        }
    }
}
