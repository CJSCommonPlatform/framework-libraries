package uk.gov.justice.services.fileservice.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.fileservice.api.ConfigurationException;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("Duplicates")
@RunWith(MockitoJUnitRunner.class)
public class DataSourceProviderTest {

    @Mock
    private InitialContextFactory initialContextFactory;

    @InjectMocks
    private DataSourceProvider dataSourceProvider;

    @Test
    public void shouldGetADataSourceUsingJndi() throws Exception {

        final InitialContext initialContext = mock(InitialContext.class);
        final DataSource dataSource = mock(DataSource.class);

        when(initialContextFactory.create()).thenReturn(initialContext);
        when(initialContext.lookup("java:/DS.fileservice")).thenReturn(dataSource);

        assertThat(dataSourceProvider.getDatasource(), is(dataSource));
    }

    @Test
    public void shouldWrapAndRethrowIfLookingUpTheDataSourceThrowsANamingException() throws Exception {

        final Throwable namingException = new NamingException("Ooops");

        final InitialContext initialContext = mock(InitialContext.class);

        when(initialContextFactory.create()).thenReturn(initialContext);
        when(initialContext.lookup("java:/DS.fileservice")).thenThrow(namingException);

        try {
            dataSourceProvider.getDatasource();
            fail();
        } catch (final ConfigurationException expected) {
            assertThat(expected.getCause(), is(namingException));
            assertThat(expected.getMessage(), is("Failed to get Connection from container using JNDI name 'java:/DS.fileservice'"));
        }
    }
}
