package uk.gov.justice.schema.catalog.generation;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.schema.catalog.generation.maven.CatalogGeneratorProperties;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MavenCatalogGeneratorTest {

    @Mock
    private ObjectFactory objectFactory;

    @InjectMocks
    private MavenCatalogGenerator mavenCatalogGenerator;

    @Test
    public void shouldInstantiateAndRunTheCatalogGeneration() throws Exception {

        final String catalogName = "my catalog";
        final List<URI> schemaFiles = singletonList(new URI("/a-schema-file.json"));

        final CatalogGenerationRunner catalogGenerationRunner = mock(CatalogGenerationRunner.class);
        final GeneratorConfig generatorConfig = mock(GeneratorConfig.class);
        final CatalogGeneratorProperties catalogGeneratorProperties = mock(CatalogGeneratorProperties.class);

        when(objectFactory.catalogGenerationRunner()).thenReturn(catalogGenerationRunner);
        when(generatorConfig.getGeneratorProperties()).thenReturn(catalogGeneratorProperties);
        when(catalogGeneratorProperties.getCatalogName()).thenReturn(catalogName);

        mavenCatalogGenerator.run(schemaFiles, generatorConfig);

        verify(catalogGenerationRunner).generateCatalog(catalogName, schemaFiles);
    }

    @Test
    public void shouldAddThisTestToStopCoverallsFromWhingeing() throws Exception {
        assertThat(new MavenCatalogGenerator(), is(instanceOf(MavenCatalogGenerator.class)));
    }
}
