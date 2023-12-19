package uk.gov.justice.services.fileservice.repository;

import static java.io.File.createTempFile;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.fromSqlTimestamp;

import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.utils.test.FileStoreTestDataSourceProvider;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContentJdbcRepositoryIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    @Mock
    private UtcClock clock;

    @InjectMocks
    private ContentJdbcRepository contentJdbcRepository;

    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();

    private Connection connection;

    @BeforeEach
    public void setupDatabase() throws Exception {

        connection = new FileStoreTestDataSourceProvider().getDatasource().getConnection();

        liquibaseDatabaseBootstrapper.bootstrap(
                LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                connection);
    }

    @AfterEach
    public void closeConnection() throws SQLException {

        if(connection != null) {
            connection.close();
        }
    }

    @Test
    public void shouldStoreAndRetrieveFileContent() throws Exception {

        final UUID fileId = randomUUID();
        final File inputFile = getFile("/for-testing-file-store.jpg");

        final InputStream content = new FileInputStream(inputFile);
        contentJdbcRepository.insert(fileId, content, connection);
        content.close();

        final FileContent fileContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find file contents"));

        final File outputFile = createTempFile("/created-for-testing-file-store-please-delete-me_1", "jpg");
        outputFile.deleteOnExit();

        final InputStream contentStream = fileContent.getContent();
        copy(contentStream, outputFile.toPath(), REPLACE_EXISTING);

        contentStream.close();

        assertThat(outputFile.exists(), is(true));
        assertThat(outputFile.length(), is(greaterThan(0L)));
        assertThat(outputFile.length(), is(inputFile.length()));

        connection.commit();
    }

    @Test
    public void shouldMarkFileContentAsDeletedWithDeletedDateOnDelete() throws Exception {

        final UUID fileId = randomUUID();
        final File inputFile = getFile("/for-testing-file-store.jpg");
        final ZonedDateTime now = new UtcClock().now();

        when(clock.now()).thenReturn(now);

        final InputStream content = new FileInputStream(inputFile);
        contentJdbcRepository.insert(fileId, content, connection);

        content.close();

        final FileContent fileContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find file content"));

        fileContent.close();

        contentJdbcRepository.delete(fileId, connection);

        final Optional<FileContent> deletedFileContent = contentJdbcRepository
                .findByFileId(fileId, connection);

        assertThat(deletedFileContent.isPresent(), is(false));

        try(final PreparedStatement preparedStatement = connection.prepareStatement("SELECT deleted, deleted_at from content where file_id = ?")) {
            preparedStatement.setObject(1, fileId);

            try(final ResultSet resultSet = preparedStatement.executeQuery()) {
                assertThat(resultSet.next(), is(true ));

                final boolean deleted = resultSet.getBoolean(1);
                final ZonedDateTime dateDeleted = fromSqlTimestamp(resultSet.getTimestamp(2));

                assertThat(deleted, is(true));
                assertThat(dateDeleted, is(now));
            }
        }

        connection.commit();
    }

    public File getFile(final String fileName) throws URISyntaxException {
        final URL url = getClass().getResource(fileName);
        return new File(url.toURI());
    }
}
