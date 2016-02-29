package uk.gov.justice.services.adapter.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.justice.services.core.dispatcher.Dispatcher;

import javax.json.JsonObject;

public class DefaultDispatcher implements Dispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDispatcher.class);

    @Override
    public void dispatch(final Envelope<JsonObject> envelope) {
        LOGGER.info("Dispatched: " + envelope.getMessage().toString());
    }
}
