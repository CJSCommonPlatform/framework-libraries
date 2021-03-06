package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class JdbcJobStoreDataSourceProviderTest {

    private static final String SERVICE = "test";

    @Mock
    private Context initialContextMock;

    @Mock
    private Logger logger;

    @InjectMocks
    private JdbcJobStoreDataSourceProvider provider;

    @Before
    public void setup() throws Exception {
        setField(provider, "warFileName", "test-event-processor");
    }

    @Test
    public void shouldReturnDatasource() throws NamingException {

        final DataSource dataSourceMock = mock(DataSource.class);
        when(initialContextMock.lookup(eq(format("java:/app/%s-event-processor/DS.jobstore", SERVICE)))).thenReturn(dataSourceMock);

        final DataSource dataSource = provider.getDataSource();

        assertNotNull("Returned DataSource was null!", dataSource);
        assertThat(dataSource, is(dataSourceMock));
    }

    @Test
    public void shouldGetInitialContext() throws NamingException {
        final Context initialContext = getValueOfField(provider, "initialContext", Context.class);
        assertThat(initialContext, notNullValue());
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRespositoryException() throws NamingException {
        when(initialContextMock.lookup(eq(format("java:/app/%s-event-processor/DS.jobstore", SERVICE)))).thenThrow(new NamingException());
        provider.getDataSource();
    }
}