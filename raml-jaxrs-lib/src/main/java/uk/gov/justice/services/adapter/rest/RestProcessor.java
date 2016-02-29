package uk.gov.justice.services.adapter.rest;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Consumer;

public class RestProcessor {
    @Inject
    EnvelopeBuilder envelopeBuilder;

    public Response process(final Consumer<Envelope<JsonObject>> consumer, final JsonObject entity,
                            final HttpHeaders headers, final Map<String, String> pathParams) {

        JsonObjectBuilder jsonObjectBuilder = JsonObjectUtils.createObject(entity);

        for (String key : pathParams.keySet()) {
            jsonObjectBuilder = jsonObjectBuilder.add(key, pathParams.get(key));
        }

        Envelope<JsonObject> envelope = envelopeBuilder
                .withMetadataFrom(headers)
                .withBody(jsonObjectBuilder.build())
                .build();
        
        consumer.accept(envelope);
        return Response.status(202).build();
    }
   
}
