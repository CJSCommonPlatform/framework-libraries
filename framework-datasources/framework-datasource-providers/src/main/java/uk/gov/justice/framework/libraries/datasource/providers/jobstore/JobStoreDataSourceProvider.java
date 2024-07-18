package uk.gov.justice.framework.libraries.datasource.providers.jobstore;

import javax.sql.DataSource;

public interface JobStoreDataSourceProvider {

    DataSource getJobStoreDataSource();
}
