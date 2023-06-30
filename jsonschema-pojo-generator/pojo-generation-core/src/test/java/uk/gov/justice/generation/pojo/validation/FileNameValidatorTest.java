package uk.gov.justice.generation.pojo.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileNameValidatorTest {

    @Test
    public void shouldReturnTrueForEventSchemaFile() throws Exception {
        final File file = mock(File.class);

        when(file.getName()).thenReturn("example.events.test-event.json");

        assertThat(new FileNameValidator().isEventSchema(file), is(true));
    }

    @Test
    public void shouldReturnFalseForNonEventSchemaFile() throws Exception {
        final File file = mock(File.class);

        when(file.getName()).thenReturn("example.non.event.test-schema.json");

        assertThat(new FileNameValidator().isEventSchema(file), is(false));
    }
}