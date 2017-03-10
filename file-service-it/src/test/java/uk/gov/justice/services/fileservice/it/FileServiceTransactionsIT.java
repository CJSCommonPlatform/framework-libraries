package uk.gov.justice.services.fileservice.it;

import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.FileRetriever;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.FileStorer;
import uk.gov.justice.services.fileservice.client.FileService;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.it.helpers.FailingMetadataJdbcRepository;
import uk.gov.justice.services.fileservice.it.helpers.IntegrationTestDataSourceProvider;
import uk.gov.justice.services.fileservice.it.helpers.MakeMetadataRepositoryInsertFailException;
import uk.gov.justice.services.fileservice.repository.ContentJdbcRepository;
import uk.gov.justice.services.fileservice.repository.FileStore;
import uk.gov.justice.services.fileservice.repository.AnsiMetadataSqlProvider;
import uk.gov.justice.services.fileservice.repository.MetadataUpdater;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;
import uk.gov.justice.services.utilities.file.ContentTypeDetector;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.sql.DataSource;
import javax.transaction.TransactionalException;

import org.apache.openejb.jee.Application;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ApplicationComposer.class)
public class FileServiceTransactionsIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();

    @Resource(name = "openejb/Resource/DS.fileservice")
    private DataSource dataSource;

    @Inject
    FileService fileService;

    @Module
    @Classes(cdi = true, value = {

            FileRetriever.class,
            FileServiceException.class,
            FileStorer.class,
            FileReference.class,

            InitialContextFactory.class,

            FileService.class,

            IntegrationTestDataSourceProvider.class,

            AnsiMetadataSqlProvider.class,
            ContentJdbcRepository.class,
            FileStore.class,
            FailingMetadataJdbcRepository.class,
            MetadataUpdater.class,
            UtcClock.class,
            ContentTypeDetector.class
    })
    public WebApp war() {
        return new WebApp()
                .contextRoot("core-test")
                .addServlet("TestApp", Application.class.getName());
    }

    @Before
    public void initDatabase() throws Exception {
        liquibaseDatabaseBootstrapper.bootstrap(
                LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                dataSource.getConnection());
    }

    @Test
    public void shouldRollbackInsertIntoContentTableIfTheInsertIntoTheMetadataTableSubsequentlyFails() throws Exception {

        assertThat(fileService, is(notNullValue()));

        final JsonObject metadata = createObjectBuilder()
                .add("Test", "test")
                .build();
        final String fileContent = "some file content";

        try {
            fileService.store(metadata, new ByteArrayInputStream(fileContent.getBytes()));
            fail();
        } catch (final TransactionalException expected) {
            assertThat(expected.getCause(), is(instanceOf(MakeMetadataRepositoryInsertFailException.class)));
        }

        final Connection connection = dataSource.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM content");
        final ResultSet resultSet = preparedStatement.executeQuery();

        assertThat(resultSet.next(), is(false));

        connection.close();
        preparedStatement.close();
        resultSet.close();
    }
}
