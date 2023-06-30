package uk.gov.justice.services.core.extension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.core.annotation.ServiceComponentLocation.LOCAL;

import javax.enterprise.inject.spi.Bean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link ServiceComponentFoundEvent} class.
 */
@ExtendWith(MockitoExtension.class)
public class ServiceComponentFoundEventTest {

    @Mock
    private Bean<Object> bean;

    private ServiceComponentFoundEvent event;

    @BeforeEach
    public void setup() {
        event = new ServiceComponentFoundEvent("COMMAND_API", bean, LOCAL);
    }

    @Test
    public void shouldReturnBean() {
        assertThat(event.getHandlerBean(), equalTo(bean));
    }

    @Test
    public void shouldReturnCommandApiComponent() {
        assertThat(event.getComponentName(), equalTo("COMMAND_API"));
    }

    @Test
    public void shouldReturnLocation() {
        assertThat(event.getLocation(), equalTo(LOCAL));
    }
}
