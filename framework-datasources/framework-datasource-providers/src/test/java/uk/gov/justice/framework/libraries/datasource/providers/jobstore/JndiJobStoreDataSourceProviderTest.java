package uk.gov.justice.framework.libraries.datasource.providers.jobstore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.libraries.datasource.providers.DatasourceAcquisitionException;
import uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProvider;
import uk.gov.justice.services.jdbc.persistence.InitialContextFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JndiJobStoreDataSourceProviderTest {

    @Mock
    private InitialContextFactory initialContextFactory;

    @Mock
    private JndiBasedServiceContextNameProvider jndiBasedServiceContextNameProvider;

    @InjectMocks
    private JndiJobStoreDataSourceProvider jndiJobStoreDataSourceProvider;

    @Test
    public void shouldGetJobstoreDataSourceFromJndi() throws Exception {
        final String serviceContextName = "my-context-service";

        final InitialContext initialContext = mock(InitialContext.class);
        final DataSource jobStoreDataSource = mock(DataSource.class);

        when(jndiBasedServiceContextNameProvider.getServiceContextName()).thenReturn(serviceContextName);
        when(initialContextFactory.create()).thenReturn(initialContext);
        when(initialContext.lookup("java:/app/my-context-service/DS.jobstore")).thenReturn(jobStoreDataSource);

        assertThat(jndiJobStoreDataSourceProvider.getJobStoreDataSource(), is(jobStoreDataSource));
    }

    @Test
    public void shouldOnlyLookUpDataSourceFromJndiOneTimeAndCacheIt() throws Exception {
        final String serviceContextName = "my-context-service";

        final InitialContext initialContext = mock(InitialContext.class);
        final DataSource jobStoreDataSource = mock(DataSource.class);

        when(jndiBasedServiceContextNameProvider.getServiceContextName()).thenReturn(serviceContextName);
        when(initialContextFactory.create()).thenReturn(initialContext);
        when(initialContext.lookup("java:/app/my-context-service/DS.jobstore")).thenReturn(jobStoreDataSource);

        assertThat(jndiJobStoreDataSourceProvider.getJobStoreDataSource(), is(jobStoreDataSource));
        assertThat(jndiJobStoreDataSourceProvider.getJobStoreDataSource(), is(jobStoreDataSource));
        assertThat(jndiJobStoreDataSourceProvider.getJobStoreDataSource(), is(jobStoreDataSource));
        assertThat(jndiJobStoreDataSourceProvider.getJobStoreDataSource(), is(jobStoreDataSource));
        assertThat(jndiJobStoreDataSourceProvider.getJobStoreDataSource(), is(jobStoreDataSource));

        verify(jndiBasedServiceContextNameProvider, times(1)).getServiceContextName();
        verify(initialContextFactory, times(1)).create();
        verify(initialContext, times(1)).lookup(anyString());
    }

    @Test
    public void shouldThrowDatasourceAcquisitionExceptionIfJndiLookupOfDataSourceFails() throws Exception {

        final NamingException namingException = new NamingException("Ooops");
        final String serviceContextName = "my-context-service";

        final InitialContext initialContext = mock(InitialContext.class);

        when(jndiBasedServiceContextNameProvider.getServiceContextName()).thenReturn(serviceContextName);
        when(initialContextFactory.create()).thenReturn(initialContext);
        when(initialContext.lookup("java:/app/my-context-service/DS.jobstore")).thenThrow(namingException);

        final DatasourceAcquisitionException datasourceAcquisitionException = assertThrows(
                DatasourceAcquisitionException.class,
                () -> jndiJobStoreDataSourceProvider.getJobStoreDataSource());

        assertThat(datasourceAcquisitionException.getCause(), is(namingException));
        assertThat(datasourceAcquisitionException.getMessage(), is("Failed to get jobstore datasource using jndi name 'java:/app/my-context-service/DS.jobstore'"));
    }
}