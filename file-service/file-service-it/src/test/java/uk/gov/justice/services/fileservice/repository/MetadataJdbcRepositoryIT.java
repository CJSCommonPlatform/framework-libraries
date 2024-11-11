package uk.gov.justice.services.fileservice.repository;

import static java.util.UUID.randomUUID;
import static javax.json.Json.createReader;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.fileservice.utils.test.FileStoreTestDataSourceProvider;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetadataJdbcRepositoryIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    private final MetadataJdbcRepository metadataJdbcRepository = new MetadataJdbcRepository();
    private final ContentJdbcRepository contentJdbcRepository = new ContentJdbcRepository();
    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();

    private Connection connection;

    @BeforeEach
    public void setupDatabase() throws Exception {

        connection = new FileStoreTestDataSourceProvider().getDatasource().getConnection();
        liquibaseDatabaseBootstrapper.bootstrap(LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML, connection);
    }

    @AfterEach
    public void closeConnection() throws SQLException {

        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void shouldStoreAndRetrieveMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final String json = "{\"some\": \"json\"}";
        final InputStream content = new ByteArrayInputStream("some file or other".getBytes());
        final JsonObject metadata = toJsonObject(json);

        contentJdbcRepository.insert(fileId, content, connection);
        metadataJdbcRepository.insert(fileId, metadata, connection);

        connection.commit();

        final JsonObject foundMetadata = metadataJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find metadata in database"));

        assertThat(foundMetadata, is(metadata));
    }

    @Test
    public void shouldUpdateMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final InputStream content = new ByteArrayInputStream("some file or other".getBytes());
        final JsonObject metadata = toJsonObject("{\"some\": \"json\"}");
        final JsonObject newMetadata = toJsonObject("{\"someOther\": \"json\"}");

        contentJdbcRepository.insert(fileId, content, connection);

        metadataJdbcRepository.insert(fileId, metadata, connection);

        final JsonObject foundMetadata = metadataJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find metadata using id " + fileId));

        assertThat(foundMetadata, is(metadata));

        metadataJdbcRepository.update(fileId, newMetadata, connection);

        final JsonObject updatedMetadata = metadataJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find metadata using id " + fileId));

        assertThat(updatedMetadata, is(newMetadata));

        connection.commit();
    }

    private JsonObject toJsonObject(final String json) {
        return createReader(new StringReader(json)).readObject();
    }
}
