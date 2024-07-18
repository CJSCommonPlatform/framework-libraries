package uk.gov.justice.framework.libraries.datasource.providers.jobstore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestJobStoreDataSourceProviderTest {

    @InjectMocks
    private TestJobStoreDataSourceProvider testJobStoreDataSourceProvider;

    @Test
    public void shouldGetJobstoreDatasource() throws Exception {
        assertThat(testJobStoreDataSourceProvider.getJobStoreDataSource(), is(notNullValue()));
    }
}