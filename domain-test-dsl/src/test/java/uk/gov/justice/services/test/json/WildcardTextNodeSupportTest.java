package uk.gov.justice.services.test.json;

import static org.junit.Assert.fail;

import org.junit.Test;

public class WildcardTextNodeSupportTest {

    @Test(expected = IllegalAccessException.class)
    public void preventInitialisationTest() throws ReflectiveOperationException {

        WildcardTextNodeSupport disallowed = (WildcardTextNodeSupport)
                Class.forName(WildcardTextNodeSupport.class.getName()).getDeclaredConstructor().newInstance();

        fail(disallowed.toString());
    }
}
