package uk.gov.justice.services.test.utils.core.http;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.INTEGRATION_HOST_KEY;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class BaseUriProviderTest {

    private BaseUriProvider baseUriProvider = new BaseUriProvider();

    @AfterEach
    public void resetTheSystemPropertySetInTheTest() {
        System.clearProperty(INTEGRATION_HOST_KEY);
    }

    @Test
    public void shouldGetTheBaseUriWithLocalhostAsHostnameByDefault() throws Exception {

        assertThat(baseUriProvider.getBaseUri(), is("http://localhost:8080"));
    }

    @Test
    public void shouldGetTheBaseUriWithSystemPropertyAsHostnameIfSet() throws Exception {

        System.setProperty(INTEGRATION_HOST_KEY, "my.funky.domain.com");

        assertThat(baseUriProvider.getBaseUri(), is("http://my.funky.domain.com:8080"));
    }

}
