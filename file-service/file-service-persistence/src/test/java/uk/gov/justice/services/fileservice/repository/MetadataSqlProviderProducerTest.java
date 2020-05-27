package uk.gov.justice.services.fileservice.repository;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class MetadataSqlProviderProducerTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private MetadataSqlProviderProducer metadataSqlProviderProducer;

    @Test
    public void shouldProducePostgresStrategy() throws Exception {
        metadataSqlProviderProducer.strategyClass = "uk.gov.justice.services.fileservice.repository.PostgresMetadataSqlProvider";
        assertThat(metadataSqlProviderProducer.metadataSqlProvider(), instanceOf(PostgresMetadataSqlProvider.class));
    }

    @Test
    public void shouldProduceAnsiSQLStrategy() throws Exception {
        metadataSqlProviderProducer.strategyClass = "uk.gov.justice.services.fileservice.repository.AnsiMetadataSqlProvider";
        assertThat(metadataSqlProviderProducer.metadataSqlProvider(), instanceOf(AnsiMetadataSqlProvider.class));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfClassDoesNotExist() {
        metadataSqlProviderProducer.strategyClass = "uk.gov.justice.services.eventsourcing.repository.jdbc.SomeUnknowClazzz";

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Could not instantiate File Service SQL provider strategy.");

        metadataSqlProviderProducer.metadataSqlProvider();
    }
}