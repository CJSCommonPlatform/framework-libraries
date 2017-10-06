package uk.gov.justice.services.core.json;

import uk.gov.justice.services.core.json.DefaultFileSystemUrlResolverStrategy;
import uk.gov.justice.services.core.json.DefaultJsonSchemaValidator;
import uk.gov.justice.services.core.json.JsonSchemaLoader;
import uk.gov.justice.services.core.json.JsonSchemaValidator;

import org.slf4j.LoggerFactory;

public class DefaultJsonSchemaValidatorFactoryPI {

    public static JsonSchemaValidator getDefaultJsonSchemaValidator() {

        final JsonSchemaLoader loader = new JsonSchemaLoader();
        loader.fileSystemUrlResolverStrategy = new DefaultFileSystemUrlResolverStrategy();

        final DefaultJsonSchemaValidator defaultJsonSchemaValidator = new DefaultJsonSchemaValidator();
        defaultJsonSchemaValidator.loader = loader;
        defaultJsonSchemaValidator.logger = LoggerFactory.getLogger(DefaultJsonSchemaValidatorFactoryPI.class);

        return defaultJsonSchemaValidator;
    }
}
