package uk.gov.justice.services.test.utils.core.rest;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class RestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    public static final ResteasyClient RESTEASY_CLIENT = ResteasyClientBuilderFactory.clientBuilder().connectionPoolSize(5).build();

    public Response postCommand(final String url, final String contentType, final String requestPayload) {
        Entity<String> entity = Entity.entity(requestPayload, MediaType.valueOf(contentType));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Making POST request to '{}' with Content Type '{}' Request payload: '{}'", url, contentType, requestPayload);
        }

        try (Response response = RESTEASY_CLIENT.target(url).request().post(entity)) {
            response.bufferEntity();
            logIfFailed(response);
            return response;
        }
    }

    public Response postCommand(final String url, final String contentType, final String requestPayload, final MultivaluedMap<String, Object> headers) {
        Entity<String> entity = Entity.entity(requestPayload, MediaType.valueOf(contentType));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Making POST request to '{}' with Content Type '{}' Request payload: '{}' Headers: {} ", url, contentType, requestPayload, headers);
        }

        try (Response response = RESTEASY_CLIENT.target(url).request().headers(headers).post(entity)) {
            response.bufferEntity();
            logIfFailed(response);
            return response;
        }
    }

    public Response query(final String url, final String contentTypes) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Making GET request to '{}' with Content Type '{}'", url, contentTypes);
        }

        try (Response response = RESTEASY_CLIENT.target(url).request(new MediaType[]{MediaType.valueOf(contentTypes)}).get()) {
            response.bufferEntity();
            logIfFailed(response);
            return response;
        }
    }

    public Response query(final String url, final String contentTypes, final MultivaluedMap<String, Object> headers) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Making GET request to '{}' with Content Type '{}' and Headers: {}", url, contentTypes, headers);
        }

        try (Response response = RESTEASY_CLIENT.target(url).request().headers(headers).header("Accept", contentTypes).get()) {
            response.bufferEntity();
            logIfFailed(response);
            return response;
        }
    }

    public Response deleteCommand(final String url, final String contentType, final MultivaluedMap<String, Object> headers) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Making DELETE request to '{}' with Content Type '{}'", url, contentType);
        }

        try (Response response = RESTEASY_CLIENT.target(url).request().headers(headers).header("Content-Type", contentType).delete()) {
            response.bufferEntity();
            logIfFailed(response);
            return response;
        }
    }

    private static void logIfFailed(final Response response) {
        if (LOGGER.isInfoEnabled()) {
            Response.StatusType statusType = response.getStatusInfo();
            if (statusType.getFamily() != Response.Status.Family.SUCCESSFUL) {
                LOGGER.info("Received response status '{}' '{}' {} ", statusType.getStatusCode(), statusType.getReasonPhrase(), response.readEntity(String.class));
            }
        }
    }
}

