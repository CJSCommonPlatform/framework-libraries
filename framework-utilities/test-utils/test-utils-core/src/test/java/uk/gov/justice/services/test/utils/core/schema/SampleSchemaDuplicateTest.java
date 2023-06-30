package uk.gov.justice.services.test.utils.core.schema;


import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class SampleSchemaDuplicateTest {

    @Test
    public void testCheckSchemaCompliance()
    {
        try {
            SchemaDuplicateTestHelper.failTestIfDifferentSchemasWithSameName();
            fail("An assertion error was expected but nothing was caught");
        } catch(AssertionError ignored){
        }
    }

}
