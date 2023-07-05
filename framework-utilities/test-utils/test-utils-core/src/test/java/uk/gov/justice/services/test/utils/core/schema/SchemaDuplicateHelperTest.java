package uk.gov.justice.services.test.utils.core.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SchemaDuplicateHelperTest {

    @Test
    public void testExtractJarPath_normal() throws MalformedURLException {
        String jarClasspathURL = "jar:file:/c/c/c/c.jar!/json/schema/";
        URL url = new URL(jarClasspathURL);

        assertEquals("/c/c/c/c.jar", SchemaDuplicateHelper.extractJarPath(url));
    }

    @Test
    public void testExtractJarPath_errorConditions1() throws MalformedURLException {
        try {
            assertNull(SchemaDuplicateHelper.extractJarPath(new URL("jar:file:/c/c/c/c.ja!/json/schema/")));
            fail();
        } catch (final SchemaCheckerException expected) {
        }
    }

    @Test
    public void testExtractJarPath_errorConditions2() throws MalformedURLException {
        try {
            assertNull(SchemaDuplicateHelper.extractJarPath(new URL("file:/c/c/c/c.jar!/json/schema/")));
            fail();
        } catch (final SchemaCheckerException expected) {
        }
    }

    @Test
    public void testExtractJarPath_errorConditions3() throws MalformedURLException {
        try {
            assertNull(SchemaDuplicateHelper.extractJarPath(new URL("jar:file:/c/c/c/c.jara!/json/schema/")));
            fail();
        } catch (final SchemaCheckerException expected) {
        }
    }

    @Test
    public void testConvertFileContentToLines() {
        String a = "AAA";
        String b = "BBB";

        String unix = a + "\n" + b;
        String windows = a + "\r\n" + b;

        List<String> unixLines = SchemaDuplicateHelper.convertFileContentToLines(unix);
        List<String> windowsLines = SchemaDuplicateHelper.convertFileContentToLines(windows);

        assertEquals(2, unixLines.size());
        assertEquals(2, windowsLines.size());

        assertEquals(a, unixLines.get(0));
        assertEquals(b, unixLines.get(1));

        assertEquals(a, windowsLines.get(0));
        assertEquals(b, windowsLines.get(1));

    }

}
