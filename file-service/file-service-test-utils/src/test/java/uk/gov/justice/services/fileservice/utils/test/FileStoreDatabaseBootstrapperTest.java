package uk.gov.justice.services.fileservice.utils.test;

import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.fileservice.utils.test.FileStoreDatabaseBootstrapper.LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML;

import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;

import java.sql.Connection;

import javax.sql.DataSource;

@RunWith(MockitoJUnitRunner.class)
public class FileStoreDatabaseBootstrapperTest {

    @Mock
    private LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper;

    @Mock
    private FileStoreTestDataSourceProvider fileStoreTestDataSourceProvider;

    @InjectMocks
    private FileStoreDatabaseBootstrapper fileStoreDatabaseBootstrapper;

    @Test
    public void shouldInitializeTheFileStoreDatabaseUsingLiquibase() throws Exception {

        final DataSource datasource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(fileStoreTestDataSourceProvider.getDatasource()).thenReturn(datasource);
        when(datasource.getConnection()).thenReturn(connection);

        fileStoreDatabaseBootstrapper.initDatabase();

        verify(liquibaseDatabaseBootstrapper).bootstrap(
                LIQUIBASE_FILE_STORE_DB_CHANGELOG_XML,
                connection);
        verify(connection).close();
    }
}