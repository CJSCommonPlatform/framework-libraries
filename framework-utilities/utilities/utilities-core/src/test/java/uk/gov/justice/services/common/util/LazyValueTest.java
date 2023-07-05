package uk.gov.justice.services.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LazyValueTest {

    @InjectMocks
    private LazyValue lazyValue;

    @Test
    public void shouldLazilyGetAValue() throws Exception {

        final String aString = "some string";

        final StringProviderForTesting stringProvider = mock(StringProviderForTesting.class);

        when(stringProvider.getString()).thenReturn(aString);

        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));
        assertThat(lazyValue.createIfAbsent(stringProvider::getString), is(aString));

        verify(stringProvider, times(1)).getString();
    }
}