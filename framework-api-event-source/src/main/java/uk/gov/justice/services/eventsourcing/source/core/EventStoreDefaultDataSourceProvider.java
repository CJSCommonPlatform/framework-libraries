package uk.gov.justice.services.eventsourcing.source.core;

import javax.sql.DataSource;

public interface EventStoreDefaultDataSourceProvider {

    DataSource getDataSource();
}
