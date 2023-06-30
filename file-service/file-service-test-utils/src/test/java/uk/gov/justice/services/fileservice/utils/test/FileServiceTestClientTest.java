package uk.gov.justice.services.fileservice.utils.test;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.io.File.createTempFile;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FileServiceTestClientTest {

    private static final String LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML = "liquibase/file-service-liquibase-db-changelog.xml";
    private static FileStoreTestDataSourceProvider filestoreTestDataSourceProvider = new FileStoreTestDataSourceProvider();

    private final ClasspathFileResource classpathFileResource = new ClasspathFileResource();
    private final FileServiceTestClient fileServiceTestClient = new FileServiceTestClient();

    @BeforeAll
    public static void boostrapFileStoreDatabase() throws Exception {

        try(final Connection connection = filestoreTestDataSourceProvider.getDatasource().getConnection()) {
            new LiquibaseDatabaseBootstrapper().bootstrap(
                    LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                    connection);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldStoreAndGetAnImage() throws Exception {

        final String fileName = "some.jpg";
        final String mediaType = "image/jpeg";
        final File inputFile = classpathFileResource.getFileFromClasspath("/for-testing.jpg");

        final InputStream contentStream = new FileInputStream(inputFile);

        try(final Connection connection = filestoreTestDataSourceProvider.getDatasource().getConnection()) {

            final UUID fileId = fileServiceTestClient.create(fileName, mediaType, contentStream, connection);

            contentStream.close();

            final Optional<FileReference> fileReferenceOptional = fileServiceTestClient.read(fileId, connection);

            assertThat(fileReferenceOptional.isPresent(), is(true));

            final FileReference fileReference = fileReferenceOptional.get();

            assertThat(fileReference.getFileId(), is(fileId));
            assertThat(fileReference.isDeleted(), is(false));

            final String metadataJson = fileReference.getMetadata().toString();

            with(metadataJson)
                    .assertThat("$.fileName", is(fileName))
                    .assertThat("$.mediaType", is(mediaType))
            ;

            try (final InputStream inputStream = fileReference.getContentStream()) {
                final File outputFile = createTempFile("for-testing-please.delete", "jpg");
                outputFile.deleteOnExit();

                copy(inputStream, outputFile.toPath(), REPLACE_EXISTING);

                assertThat(outputFile.exists(), is(true));
                assertThat(outputFile.length(), is(greaterThan(0L)));
                assertThat(outputFile.length(), is(inputFile.length()));
            }
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldStoreAndGetAnImageWithTheSpecifiedId() throws Exception {

        final String fileName = "some.jpg";
        final String mediaType = "image/jpeg";
        final File inputFile = classpathFileResource.getFileFromClasspath("/for-testing.jpg");

        final InputStream contentStream = new FileInputStream(inputFile);

        final UUID fileId = randomUUID();

        try(final Connection connection = filestoreTestDataSourceProvider.getDatasource().getConnection()) {
            assertThat(fileServiceTestClient.create(fileId, fileName, mediaType, contentStream, connection), is(fileId));

            contentStream.close();

            final Optional<FileReference> fileReferenceOptional = fileServiceTestClient.read(fileId, connection);

            assertThat(fileReferenceOptional.isPresent(), is(true));

            final FileReference fileReference = fileReferenceOptional.get();

            assertThat(fileReference.getFileId(), is(fileId));
            assertThat(fileReference.isDeleted(), is(false));


            final String metadataJson = fileReference.getMetadata().toString();

            with(metadataJson)
                    .assertThat("$.fileName", is(fileName))
                    .assertThat("$.mediaType", is(mediaType))
            ;

            try (final InputStream inputStream = fileReference.getContentStream()) {
                final File outputFile = createTempFile("for-testing-please.delete", "jpg");
                outputFile.deleteOnExit();

                copy(inputStream, outputFile.toPath(), REPLACE_EXISTING);

                assertThat(outputFile.exists(), is(true));
                assertThat(outputFile.length(), is(greaterThan(0L)));
                assertThat(outputFile.length(), is(inputFile.length()));
            }
        }
    }
}
