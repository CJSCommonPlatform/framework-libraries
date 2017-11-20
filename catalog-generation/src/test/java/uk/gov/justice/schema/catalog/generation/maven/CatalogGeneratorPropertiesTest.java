package uk.gov.justice.schema.catalog.generation.maven;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogGeneratorPropertiesTest {

    @InjectMocks
    private CatalogGeneratorProperties catalogGeneratorProperties;

    @Test
    public void shouldGetTheCatalogNameSetByMaven() throws Exception {

        final String catalogName = "my catalog";
        setProperty(catalogGeneratorProperties, "catalogName", catalogName);

        assertThat(catalogGeneratorProperties.getCatalogName(), is(catalogName));
    }

    @Test
    public void shouldGetTheJsonSchemaPathAsAPath() throws Exception {

        final String jsonSchemaPath = "json/schema/";

        setProperty(catalogGeneratorProperties, "jsonSchemaPath", jsonSchemaPath);

        assertThat(catalogGeneratorProperties.getJsonSchemaPath(), is(Paths.get(jsonSchemaPath)));
    }

    private void setProperty(final Object bean, final String propertyName, final Object propertyValue) throws Exception {

        final Field declaredField = bean.getClass().getDeclaredField(propertyName);
        declaredField.setAccessible(true);
        declaredField.set(bean, propertyValue);
    }
}
