package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassName;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassNameProviderTest {

    @InjectMocks
    private ClassNameProvider classNameProvider;

    @Test
    public void shouldConvertAUUIDStringToAUUIDClassName() throws Exception {

        final ClassName className = classNameProvider.classNameFor("UUID");
        assertThat(className.getFullyQualifiedName(), is(UUID.class.getName()));
    }

    @Test
    public void shouldConvertAZoneDateTimeStringToAZoneDateTimeClassName() throws Exception {

        final ClassName className = classNameProvider.classNameFor("ZonedDateTime");
        assertThat(className.getFullyQualifiedName(), is(ZonedDateTime.class.getName()));
    }

    @Test
    public void shouldConvertAnyUnknownStringToAStringClassName() throws Exception {

        final ClassName className = classNameProvider.classNameFor("SomethingSilly");
        assertThat(className.getFullyQualifiedName(), is(String.class.getName()));
    }

    @Test
    public void shouldHandleNullStrings() throws Exception {

        final ClassName className = classNameProvider.classNameFor(null);
        assertThat(className.getFullyQualifiedName(), is(String.class.getName()));
    }
}
