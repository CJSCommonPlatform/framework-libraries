package uk.gov.justice.services.test.utils.core.rest;

import static org.apache.http.entity.ContentType.create;
import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Simple client for making multipart file uploads. NB: none of these methods will close the file
 * InputStream
 *
 * To Use:
 *
 * <pre>
 *     <blockquote>
 *
 *         final String url = ...
 *         final String filename = ...
 *         final String filePartName = ...
 *
 *         final File file = ...
 *
 *         try(final InputStream inputStream = new FileInputStream(file)) {
 *
 *              final MultipartRestClient multipartRestClient = new MultipartRestClient();
 *
 *              final FilePart filePart = new FilePart(
 *                  filePartName,
 *                  filename,
 *                  inputStream);
 *
 *              final HttpResponse response = multipartRestClient.post(filePart, url);
 *         }
 *
 *     </blockquote>
 * </pre>
 */
public class MultipartRestClient {

    /**
     * Uploads the file part
     *
     * @param filePart the name, InputStream etc. of the file to be uploaded
     * @param url the url to upload to
     *
     * @return the {@link HttpResponse} of the HTTP POST
     * 
     * @throws IOException if the POST fails
     */
    public HttpResponse post(final FilePart filePart, final String url) throws IOException {
        return post(filePart, url, new HashMap<>());
    }

    /**
     * Uploads the file part
     *
     * @param filePart the name, InputStream etc. of the file to be uploaded
     * @param url the url to upload to
     * @param headers a {@link Map} of the HTTP headers
     *
     * @return the {@link HttpResponse} of the HTTP POST
     *
     * @throws IOException if the POST fails
     */
    public HttpResponse post(final FilePart filePart, final String url, final Map<String, String> headers) throws IOException {

        final InputStreamBody inputStreamBody = getInputStreamBody(filePart);

        final HttpEntity httpEntity = MultipartEntityBuilder
                .create()
                .setMode(BROWSER_COMPATIBLE)
                .addPart(filePart.getFilePartName(), inputStreamBody)
                .build();

        final HttpPost httpPost = new HttpPost(url);
        headers.forEach(httpPost::addHeader);
        httpPost.setEntity(httpEntity);

        return HttpClientBuilder
                .create()
                .build()
                .execute(httpPost);
    }

    private InputStreamBody getInputStreamBody(final FilePart filePart) {

        final Optional<MediaType> mediaType = filePart.getMediaType();

        if(mediaType.isPresent() ) {
            return new InputStreamBody(
                    filePart.getInputStream(),
                    asContentType(mediaType),
                    filePart.getFilename());
        }

        return new InputStreamBody(
                filePart.getInputStream(),
                filePart.getFilename());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private ContentType asContentType(final Optional<MediaType> mediaType) {
        return create(mediaType.get().getType() + "/" + mediaType.get().getSubtype());
    }
}
