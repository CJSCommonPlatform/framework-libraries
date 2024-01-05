package uk.gov.justice.services.fileservice.repository;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.io.File.createTempFile;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.UUID.randomUUID;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.fromSqlTimestamp;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.utils.test.FileStoreTestDataSourceProvider;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileStoreJdbcRepositoryIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    private final DataSourceProvider dataSourceProvider = new FileStoreTestDataSourceProvider();
    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();

    private final FileStoreJdbcRepository fileStoreJdbcRepository = new FileStoreJdbcRepository();

    @BeforeEach
    public void setupDatabase() throws Exception {
        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            liquibaseDatabaseBootstrapper.bootstrap(
                    LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                    connection);
        }
    }

    @Test
    public void shouldStoreAndRetrieveFileContent() throws Exception {

        final UUID fileId = randomUUID();
        final File inputFile = getFile("/for-testing-file-store.jpg");

        final String fileName = "croydon-by-starlight.jpg";
        final String mediaType = "image/jpeg";
        final String createdAt = new UtcClock().now().toString();

        final JsonObject metadata = createObjectBuilder()
                .add("fileId", fileId.toString())
                .add("fileName", fileName)
                .add("mediaType", mediaType)
                .add("createdAt", createdAt)
                .build();

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            final InputStream contentInputStream = new FileInputStream(inputFile);
            fileStoreJdbcRepository.insert(fileId, contentInputStream, metadata, connection);
            contentInputStream.close();

            try (final FileReference fileReference = fileStoreJdbcRepository
                    .findByFileId(fileId, connection)
                    .orElseThrow(() -> new AssertionError("Failed to find file with id '" + fileId + "'"))) {

                assertThat(fileReference.getFileId(), is(fileId));
                final String metadataJson = fileReference.getMetadata().toString();
                with(metadataJson)
                        .assertThat("$.fileId", is(fileId.toString()))
                        .assertThat("$.fileName", is(fileName))
                        .assertThat("$.mediaType", is(mediaType))
                        .assertThat("$.createdAt", is(createdAt));

                final File outputFile = createTempFile("/created-for-testing-file-store-please-delete-me", "jpg");
                outputFile.deleteOnExit();

                final InputStream contentStream = fileReference.getContentStream();
                copy(contentStream, outputFile.toPath(), REPLACE_EXISTING);

                contentStream.close();

                assertThat(outputFile.exists(), is(true));
                assertThat(outputFile.length(), is(greaterThan(0L)));
                assertThat(outputFile.length(), is(inputFile.length()));
            }
        }
    }

    @Test
    public void shouldMarkFileAsDeleted() throws Exception {

        final UUID fileId = randomUUID();
        final InputStream contentInputStream = new ByteArrayInputStream("some file content bytes".getBytes());
        final JsonObject metadata = createObjectBuilder().add("some", "json").build();
        final ZonedDateTime deletedAt = new UtcClock().now();

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            fileStoreJdbcRepository.insert(fileId, contentInputStream, metadata, connection);

            assertThat(fileStoreJdbcRepository.findByFileId(fileId, connection).isPresent(), is(true));

            fileStoreJdbcRepository.markAsDeleted(fileId, deletedAt, connection);

            assertThat(fileStoreJdbcRepository.findByFileId(fileId, connection).isPresent(), is(false));

            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT deleted_at FROM content where file_id = ?")) {

                preparedStatement.setObject(1, fileId);

                try (final ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        final ZonedDateTime zonedDateTime = fromSqlTimestamp(resultSet.getTimestamp(1));
                        assertThat(zonedDateTime, is(deletedAt));

                    } else {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void shouldPurgeAllFileOlderThanSpecifiedDate() throws Exception {

        final InputStream contentInputStream = new ByteArrayInputStream("some file content bytes".getBytes());
        final JsonObject metadata = createObjectBuilder().add("some", "json").build();
        final ZonedDateTime deletedAt = new UtcClock().now();

        final UUID fileId_1 = randomUUID();
        final UUID fileId_2 = randomUUID();
        final UUID oldFileThatShouldBePurgedId = randomUUID();
        final int maxNumberToDelete = 2;

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            fileStoreJdbcRepository.insert(fileId_1, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(fileId_2, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(oldFileThatShouldBePurgedId, contentInputStream, metadata, connection);

            fileStoreJdbcRepository.markAsDeleted(fileId_2, deletedAt, connection);
            fileStoreJdbcRepository.markAsDeleted(oldFileThatShouldBePurgedId, deletedAt.minusDays(2), connection);

            final int rowsDeleted = fileStoreJdbcRepository.purgeFilesOlderThan(deletedAt.minusDays(1), maxNumberToDelete, connection);

            assertThat(rowsDeleted, is(1));
        }
    }

    @Test
    public void shouldLimitTheNumberOfFilesToBePurged() throws Exception {

        final InputStream contentInputStream = new ByteArrayInputStream("some file content bytes".getBytes());
        final JsonObject metadata = createObjectBuilder().add("some", "json").build();
        final ZonedDateTime deletedAt = new UtcClock().now();

        final UUID fileId_1 = UUID.fromString("25c7c4e8-16a3-46be-825b-453705ca9001");
        final UUID purgeableFileId_2 = UUID.fromString("25c7c4e8-16a3-46be-825b-453705ca9002");
        final UUID purgeableFileId_3 = UUID.fromString("25c7c4e8-16a3-46be-825b-453705ca9003");
        final UUID purgeableFileId_4 = UUID.fromString("25c7c4e8-16a3-46be-825b-453705ca9004");
        final UUID purgeableFileId_5 = UUID.fromString("25c7c4e8-16a3-46be-825b-453705ca9005");
        final UUID purgeableFileId_6 = UUID.fromString("25c7c4e8-16a3-46be-825b-453705ca9006");

        final int maxNumberToDelete = 2;

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            fileStoreJdbcRepository.insert(fileId_1, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(purgeableFileId_2, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(purgeableFileId_3, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(purgeableFileId_4, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(purgeableFileId_5, contentInputStream, metadata, connection);
            fileStoreJdbcRepository.insert(purgeableFileId_6, contentInputStream, metadata, connection);

            fileStoreJdbcRepository.markAsDeleted(purgeableFileId_2, deletedAt, connection);
            fileStoreJdbcRepository.markAsDeleted(purgeableFileId_3, deletedAt.minusDays(5), connection);
            fileStoreJdbcRepository.markAsDeleted(purgeableFileId_4, deletedAt.minusDays(6), connection);
            fileStoreJdbcRepository.markAsDeleted(purgeableFileId_5, deletedAt.minusDays(7), connection);
            fileStoreJdbcRepository.markAsDeleted(purgeableFileId_6, deletedAt.minusDays(8), connection);

            final int rowsDeleted = fileStoreJdbcRepository.purgeFilesOlderThan(deletedAt.minusDays(1), maxNumberToDelete, connection);

            assertThat(rowsDeleted, is(maxNumberToDelete));
        }
    }

    @SuppressWarnings({"SameParameterValue", "ConstantConditions"})
    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = getClass().getResource(fileName);
        return new File(url.toURI());
    }
}