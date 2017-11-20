package uk.gov.justice.schema.catalog.generation;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogGenerationRunnerTest {

    @Mock
    private CatalogObjectGenerator catalogObjectGenerator;

    @Mock
    private CatalogWriter catalogWriter;

    @Mock
    private UrlConverter urlConverter;

    @InjectMocks
    private CatalogGenerationRunner catalogGenerationRunner;

    @Test
    public void shouldGenerateACatalogFile() throws Exception {

        final String catalogName = "catalog name";
        final URI schemaFile = new URI("file:/schemaFile.json");
        final URL schemaFileUrl = schemaFile.toURL();

        final Catalog catalog = mock(Catalog.class);

        when(urlConverter.toUrl(schemaFile)).thenReturn(schemaFileUrl);
        when(catalogObjectGenerator.generate(
                catalogName,
                singletonList(schemaFileUrl))).thenReturn(catalog);

        catalogGenerationRunner.generateCatalog(catalogName, singletonList(schemaFile));

        verify(catalogWriter).write(catalog);
    }
}
