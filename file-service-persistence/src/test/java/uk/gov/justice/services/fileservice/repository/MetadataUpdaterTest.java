package uk.gov.justice.services.fileservice.repository;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.of;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.UtcClock;

import java.time.ZonedDateTime;

import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MetadataUpdaterTest {

    @Mock
    private UtcClock utcClock;

    @InjectMocks
    private MetadataUpdater metadataUpdater;


    @Test
    public void shouldAddTheCurrentTimeInTheCorrectFormat() throws Exception {

        final ZonedDateTime dateTime = of(2017, 2, 23, 16, 45, 0, 0, UTC);

        final String existingValue = "someValue";
        final JsonObject metadata = createObjectBuilder()
                .add("someField", existingValue)
                .build();

        when(utcClock.now()).thenReturn(dateTime);

        final JsonObject updatedMetadata = metadataUpdater.addCreatedTime(metadata);
        final String json = updatedMetadata.toString();

        with(json)
                .assertThat("$.someField", is(existingValue))
                .assertThat("$.createdAt", is("2017-02-23T16:45:00.000Z"))
        ;
    }
}
