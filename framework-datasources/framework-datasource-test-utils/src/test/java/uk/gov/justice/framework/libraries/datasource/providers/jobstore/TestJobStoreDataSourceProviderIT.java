package uk.gov.justice.framework.libraries.datasource.providers.jobstore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

public class TestJobStoreDataSourceProviderIT {

    private TestJobStoreDataSourceProvider testJobStoreDataSourceProvider = new TestJobStoreDataSourceProvider();

    @Test
    public void shouldProvideDataSourceToJobstoreDatabaseForTests() throws Exception {

        final DataSource jobStoreDataSource = testJobStoreDataSourceProvider.getJobStoreDataSource();

        try(final Connection connection = jobStoreDataSource.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT NOW  ()");
            final ResultSet resultSet = preparedStatement.executeQuery()) {
            assertThat(resultSet.next(), is(true));
        }
    }
}