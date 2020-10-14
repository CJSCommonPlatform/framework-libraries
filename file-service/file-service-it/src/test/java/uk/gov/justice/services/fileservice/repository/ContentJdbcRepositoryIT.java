package uk.gov.justice.services.fileservice.repository;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.test.utils.core.jdbc.JdbcConnectionProvider;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ContentJdbcRepositoryIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    private static final String URL = "jdbc:h2:mem:test;MV_STORE=FALSE;MVCC=FALSE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa";
    private static final String DRIVER_CLASS = org.h2.Driver.class.getName();

    private final JdbcConnectionProvider connectionProvider = new JdbcConnectionProvider();
    private final ContentJdbcRepository contentJdbcRepository = new ContentJdbcRepository();
    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();

    private Connection connection;

    @Before
    public void setupDatabase() throws Exception {

        connection = connectionProvider.getConnection(URL, USERNAME, PASSWORD, DRIVER_CLASS);

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
        final String contentString = "some-content-or-other";
        final InputStream content = new ByteArrayInputStream(contentString.getBytes());
        contentJdbcRepository.insert(fileId, content, connection);

        final FileContent fileContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find file contents"));

        assertThat(IOUtils.toString(fileContent.getContent()), is(contentString));
        assertThat(fileContent.isDeleted(), is(false));
    }

    @Test
    public void shouldDeleteFileContent() throws Exception {

        final UUID fileId = randomUUID();
        final String contentString = "some-content-or-other";
        final InputStream content = new ByteArrayInputStream(contentString.getBytes());
        contentJdbcRepository.insert(fileId, content, connection);

        final FileContent fileContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find file"));

        assertThat(fileContent.isDeleted(), is(false));

        contentJdbcRepository.delete(fileId, connection);

        final FileContent foundContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find file"));

        assertThat(foundContent.isDeleted(), is(true));
        assertThat(IOUtils.toString(foundContent.getContent()), is(contentString));
    }
}
