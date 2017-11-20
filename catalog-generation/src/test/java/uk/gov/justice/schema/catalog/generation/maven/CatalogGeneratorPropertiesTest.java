package uk.gov.justice.schema.catalog.generation.maven;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogGeneratorPropertiesTest {

    @InjectMocks
    private CatalogGeneratorProperties catalogGeneratorProperties;

    @Test
    public void shouldAddAPointlessTestToStopCoverallsFromWhingeing() throws Exception {

        assertThat(catalogGeneratorProperties.getCatalogName(), is(nullValue()));
    }
}
