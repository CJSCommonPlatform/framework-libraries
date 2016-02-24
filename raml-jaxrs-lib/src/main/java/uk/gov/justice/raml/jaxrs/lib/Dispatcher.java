package uk.gov.justice.raml.jaxrs.lib;

import javax.json.JsonObject;

/**
 * Created by david on 15/02/16.
 */
public interface Dispatcher {
    void dispatch(Envelope<JsonObject> message);
}
