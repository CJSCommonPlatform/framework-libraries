package uk.gov.justice.services.adapter.rest;

/**
 * Created by david on 15/02/16.
 */
public interface Envelope<M> {

    Metadata getMetadata();

    M getMessage();
}
