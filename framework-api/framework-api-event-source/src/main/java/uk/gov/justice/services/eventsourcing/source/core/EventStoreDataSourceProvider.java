package uk.gov.justice.services.eventsourcing.source.core;

import javax.sql.DataSource;

public interface EventStoreDataSourceProvider {

    /**
     * Gets an event store DataSource using the JNDI name marked as default in event-sources.yaml;
     *
     * @return The instantiated DataSource
     */
    DataSource getDefaultDataSource();

    /**
     * Gets an event store DataSource by its JNDI name
     *
     * @param jndiName The JNDI name as specified in event-sources.yaml
     *
     * @return The instantiated DataSource
     */
    DataSource getDataSource(final String jndiName);
}
