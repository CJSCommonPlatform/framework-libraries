package uk.gov.justice.services.fileservice.it;

import static java.io.File.createTempFile;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import uk.gov.justice.services.cdi.LoggerProducer;
import uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProvider;
import uk.gov.justice.services.common.configuration.ValueProducer;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.FileRetriever;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.FileStorer;
import uk.gov.justice.services.fileservice.client.FileService;
import uk.gov.justice.services.fileservice.client.FileStorePurgeConfiguration;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.repository.FileStore;
import uk.gov.justice.services.fileservice.repository.FileStoreJdbcRepository;
import uk.gov.justice.services.fileservice.repository.MetadataUpdater;
import uk.gov.justice.services.fileservice.utils.test.FileStoreTestDataSourceProvider;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;
import uk.gov.justice.services.utilities.file.ContentTypeDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;
import org.apache.openejb.jee.Application;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

@RunWithApplicationComposer
public class FileServiceIT {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";

    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();
    private final ClasspathFileResource classpathFileResource = new ClasspathFileResource();

    private DataSource dataSource = new FileStoreTestDataSourceProvider().getDatasource();

    @Inject
    private FileService fileService;

    @Module
    @Classes(cdi = true, value = {

            FileRetriever.class,
            FileServiceException.class,
            FileStorer.class,
            FileReference.class,

            InitialContextFactory.class,

            FileService.class,

            FileStoreTestDataSourceProvider.class,

            FileStoreJdbcRepository.class,
            FileStore.class,
            MetadataUpdater.class,
            UtcClock.class,
            LogFactory.class,

            MetadataUpdater.class,
            ContentTypeDetector.class,
            LoggerProducer.class,
            Logger.class,
            UtcClock.class,
            FileStorePurgeConfiguration.class,
            ValueProducer.class,
            JndiBasedServiceContextNameProvider.class
    })
    public WebApp war() {
        return new WebApp()
                .contextRoot("core-test")
                .addServlet("TestApp", Application.class.getName());
    }

    @BeforeEach
    public void initDatabase() throws Exception {
        liquibaseDatabaseBootstrapper.bootstrap(
                LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                dataSource.getConnection());
    }

    @Test
    public void shouldSuccessfullyStoreABinaryFile() throws Exception {

        assertThat(fileService, is(notNullValue()));

        final JsonObject metadata = createObjectBuilder()
                .add("metadataField", "metadataValue")
                .build();

        final File inputFile = classpathFileResource.getFileFromClasspath("/for-testing-file-store.jpg");
        final FileInputStream inputStream = new FileInputStream(inputFile);

        final UUID fileId = fileService.store(metadata, inputStream);

        inputStream.close();

        final File outputFile = createTempFile("created-for-testing-file-store-please-delete-me_2", "jpg");
        outputFile.deleteOnExit();

        try (final FileReference fileReference = fileService
                .retrieve(fileId)
                .orElseThrow(() -> new AssertionError("Failed to get FileReference from File Store"))) {

            final JsonObject retrievedMetadata = fileReference.getMetadata();
            assertThat(retrievedMetadata.getString("metadataField"), is("metadataValue"));
            assertThat(retrievedMetadata.getString("mediaType"), is("image/jpeg"));
            assertThat(retrievedMetadata.getString("createdAt"), is(notNullValue()  ));

            final InputStream contentStream = fileReference.getContentStream();

            copy(contentStream, outputFile.toPath(), REPLACE_EXISTING);
        }

        assertThat(outputFile.exists(), is(true));
        assertThat(outputFile.length(), is(greaterThan(0L)));
        assertThat(outputFile.length(), is(inputFile.length()));
    }
}
