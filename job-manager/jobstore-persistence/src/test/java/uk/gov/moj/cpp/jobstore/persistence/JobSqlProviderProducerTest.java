package uk.gov.moj.cpp.jobstore.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

public class JobSqlProviderProducerTest {

    @Mock
    private Logger logger;

    @Test
    public void shouldReturnPostgresJobSqlProvider() {

        final JobSqlProviderProducer jobSqlProviderProducer = new JobSqlProviderProducer();
        jobSqlProviderProducer.strategyClass = "uk.gov.moj.cpp.jobstore.persistence.PostgresJobSqlProvider";
        jobSqlProviderProducer.logger = mock(Logger.class);

        final JobSqlProvider jobSqlProvider = jobSqlProviderProducer.jobSqlProvider();
        assertThat(jobSqlProvider, instanceOf(PostgresJobSqlProvider.class));
    }

    @Test
    public void shouldThrowExceptionWhenStrategyNotFound() {
        final JobSqlProviderProducer jobSqlProviderProducer = new JobSqlProviderProducer();
        jobSqlProviderProducer.logger = mock(Logger.class);
        jobSqlProviderProducer.strategyClass = "invalid strategy";

        
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                jobSqlProviderProducer.jobSqlProvider()
        );

        assertThat(illegalArgumentException.getMessage(), is("Could not instantiate Job SQL provider strategy."));
    }
}
