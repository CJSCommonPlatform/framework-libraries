package uk.gov.justice.services.test.utils.core.random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.Times.times;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.typeCheck;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UriGeneratorTest {

    private static final String URI_PATTERN = "[-0-9a-zA-Z]{1,63}\\.[-.0-9a-zA-Z]+";

    @Test
    public void shouldGenerateValidUris() {
        final UriGenerator uriGenerator = new UriGenerator();

        typeCheck(uriGenerator, s -> s.getScheme().equals("http") && s.getAuthority().matches(URI_PATTERN)).verify(times(10000));
    }

    @Test
    public void shouldThrowExceptionWhenGeneratedUriIsInvalid() throws Exception {
        final UriGenerator uriGenerator = new UriGenerator();

        final DomainPartGenerator domainPartGenerator = mock(DomainPartGenerator.class);
        when(domainPartGenerator.next()).thenReturn("");
        modifyUnderlyingGenerator(uriGenerator, domainPartGenerator);

        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                uriGenerator.next()
        );

        assertThat(runtimeException.getMessage(), is("Generated URI http:// is invalid"));
    }

    private void modifyUnderlyingGenerator(final UriGenerator uriGenerator, final DomainPartGenerator domainPartGenerator) throws NoSuchFieldException, IllegalAccessException {
        final Field field = uriGenerator.getClass().getDeclaredField("domainPartGenerator");
        field.setAccessible(true);
        field.set(uriGenerator, domainPartGenerator);
    }
}