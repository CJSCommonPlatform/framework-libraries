package uk.gov.justice.raml.jaxrs.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;

public class DefaultDispatcher implements Dispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDispatcher.class);

    @Override
    public void dispatch(final Envelope<JsonObject> envelope) {
        LOGGER.info("Dispatched: " + envelope.getMessage().toString());
    }
}
