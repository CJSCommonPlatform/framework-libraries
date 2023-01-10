package uk.gov.justice.generation.io.files.parser;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.loader.Resource;
import uk.gov.justice.generation.io.files.loader.ResourceLoader;
import uk.gov.justice.generation.io.files.loader.ResourceLoaderFactory;
import uk.gov.justice.generation.io.files.resolver.SchemaResolver;
import uk.gov.justice.generation.io.files.resolver.SchemaResolverException;
import uk.gov.justice.schema.catalog.SchemaCatalogResolver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SchemaDefinitionParserTest {

    @Mock
    private ResourceLoaderFactory resourceLoaderFactory;

    @Mock
    private SchemaResolver schemaResolver;

    @Mock
    private SchemaCatalogResolver schemaCatalogResolver;

    @Mock
    private ResourceProvider resourceProvider;

    @Mock
    private Logger logger;


    @InjectMocks
    private SchemaDefinitionParser schemaDefinitionParser;

    @Test
    public void shouldCreateSchemaDefinitionsFromListOfPaths() throws Exception {

        final Path basePath = Paths.get("basePath");
        final Path path_1 = Paths.get("filename_1");
        final Path path_2 = Paths.get("filename_2");
        final List<Path> paths = asList(path_1, path_2);

        final ResourceLoader resourceLoader = mock(ResourceLoader.class);

        final Resource resource_1 = mock(Resource.class);
        final Schema schema_1 = mock(Schema.class);

        final Resource resource_2 = mock(Resource.class);
        final Schema schema_2 = mock(Schema.class);

        when(resourceProvider.getResource(basePath, path_1, resourceLoader)).thenReturn(resource_1);
        when(schemaResolver.resolve(resource_1)).thenReturn(schema_1);

        when(resourceProvider.getResource(basePath, path_2, resourceLoader)).thenReturn(resource_2);
        when(schemaResolver.resolve(resource_2)).thenReturn(schema_2);

        when(resourceLoaderFactory.resourceLoaderFor(basePath)).thenReturn(resourceLoader);

        final List<SchemaDefinition> schemaDefinitions = (List<SchemaDefinition>) schemaDefinitionParser.parse(basePath, paths);

        assertThat(schemaDefinitions.size(), is(2));

        assertThat(schemaDefinitions.get(0).getFilename(), is("filename_1"));
        assertThat(schemaDefinitions.get(0).getSchema(), is(schema_1));
        assertThat(schemaDefinitions.get(1).getFilename(), is("filename_2"));
        assertThat(schemaDefinitions.get(1).getSchema(), is(schema_2));
        verify(schemaCatalogResolver).updateCatalogSchemaCache(basePath, paths);
    }

    @Test
    public void shouldIgnoreAndLogIfLoadingAResourceFails() throws Exception {

        final Path basePath = Paths.get("basePath");
        final Path path_1 = Paths.get("filename_1");
        final Path path_2 = Paths.get("filename_2");
        final ResourceLoader resourceLoader = mock(ResourceLoader.class);

        final Resource resource_1 = mock(Resource.class);

        final Resource resource_2 = mock(Resource.class);
        final Schema schema_2 = mock(Schema.class);

        when(resourceProvider.getResource(basePath, path_1, resourceLoader)).thenReturn(resource_1);
        when(schemaResolver.resolve(resource_1)).thenThrow(new SchemaResolverException("Oh my"));

        when(resourceProvider.getResource(basePath, path_2, resourceLoader)).thenReturn(resource_2);
        when(schemaResolver.resolve(resource_2)).thenReturn(schema_2);

        when(resourceLoaderFactory.resourceLoaderFor(basePath)).thenReturn(resourceLoader);

        final List<SchemaDefinition> schemaDefinitions = (List<SchemaDefinition>) schemaDefinitionParser.parse(basePath, asList(path_1, path_2));

        assertThat(schemaDefinitions.size(), is(1));

        assertThat(schemaDefinitions.get(0).getFilename(), is("filename_2"));
        assertThat(schemaDefinitions.get(0).getSchema(), is(schema_2));

        verify(logger).warn("Skipping resource for basePath: basePath, resourcePath: filename_1, with reason: Oh my");
    }
}
