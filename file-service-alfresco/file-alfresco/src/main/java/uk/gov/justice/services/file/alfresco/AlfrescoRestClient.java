package uk.gov.justice.services.file.alfresco;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import uk.gov.justice.services.common.configuration.GlobalValue;

import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@ApplicationScoped
public class AlfrescoRestClient {

    private static final String NO_PROXY = "none";

    @Inject
    @GlobalValue(key = "alfrescoBaseUri")
    String alfrescoBaseUri;

    @Inject
    @GlobalValue(key = "alfresco.proxy.type", defaultValue = NO_PROXY)
    String proxyType;

    @Inject
    @GlobalValue(key = "alfresco.proxy.hostname", defaultValue = "none")
    String proxyHostname;

    @Inject
    @GlobalValue(key = "alfresco.proxy.port", defaultValue = "0")
    String proxyPort;

    /**
     * Sends a message via post.
     *
     * @param uri       - the URI to post the message to.
     * @param mediaType - the mediaType of the message.
     * @param headers   - any Http headers required.
     * @param entity    - the entity to post.
     * @return the response from the Http request.
     */
    public Response post(final String uri, final MediaType mediaType, final MultivaluedHashMap<String, Object> headers, final Entity entity) {
        return alfrescoRequestWith(uri, mediaType, headers).post(entity);
    }

    /**
     * Sends a message via a delete request.
     *
     * @param uri       - the URI to post the message to.
     * @param mediaType - the mediaType of the message.
     * @param headers   - any Http headers required.
     * @return the response from the Http request.
     */
    public Response delete(final String uri, final MediaType mediaType, final MultivaluedHashMap<String, Object> headers) {
        return alfrescoRequestWith(uri, mediaType, headers).delete();
    }

    /**
     * Request a resource as InputStream via a get.
     *
     * @param uri       - the URI to request the resource from.
     * @param mediaType - the mediaType of the resource.
     * @param headers   - any Http headers required for the request.
     * @return the response from the Http request.
     */
    public InputStream getAsInputStream(final String uri, final MediaType mediaType, final MultivaluedHashMap<String, Object> headers) {
        return alfrescoRequestWith(uri, mediaType, headers).get(InputStream.class);
    }

    private Builder alfrescoRequestWith(final String uri, final MediaType mediaType, final MultivaluedHashMap<String, Object> headers) {
        return client()
                .target(format("%s%s", alfrescoBaseUri, uri))
                .request(mediaType)
                .headers(headers);
    }

    private Client client() {
        if (NO_PROXY.equals(proxyType)) {
            return ClientBuilder.newClient();
        } else {
            return new ResteasyClientBuilder()
                    .defaultProxy(proxyHostname, parseInt(proxyPort), proxyType)
                    .build();
        }
    }

}
