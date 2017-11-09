package uk.gov.justice.services.test.utils.core.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchemaComplianceCheckerTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");
    }

    private static final Logger LOG = LoggerFactory.getLogger(SchemaComplianceCheckerTest.class);

    @Test
    public void testextractJarPath() throws Exception {
        SchemaComplianceChecker scc = new SchemaComplianceChecker();

        //normal conditions
        assertEquals("file:/a/a/a/a.jar", scc.extractJarPath(new URL("jar:file:/a/a/a/a.jar!/json/schema/")));

        //error conditions

        //missing the trailing /
        assertNull(scc.extractJarPath(new URL("jar:file:/a/a/a/a.jar!/json/schema")));

        assertNull(scc.extractJarPath(new URL("file:/a/a/a/a.jar!/json/schema")));
    }

    @Test
    public void testExecuteComplianceCheck_shouldPassWithNoErrorsSimpleFileSystem() throws Exception {

        final String schemaDir1 = "file:/a/a/a/a/json/schema/";
        final String schemaDir2 = "file:/b/b/b/b/json/schema/";
        final String schemaDir3 = "file:/c/c/c/c/json/schema/";


        final URL schemaURL1 = new URL(schemaDir1);
        final URL schemaURL2 = new URL(schemaDir2);
        final URL schemaURL3 = new URL(schemaDir3);

        final Enumeration<URL> urlsToReadSchemasFrom = new ConstantsEnumeration<URL>(schemaURL1, schemaURL2, schemaURL3);

        //common A is repeated three times and all of them are different
        //common B is repeated twice and they are the same
        //specific only appears once

        final String fileCommonA1 = "This is file A";
        final String fileCommonA2 = "This is file A with revisions";
        final String fileCommonA3 = "This is file the new file A";

        final String fileCommonB1 = "This is file B";
        final String fileCommonB2 = "This is file B";

        final String fileSpecificA = "This is file specific A";
        final String fileSpecificB = "This is file specific B";
        final String fileSpecificC = "This is file specific C";

        final SchemaEntryTestVO schemaEntry11 = new SchemaEntryTestVO(new URL("file:/a/a/a/a/json/schema/commonA.json"), "commonA.json", fileCommonA1); //fileCommonA1
        final SchemaEntryTestVO schemaEntry12 = new SchemaEntryTestVO(new URL("file:/a/a/a/a/json/schema/specificA.json"), "specificA.json", fileSpecificA);
        final SchemaEntryTestVO schemaEntry13 = new SchemaEntryTestVO(new URL("file:/a/a/a/a/json/schema/commonB.json"), "commonB.json", fileCommonB1);

        final SchemaEntryTestVO schemaEntry21 = new SchemaEntryTestVO(new URL("file:/b/b/b/b/json/schema/commonA.json"), "commonA.json", fileCommonA2);
        final SchemaEntryTestVO schemaEntry22 = new SchemaEntryTestVO(new URL("file:/b/b/b/b/json/schema/commonB.json"), "commonB.json", fileCommonB2);
        final SchemaEntryTestVO schemaEntry23 = new SchemaEntryTestVO(new URL("file:/b/b/b/b/json/schema/specificB.json"), "specificB.json", fileSpecificB);

        final SchemaEntryTestVO schemaEntry31 = new SchemaEntryTestVO(new URL("file:/c/c/c/c/json/schema/commonA.json"), "commonA.json", fileCommonA3);
        final SchemaEntryTestVO schemaEntry32 = new SchemaEntryTestVO(new URL("file:/c/c/c/c/json/schema/specificC.json"), "specificC.json", fileSpecificC);

        final List<SchemaEntry> schemaEntriesForDir1 = Arrays.asList(new SchemaEntry[]{schemaEntry11, schemaEntry12, schemaEntry13});
        final List<SchemaEntry> schemaEntriesForDir2 = Arrays.asList(new SchemaEntry[]{schemaEntry21, schemaEntry22, schemaEntry23});
        final List<SchemaEntry> schemaEntriesForDir3 = Arrays.asList(new SchemaEntry[]{schemaEntry31, schemaEntry32});

        SchemaComplianceChecker scc = spy(SchemaComplianceChecker.class);


        doReturn(urlsToReadSchemasFrom).when(scc).getSchemaFolderURLs();

        doReturn(schemaEntriesForDir1).when(scc).grabFiles(schemaURL1);
        doReturn(schemaEntriesForDir2).when(scc).grabFiles(schemaURL2);
        doReturn(schemaEntriesForDir3).when(scc).grabFiles(schemaURL3);


        mockReadFunction(schemaEntry11, scc);
        mockReadFunction(schemaEntry12, scc);
        mockReadFunction(schemaEntry13, scc);

        mockReadFunction(schemaEntry21, scc);
        mockReadFunction(schemaEntry22, scc);
        mockReadFunction(schemaEntry23, scc);

        mockReadFunction(schemaEntry31, scc);
        mockReadFunction(schemaEntry32, scc);

        scc.runComplianceCheck();

        assertEquals(schemaEntry11.getFileContentTestCalculatedHash(), schemaEntry11.getChecksum());
        assertEquals(schemaEntry12.getFileContentTestCalculatedHash(), schemaEntry12.getChecksum());
        assertEquals(schemaEntry13.getFileContentTestCalculatedHash(), schemaEntry13.getChecksum());

        assertEquals(schemaEntry21.getFileContentTestCalculatedHash(), schemaEntry21.getChecksum());
        assertEquals(schemaEntry22.getFileContentTestCalculatedHash(), schemaEntry22.getChecksum());
        assertEquals(schemaEntry23.getFileContentTestCalculatedHash(), schemaEntry23.getChecksum());

        assertEquals(schemaEntry31.getFileContentTestCalculatedHash(), schemaEntry31.getChecksum());
        assertEquals(schemaEntry32.getFileContentTestCalculatedHash(), schemaEntry32.getChecksum());


        assertEquals(8, scc.getTotalSchemasChecked());
        assertEquals(1, scc.getNumberOfViolatingSchemas());
        assertEquals(false, scc.isCompliant());

    }


    @Test
    public void testExecuteComplianceCheck_simpleFilesDryRun() throws Exception {

        URL schemaURL = SchemaComplianceChecker.class.getClassLoader().getResource("json/schema/myschema.json");
        String s = schemaURL.toString();
        URL baseURL = new URL(s.substring(0, s.lastIndexOf("myschema.json")));

        final Enumeration<URL> urlsToReadSchemasFrom = new ConstantsEnumeration<URL>(baseURL);

        SchemaComplianceChecker scc = spy(SchemaComplianceChecker.class);
        doReturn(urlsToReadSchemasFrom).when(scc).getSchemaFolderURLs();

        scc.runComplianceCheck();


        assertEquals(2, scc.getTotalSchemasChecked());
        assertEquals(0, scc.getNumberOfViolatingSchemas());
        assertEquals(true, scc.isCompliant());
    }


    @Test
    public void testExecuteComplianceCheck_shouldPassWithNoErrorsJARS() throws Exception {

        final String schemaDir1 = "jar:file:/a/a/a/a.jar!/json/schema/";
        final String schemaDir2 = "jar:file:/b/b/b/b.jar!/json/schema/";
        final String schemaDir3 = "jar:file:/c/c/c/c.jar!/json/schema/";


        final URL schemaURL1 = new URL(schemaDir1);
        final URL schemaURL2 = new URL(schemaDir2);
        final URL schemaURL3 = new URL(schemaDir3);

        final Enumeration<URL> urlsToReadSchemasFrom = new ConstantsEnumeration<URL>(schemaURL1, schemaURL2, schemaURL3);

        //common A is repeated three times and all of them are different
        //common B is repeated twice and they are the same
        //specific only appears once

        final String fileCommonA1 = "This is file A";
        final String fileCommonA2 = "This is file A with revisions";
        final String fileCommonA3 = "This is file the new file A";

        final String fileCommonB1 = "This is file B";
        final String fileCommonB2 = "This is file B";

        final String fileSpecificA = "This is file specific A";
        final String fileSpecificB = "This is file specific B";
        final String fileSpecificC = "This is file specific C";

        final SchemaEntryTestVO schemaEntry11 = new SchemaEntryTestVO(new URL("jar:file:/a/a/a/a.jar!/json/schema/common.json"), "commonA.json", fileCommonA1); //fileCommonA1
        final SchemaEntryTestVO schemaEntry12 = new SchemaEntryTestVO(new URL("jar:file:/a/a/a/a.jar!/json/schema/specificA.json"), "specificA.json", fileSpecificA);
        final SchemaEntryTestVO schemaEntry13 = new SchemaEntryTestVO(new URL("jar:file:/a/a/a/a.jar!/json/schema/commonB.json"), "commonB.json", fileCommonB1);

        final SchemaEntryTestVO schemaEntry21 = new SchemaEntryTestVO(new URL("jar:file:/b/b/b/b.jar!/json/schema/commonA.json"), "commonA.json", fileCommonA2);
        final SchemaEntryTestVO schemaEntry22 = new SchemaEntryTestVO(new URL("jar:file:/b/b/b/b.jar!/json/schema/commonB.json"), "commonB.json", fileCommonB2);
        final SchemaEntryTestVO schemaEntry23 = new SchemaEntryTestVO(new URL("jar:file:/b/b/b/b.jar!/json/schema/specificB.json"), "specificB.json", fileSpecificB);

        final SchemaEntryTestVO schemaEntry31 = new SchemaEntryTestVO(new URL("jar:file:/c/c/c/c.jar!/json/schema/commonA.json"), "commonA.json", fileCommonA3);
        final SchemaEntryTestVO schemaEntry32 = new SchemaEntryTestVO(new URL("jar:file:/c/c/c/c.jar!/json/schema/specificC.json"), "specificC.json", fileSpecificC);

        final List<SchemaEntry> schemaEntriesForDir1 = Arrays.asList(new SchemaEntry[]{schemaEntry11, schemaEntry12, schemaEntry13});
        final List<SchemaEntry> schemaEntriesForDir2 = Arrays.asList(new SchemaEntry[]{schemaEntry21, schemaEntry22, schemaEntry23});
        final List<SchemaEntry> schemaEntriesForDir3 = Arrays.asList(new SchemaEntry[]{schemaEntry31, schemaEntry32});

        SchemaComplianceChecker scc = spy(SchemaComplianceChecker.class);


        doReturn(urlsToReadSchemasFrom).when(scc).getSchemaFolderURLs();

        doReturn(schemaEntriesForDir1).when(scc).grabFilesFromJarInClasspath(schemaURL1);
        doReturn(schemaEntriesForDir2).when(scc).grabFilesFromJarInClasspath(schemaURL2);
        doReturn(schemaEntriesForDir3).when(scc).grabFilesFromJarInClasspath(schemaURL3);


        mockReadFunction(schemaEntry11, scc);
        mockReadFunction(schemaEntry12, scc);
        mockReadFunction(schemaEntry13, scc);

        mockReadFunction(schemaEntry21, scc);
        mockReadFunction(schemaEntry22, scc);
        mockReadFunction(schemaEntry23, scc);

        mockReadFunction(schemaEntry31, scc);
        mockReadFunction(schemaEntry32, scc);

        scc.runComplianceCheck();

        assertEquals(schemaEntry11.getFileContentTestCalculatedHash(), schemaEntry11.getChecksum());
        assertEquals(schemaEntry12.getFileContentTestCalculatedHash(), schemaEntry12.getChecksum());
        assertEquals(schemaEntry13.getFileContentTestCalculatedHash(), schemaEntry13.getChecksum());

        assertEquals(schemaEntry21.getFileContentTestCalculatedHash(), schemaEntry21.getChecksum());
        assertEquals(schemaEntry22.getFileContentTestCalculatedHash(), schemaEntry22.getChecksum());
        assertEquals(schemaEntry23.getFileContentTestCalculatedHash(), schemaEntry23.getChecksum());

        assertEquals(schemaEntry31.getFileContentTestCalculatedHash(), schemaEntry31.getChecksum());
        assertEquals(schemaEntry32.getFileContentTestCalculatedHash(), schemaEntry32.getChecksum());


        assertEquals(8, scc.getTotalSchemasChecked());
        assertEquals(1, scc.getNumberOfViolatingSchemas());
        assertEquals(false, scc.isCompliant());

    }

    public static void mockReadFunction(SchemaEntryTestVO schemaEntry, SchemaComplianceChecker scc) {
        doReturn(schemaEntry.getFileContents()).when(scc).readURL(schemaEntry.getUrl());
    }


}
