package uk.gov.justice.generation.pojo.plugin.classmodifying.hashcode;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.io.FileInputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SerialVersionUIDGeneratorTest {

    @InjectMocks
    private SerialVersionUIDGenerator serialVersionUIDGenerator;

    @Test
    public void shouldGenerateAHashCodeFromASchemaWhilstFollowingAnyReferencedSchemaLinks() throws Exception {

        final File schemaFile = new ClasspathFileResource()
                .getFileFromClasspath("/schemas/hash-schema.json");

        final JSONObject rawSchema = new JSONObject(new JSONTokener(new FileInputStream(schemaFile)));
        final Schema schema = SchemaLoader.load(rawSchema);

        assertThat(serialVersionUIDGenerator.generateHashCode(schema), is(4596944059130132277L));
    }
}
