package uk.gov.justice.services.adapter.direct;

public interface SynchronousDirectAdapterCache {

    SynchronousDirectAdapter directAdapterForComponent(final String component);
}
