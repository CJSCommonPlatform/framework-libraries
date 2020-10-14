package uk.gov.justice.services.file.alfresco;

import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static uk.gov.justice.services.file.alfresco.Headers.ALFRESCO_USER_ID;
import static uk.gov.justice.services.file.alfresco.Headers.headersWithUserId;

import org.junit.Test;

public class HeadersTest {

    @Test
    public void shouldBeWellDefinedUtilityClass() {
        assertUtilityClassWellDefined(Headers.class);
    }

    @Test
    public void shouldSetAlfrescoUserId() throws Exception {
        assertThat(headersWithUserId("userId").get(ALFRESCO_USER_ID), contains("userId"));
    }
}