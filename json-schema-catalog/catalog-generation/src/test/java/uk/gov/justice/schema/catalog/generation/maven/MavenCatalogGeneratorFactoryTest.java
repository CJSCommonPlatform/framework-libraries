package uk.gov.justice.schema.catalog.generation.maven;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MavenCatalogGeneratorFactoryTest {

    @InjectMocks
    private MavenCatalogGeneratorFactory mavenCatalogGeneratorFactory;

    @Test
    public void shouldCreateAMavenCatalogGenerator() throws Exception {

        final Generator<List<URI>> generator = mavenCatalogGeneratorFactory.create();

        assertThat(generator, is(notNullValue()));
        assertThat(generator, is(instanceOf(MavenCatalogGenerator.class)));
    }
}
