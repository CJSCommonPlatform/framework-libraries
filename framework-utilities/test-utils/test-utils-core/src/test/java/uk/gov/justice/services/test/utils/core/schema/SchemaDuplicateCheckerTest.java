package uk.gov.justice.services.test.utils.core.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.extractJarPath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchemaDuplicateCheckerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaDuplicateCheckerTest.class);

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    @Test
    public void testExtractJarPath() throws Exception {
        SchemaDuplicateChecker scc = new SchemaDuplicateChecker();

        //normal conditions
        assertEquals("/a/a/a/a.jar", extractJarPath(new URL("jar:file:/a/a/a/a.jar!/json/schema/")));

    }

    @Test
    public void testExecuteComplianceCheck_shouldPassWithNoErrorsSimpleFileSystem() throws Exception {

        final String schemaDir1 = "/a/a/a/a/json/schema/";
        final String schemaDir2 = "/b/b/b/b/json/schema/";
        final String schemaDir3 = "/c/c/c/c/json/schema/";

        final String urlSchemaDir1 = "file:/a/a/a/a/json/schema/";
        final String urlSchemaDir2 = "file:/b/b/b/b/json/schema/";
        final String urlSchemaDir3 = "file:/c/c/c/c/json/schema/";

        final URL schemaURL1 = new URL(urlSchemaDir1);
        final URL schemaURL2 = new URL(urlSchemaDir2);
        final URL schemaURL3 = new URL(urlSchemaDir3);

        final File schemaFileDir1 = new File(schemaDir1);
        final File schemaFileDir2 = new File(schemaDir2);
        final File schemaFileDir3 = new File(schemaDir3);

        final List<File> dirsToReadSchemasFrom = Arrays.asList(schemaFileDir1, schemaFileDir2, schemaFileDir3);

        //common A is repeated three times and all of them are different
        //common B is repeated twice and they are the same
        //specific only appears once

        final String fileCommonA1 = "{\"fileA\":\"This is file A\"}";
        final String fileCommonA2 = "{\"fileA\":\"This is file A with revisions\"}";
        final String fileCommonA3 = "{\"fileA\":\"This is file the new file A\"}";

        final String fileCommonB1 = "{\"fileB\":\"This is file B\"}";
        final String fileCommonB2 = "{\"fileB\":\"This is file B\"}";

        final String fileSpecificA = "{\"specificA\":\"This is file specific A\"}";
        final String fileSpecificB = "{\"specificB\":\"This is file specific B\"}";
        final String fileSpecificC = "{\"specificC\":\"This is file specific C\"}";

        final SchemaFile schemaEntry11 = new SchemaFile(schemaFileDir1, new File("commonA.json"), fileCommonA1);
        final SchemaFile schemaEntry12 = new SchemaFile(schemaFileDir1, new File("specificA.json"), fileSpecificA);
        final SchemaFile schemaEntry13 = new SchemaFile(schemaFileDir1, new File("commonB.json"), fileCommonB1);

        final SchemaFile schemaEntry21 = new SchemaFile(schemaFileDir2, new File("commonA.json"), fileCommonA2);
        final SchemaFile schemaEntry22 = new SchemaFile(schemaFileDir2, new File("commonB.json"), fileCommonB2);
        final SchemaFile schemaEntry23 = new SchemaFile(schemaFileDir2, new File("specificB.json"), fileSpecificB);

        final SchemaFile schemaEntry31 = new SchemaFile(schemaFileDir3, new File("commonA.json"), fileCommonA3);
        final SchemaFile schemaEntry32 = new SchemaFile(schemaFileDir3, new File("specificC.json"), fileSpecificC);


        final List<SchemaFile> schemaEntriesForDir1 = Arrays.asList(new SchemaFile[]{schemaEntry11, schemaEntry12, schemaEntry13});
        final List<SchemaFile> schemaEntriesForDir2 = Arrays.asList(new SchemaFile[]{schemaEntry21, schemaEntry22, schemaEntry23});
        final List<SchemaFile> schemaEntriesForDir3 = Arrays.asList(new SchemaFile[]{schemaEntry31, schemaEntry32});

        SchemaDuplicateChecker scc = spy(SchemaDuplicateChecker.class);

        doReturn(dirsToReadSchemasFrom).when(scc).retrieveSchemaContainers();

        doReturn(schemaEntriesForDir1).when(scc).listSchemaFilesFromDir(schemaFileDir1);
        doReturn(schemaEntriesForDir2).when(scc).listSchemaFilesFromDir(schemaFileDir2);
        doReturn(schemaEntriesForDir3).when(scc).listSchemaFilesFromDir(schemaFileDir3);

        scc.runDuplicateCheck();


        assertEquals(8, scc.getTotalSchemasFound());
        assertEquals(1, scc.getNumberOfViolatingSchemas());
        assertEquals(false, scc.isCompliant());

    }

    @Test
    public void test_loadJARs() throws IOException {

        final String schemaDir1 = "/a/a/a/a.jar";

        final File schemaFileJar1 = new File(schemaDir1);

        JarFile mockJarFile = mock(JarFile.class);

        SchemaDuplicateChecker scc = spy(SchemaDuplicateChecker.class);

        JarEntry jarEntry1 = new JarEntry("json/schema/commonA.json");
        JarEntry jarEntry2 = new JarEntry("json/schema/commonB.json");
        JarEntry jarEntry3 = new JarEntry("json/schema/other/commonA.json");
        JarEntry jarEntry4 = new JarEntry("json/schema/");
        JarEntry jarEntry5 = new JarEntry("json/my.json");

        final String fileCommonA1 = "{\"fileA\":\"This is file A\"}";
        final String fileCommonA2 = "{\"fileA\":\"This is file A with revisions\"}";
        final String fileCommonB1 = "{\"fileB\":\"This is file B\"}";


        doReturn(Stream.of(jarEntry1, jarEntry2, jarEntry3, jarEntry4, jarEntry5)).when(mockJarFile).stream();

        doReturn(IOUtils.toInputStream(fileCommonA1)).when(mockJarFile).getInputStream(jarEntry1);
        doReturn(IOUtils.toInputStream(fileCommonA2)).when(mockJarFile).getInputStream(jarEntry3);
        doReturn(IOUtils.toInputStream(fileCommonB1)).when(mockJarFile).getInputStream(jarEntry2);


        List<SchemaFile> schemaFiles = scc.listSchemaFilesFromJar(schemaFileJar1, mockJarFile);

        assertEquals(3, schemaFiles.size());
    }

    @Test
    public void test_dryRun() throws IOException {

        SchemaDuplicateChecker scc = new SchemaDuplicateChecker();

        scc.runDuplicateCheck();
        assertFalse(scc.isCompliant());
        assertEquals(4, scc.getTotalSchemasFound());
        assertEquals(1, scc.getNumberOfViolatingSchemas());

    }
}