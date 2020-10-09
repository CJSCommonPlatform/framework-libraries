package uk.gov.justice.services.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LazyValueTest {

    @InjectMocks
    private LazyValue lazyValue;

    @Test
    public void shouldLazilyGetAValue() throws Exception {

        final String aString = "some string";

        final StringProviderForTesting stringProvider = mock(StringProviderForTesting.class);

        when(stringProvider.getString()).thenReturn(aString);

        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));
        assertThat(lazyValue.get(stringProvider::getString), is(aString));

        verify(stringProvider, times(1)).getString();
    }
}