package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class JdbcJobStoreDataSourceProviderTest {

    private static final String SERVICE = "test";

    @Mock
    private Context initialContextMock;

    @Mock
    private Logger logger;

    @InjectMocks
    private JdbcJobStoreDataSourceProvider provider;

    @BeforeEach
    public void setup() throws Exception {
        setField(provider, "warFileName", "test-event-processor");
    }

    @Test
    public void shouldReturnDatasource() throws NamingException {

        final DataSource dataSourceMock = mock(DataSource.class);
        when(initialContextMock.lookup(eq(format("java:/app/%s-event-processor/DS.jobstore", SERVICE)))).thenReturn(dataSourceMock);

        final DataSource dataSource = provider.getDataSource();

        assertNotNull(dataSource, "Returned DataSource was null!");
        assertThat(dataSource, is(dataSourceMock));
    }

    @Test
    public void shouldGetInitialContext() throws NamingException {
        final Context initialContext = getValueOfField(provider, "initialContext", Context.class);
        assertThat(initialContext, notNullValue());
    }

    @Test
    public void shouldThrowJdbcRespositoryException() throws NamingException {
        when(initialContextMock.lookup(eq(format("java:/app/%s-event-processor/DS.jobstore", SERVICE)))).thenThrow(new NamingException());
        
        assertThrows(JdbcRepositoryException.class, () -> provider.getDataSource());
    }
}