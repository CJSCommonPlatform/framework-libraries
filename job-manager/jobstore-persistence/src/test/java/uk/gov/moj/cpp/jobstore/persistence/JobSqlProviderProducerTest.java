package uk.gov.moj.cpp.jobstore.persistence;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.slf4j.Logger;

public class JobSqlProviderProducerTest {

    @Mock
    private Logger logger;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Could not instantiate Job SQL provider strategy");

        jobSqlProviderProducer.jobSqlProvider();
    }
}
