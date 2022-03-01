package uk.gov.justice.services.fileservice.utils.test;


import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getHost;

import uk.gov.justice.services.fileservice.repository.DataSourceProvider;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

public class FileStoreTestDataSourceProvider extends DataSourceProvider {

    private static final int PORT = 5432;

    public DataSource getDatasource()  {

        final PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName(getHost());
        dataSource.setPortNumber(PORT);
        dataSource.setDatabaseName("frameworkfilestore");
        dataSource.setUser("framework");
        dataSource.setPassword("framework");

        return dataSource;
    }
}
