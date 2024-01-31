package uk.gov.justice.services.test.utils.core.schema;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaDuplicateTestHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaDuplicateTestHelper.class);

    private SchemaDuplicateTestHelper() { }

    /**
     * Test helper which runs the schema duplicate check.
     * Supports the suppression of false positives for json files whose difference is only whitespace.
     * Raises an assertion error if duplicate schemas with different content is found (safe whitespace)
     */
    public static void failTestIfDifferentSchemasWithSameName() {
        SchemaDuplicateChecker scc = new SchemaDuplicateChecker();
        try {
            scc.runDuplicateCheck();
        } catch (SchemaCheckerException e) {
            LOGGER.error("Schema duplicates check failed", e);
            throw e;
        }
        LOGGER.info("Total duplicated schemas: {}", scc.getNumberOfViolatingSchemas());
        final String format = format("Schema duplicates check test failed with %s schema(s)", scc.getNumberOfViolatingSchemas());
        assertThat(format, scc.isCompliant(), is(true));
    }
}
