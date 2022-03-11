package uk.gov.justice.services.fileservice.repository;

import static java.io.File.createTempFile;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

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
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ContentJdbcRepositoryIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    private final ContentJdbcRepository contentJdbcRepository = new ContentJdbcRepository();
    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();

    private Connection connection;

    @Before
    public void setupDatabase() throws Exception {

        connection = new FileStoreTestDataSourceProvider().getDatasource().getConnection();

        liquibaseDatabaseBootstrapper.bootstrap(
                LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                connection);
    }

    @After
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
    public void shouldDeleteFileContent() throws Exception {

        final UUID fileId = randomUUID();
        final File inputFile = getFile("/for-testing-file-store.jpg");

        final InputStream content = new FileInputStream(inputFile);
        contentJdbcRepository.insert(fileId, content, connection);

        content.close();

        final FileContent fileContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find file content"));

        fileContent.getContent().close();

        contentJdbcRepository.delete(fileId, connection);

        final Optional<FileContent> deletedFileContent = contentJdbcRepository
                .findByFileId(fileId, connection);

        assertThat(deletedFileContent.isPresent(), is(false));

        try(final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from content where file_id = ?")) {
            preparedStatement.setObject(1, fileId);

            try(final ResultSet resultSet = preparedStatement.executeQuery()) {
                assertThat(resultSet.next(), is(false));
            }
        }

        connection.commit();
    }

    public File getFile(final String fileName) throws URISyntaxException {
        final URL url = getClass().getResource(fileName);
        return new File(url.toURI());
    }
}
