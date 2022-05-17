package uk.gov.justice.services.fileservice.repository;

import static java.util.UUID.randomUUID;
import static javax.json.Json.createReader;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.fileservice.utils.test.FileStoreDatabaseBootstrapper;
import uk.gov.justice.services.fileservice.utils.test.FileStoreTestDataSourceProvider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetadataJdbcRepositoryIT {

    private final MetadataJdbcRepository metadataJdbcRepository = new MetadataJdbcRepository();
    private final ContentJdbcRepository contentJdbcRepository = new ContentJdbcRepository();

    private Connection connection;

    @Before
    public void setupDatabase() throws Exception {

        connection = new FileStoreTestDataSourceProvider().getDatasource().getConnection();
        new FileStoreDatabaseBootstrapper().initDatabase();
    }

    @After
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
    }

    @Test
    public void shouldDeleteMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final String json = "{\"some\": \"json\"}";
        final InputStream content = new ByteArrayInputStream("some file or other".getBytes());
        final JsonObject metadata = toJsonObject(json);

        contentJdbcRepository.insert(fileId, content, connection);
        metadataJdbcRepository.insert(fileId, metadata, connection);

        assertThat(metadataJdbcRepository.findByFileId(fileId, connection).isPresent(), is(true));

        metadataJdbcRepository.delete(fileId, connection);

        assertThat(metadataJdbcRepository.findByFileId(fileId, connection).isPresent(), is(false));
    }

    private JsonObject toJsonObject(final String json) {
        return createReader(new StringReader(json)).readObject();
    }
}
