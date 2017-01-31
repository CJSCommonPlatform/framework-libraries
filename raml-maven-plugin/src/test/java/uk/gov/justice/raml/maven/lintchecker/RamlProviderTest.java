package uk.gov.justice.raml.maven.lintchecker;

import static java.nio.file.Paths.get;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.raml.model.Raml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import uk.gov.justice.raml.io.files.parser.RamlFileParser;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;


@RunWith(MockitoJUnitRunner.class)
public class RamlProviderTest {

    @Mock
    private RamlFileParser ramlFileParser;

    @InjectMocks
    private RamlProvider ramlProvider;

    @Test
    public void shouldGetTheRamlFilesAsObjects() throws Exception {

        final File sourceDirectory = new File(".");
        final Collection<Path> paths = singletonList(get("."));
        final Collection<Raml> ramls = singletonList(mock(Raml.class));

        when(ramlFileParser.ramlOf(sourceDirectory.toPath(), paths)).thenReturn(ramls);

        assertThat(ramlProvider.getRamls(sourceDirectory, paths), is(ramls));
    }
}
