package uk.gov.justice.services.fileservice.utils.test;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static javax.json.Json.createObjectBuilder;

import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.repository.ContentJdbcRepository;
import uk.gov.justice.services.fileservice.repository.FileContent;
import uk.gov.justice.services.fileservice.repository.MetadataJdbcRepository;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

/**
 * Test client for testing input/output to the File Service
 *
 * To Use:
 *  <pre>
 *      <blockquote>
 *          final String fileName = "some.jpg";
 *          final String mediaType = "image/jpeg";
 *          final InputStream contentStream ...
 *
 *          final FileServiceTestClient fileServiceTestClient = new FileServiceTestClient(DatabaseDialect.POSTGRES);
 *
 *          final UUID fileId = fileServiceTestClient.create(fileName, mediaType, contentStream, connection);
 *          contentStream.close();
 *
 *          ...
 *
 *          fileReference = fileServiceTestClient.read(fileId, connection);
 *          
 *      </blockquote>
 *  </pre>
 */
public class FileServiceTestClient {

    private final MetadataJdbcRepository metadataJdbcRepository;
    private final ContentJdbcRepository contentJdbcRepository;
    private final UtcClock utcClock;

    /**
     * Creates a test file service client
     *
     * @param databaseDialect the database dialect to use. See {@link DatabaseDialect}
     */
    public FileServiceTestClient() {
        metadataJdbcRepository = new MetadataJdbcRepository();
        contentJdbcRepository = new ContentJdbcRepository();
        utcClock = new UtcClock();
    }

    /**
     * Puts a file in the database.
     *
     * NB: the {@link Connection} is not closed by the client.
     *
     * @param fileName the name of the file. Will appear in the metadata json
     * @param mediaType the media type of the file. Will appear in the metadata json
     * @param contentStream an {@link InputStream} from the file to store
     * @param connection a live connection to the database.
     * @return the newly created file id
     * @throws FileServiceException if saving the file fails
     */
    public UUID create(
            final String fileName,
            final String mediaType,
            final InputStream contentStream,
            final Connection connection) throws FileServiceException {

        return create(randomUUID(), fileName, mediaType, contentStream, connection);
    }

    /**
     * Puts a file in the database with the specified id.
     *
     * NB: the {@link Connection} is not closed by the client.
     *
     * @param fileId the file id for the file to be created
     * @param fileName the name of the file. Will appear in the metadata json
     * @param mediaType the media type of the file. Will appear in the metadata json
     * @param contentStream an {@link InputStream} from the file to store
     * @param connection a live connection to the database.
     * @return the file id
     * @throws FileServiceException if saving the file fails
     */
    public UUID create(
            final UUID fileId,
            final String fileName,
            final String mediaType,
            final InputStream contentStream,
            final Connection connection) throws FileServiceException {

        final JsonObject metadata = createObjectBuilder()
                .add("fileName", fileName)
                .add("mediaType", mediaType)
                .add("createdAt", ZonedDateTimes.toString(utcClock.now()))
                .build();

        contentJdbcRepository.insert(fileId, contentStream, connection);
        metadataJdbcRepository.insert(fileId, metadata, connection);

        return fileId;
    }

    /**
     * Reads a previously stored file from the database
     *
     * NB: the {@link Connection} is not closed by the client.
     *
     * @param fileId the id of the stored file
     * @param connection a live connection to the database.
     * @return an {@link Optional} containing the {@link FileReference}
     * @throws FileServiceException if retrieving the file fails
     */
    public Optional<FileReference> read(final UUID fileId, final Connection connection) throws FileServiceException {

        final Optional<JsonObject> metadata = metadataJdbcRepository.findByFileId(fileId, connection);
        final Optional<FileContent> content = contentJdbcRepository.findByFileId(fileId, connection);

        if (metadata.isPresent() && content.isPresent()) {

            final InputStream inputStream = content.get().getContent();

            final FileReference fileReference = new FileReference(
                    fileId,
                    metadata.get(),
                    inputStream,
                    false);

            return of(fileReference);
        }

        return empty();
    }
}
