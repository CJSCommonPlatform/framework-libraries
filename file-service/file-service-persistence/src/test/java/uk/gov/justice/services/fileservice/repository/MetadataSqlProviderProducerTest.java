package uk.gov.justice.services.fileservice.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
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

    @Test
    public void shouldThrowExceptionIfClassDoesNotExist() {
        metadataSqlProviderProducer.strategyClass = "uk.gov.justice.services.eventsourcing.repository.jdbc.SomeUnknowClazzz";

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                metadataSqlProviderProducer.metadataSqlProvider()
        );

        assertThat(illegalArgumentException.getMessage(), is("Could not instantiate File Service SQL provider strategy."));
    }
}