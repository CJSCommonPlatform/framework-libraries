package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class GenerationContextTest {

    @InjectMocks
    private GenerationContext generationContext;

    @Test
    public void shouldGetTheCorrectLoggerForTheClass() throws Exception {

        final Logger logger = generationContext.getLoggerFor(getClass());

        assertThat(logger.getName(), is(getClass().getName()));
    }
}
