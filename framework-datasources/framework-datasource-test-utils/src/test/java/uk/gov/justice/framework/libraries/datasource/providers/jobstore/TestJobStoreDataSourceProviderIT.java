package uk.gov.justice.framework.libraries.datasource.providers.jobstore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

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