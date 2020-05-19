package uk.gov.justice.generation.io.files.parser;

import uk.gov.justice.generation.io.files.loader.InputStreamToJsonObjectConverter;
import uk.gov.justice.generation.io.files.loader.ResourceLoaderFactory;
import uk.gov.justice.generation.io.files.resolver.SchemaResolver;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.FileParserFactory;
import uk.gov.justice.schema.catalog.CatalogObjectFactory;
import uk.gov.justice.schema.catalog.SchemaCatalogResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaDefinitionParserFactory implements FileParserFactory<SchemaDefinition> {

    public FileParser<SchemaDefinition> create() {

        final Logger logger = LoggerFactory.getLogger(SchemaDefinitionParser.class);

        final CatalogObjectFactory catalogObjectFactory = new CatalogObjectFactory();

        final SchemaCatalogResolver schemaCatalogResolver = catalogObjectFactory.schemaCatalogResolver();
        final InputStreamToJsonObjectConverter inputStreamToJsonObjectConverter = new InputStreamToJsonObjectConverter();
        final ResourceLoaderFactory resourceLoaderFactory = new ResourceLoaderFactory();
        final SchemaResolver schemaResolver = new SchemaResolver(schemaCatalogResolver);
        final ResourceProvider resourceProvider = new ResourceProvider(inputStreamToJsonObjectConverter);

        return new SchemaDefinitionParser(
                resourceLoaderFactory,
                schemaResolver,
                schemaCatalogResolver,
                resourceProvider,
                logger
        );
    }
}
