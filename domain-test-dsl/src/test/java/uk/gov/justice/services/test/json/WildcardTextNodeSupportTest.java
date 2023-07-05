package uk.gov.justice.services.test.json;

import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WildcardTextNodeSupportTest {

    @Test
    public void preventInitialisationTest() throws Exception {
        assertThrows(
                IllegalAccessException.class,
                () -> Class.forName(WildcardTextNodeSupport.class.getName())
                        .getDeclaredConstructor()
                        .newInstance());
    }
}
