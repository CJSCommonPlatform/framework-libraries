package uk.gov.justice.services.core.dispatcher;

import javax.json.JsonObject;

import uk.gov.justice.services.adapter.rest.Envelope;

/**
 * Created by david on 15/02/16.
 */
public interface Dispatcher {
    void dispatch(Envelope<JsonObject> message);
}
