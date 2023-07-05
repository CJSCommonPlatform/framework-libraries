package uk.gov.justice.schema.catalog.generation.maven;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.schema.catalog.generation.CatalogGenerationRunner;
import uk.gov.justice.schema.catalog.generation.GenerationObjectFactory;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MavenCatalogGeneratorTest {

    @Mock
    private GenerationObjectFactory generationObjectFactory;

    @InjectMocks
    private MavenCatalogGenerator mavenCatalogGenerator;

    @Test
    public void shouldInstantiateAndRunTheCatalogGeneration() throws Exception {

        final String catalogName = "my catalog";
        final List<URI> schemaFiles = singletonList(new URI("/a-schema-file.json"));
        final Path catalogGenerationPath = Paths.get(new URI("file:/path/to/catalog/generation/directory"));
        final Path jsonSchemaPath = Paths.get("json/schema/");

        final CatalogGenerationRunner catalogGenerationRunner = mock(CatalogGenerationRunner.class);
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final CatalogGeneratorProperties catalogGeneratorProperties = mock(CatalogGeneratorProperties.class);

        when(generatorConfig.getOutputDirectory()).thenReturn(catalogGenerationPath);
        when(generationObjectFactory.catalogGenerationRunner()).thenReturn(catalogGenerationRunner);
        when(generatorConfig.getGeneratorProperties()).thenReturn(catalogGeneratorProperties);
        when(catalogGeneratorProperties.getCatalogName()).thenReturn(catalogName);
        when(catalogGeneratorProperties.getJsonSchemaPath()).thenReturn(jsonSchemaPath);

        mavenCatalogGenerator.run(schemaFiles, generatorConfig);

        verify(catalogGenerationRunner).generateCatalog(catalogName, schemaFiles, catalogGenerationPath, jsonSchemaPath);
    }
}
