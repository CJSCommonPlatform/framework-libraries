package uk.gov.justice.services.test.utils.core.schema;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.FILE_PROTOCOL;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.JAR_PROTOCOL;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.JSON_SCHEMA_BASE_PATH;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.JSON_SCHEMA_FILE_EXTENSION;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.convertFileContentToLines;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.extractJarPath;
import static uk.gov.justice.services.test.utils.core.schema.SchemaDuplicateHelper.isJAR;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchemaDuplicateChecker {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaDuplicateChecker.class);

    private int totalSchemasFound = 0;
    private boolean isCompliant = false;
    private int numberOfViolatingSchemas = 0;

    /**
     * Returns true if the check found no schemas with different contents
     */
    public boolean isCompliant() {
        return isCompliant;
    }

    /**
     * Runs checks to identify schemas with the same filename but with different content across
     * modules
     */
    public boolean runDuplicateCheck() {
        this.isCompliant = false;
        this.totalSchemasFound = 0;
        this.numberOfViolatingSchemas = 0;

        //List all schema containers with a json/schema directory inside
        //These can be directories or Jar files
        List<File> schemaContainers = retrieveSchemaContainers();

        //read all files from the given directories
        final List<SchemaFile> schemaFilesList = listAllSchemaFilesFromContainers(schemaContainers);

        //sort by filename to improve experience when going through the list and fixing files
        //hashmaps will not maintain insertion ordering when iterating through keyset
        schemaFilesList.sort(Comparator.comparing(SchemaFile::getSchemaFileName));

        totalSchemasFound = schemaFilesList.size();

        //Identify schemas with the same name, count how many times they occur
        Map<String, Long> schemaOccurrenceMap = generateSchemaOccurrenceCount(schemaFilesList);


        //debug how many times does each file appear
        if (LOG.isDebugEnabled()) {
            schemaOccurrenceMap
                    .forEach((k, v) -> LOG.debug("{} - is present {} time(s) across modules", k, v));
        }

        //add to the list every schema that appears more than once
        List<String> duplicatedSchemasList =
                generateSchemaDuplicationList(schemaOccurrenceMap);

        if (duplicatedSchemasList.isEmpty()) {
            this.isCompliant = true;
            this.numberOfViolatingSchemas = 0;
            return this.isCompliant;
        }

        final Map<String, Set<String>> offenders = new HashMap<>();


        duplicatedSchemasList.forEach(e -> {
            Set<String> setOfDistinctVersions = schemaFilesList
                    .stream()
                    .filter(schemaEntry -> schemaEntry.getSchemaFileName().equals(e))
                    .map(SchemaFile::getPrettyPrintChecksum)
                    .distinct()
                    .collect(Collectors.toSet());


            if (setOfDistinctVersions.size() > 1) {
                LOG.error("{} has {} distinct versions", e, setOfDistinctVersions.size());
            } else {
                LOG.debug("{} is the same across modules", e);
            }
            if (setOfDistinctVersions.size() > 1) {
                offenders.put(e, setOfDistinctVersions);
            }
        });


        if (offenders.size() < 1) {
            this.isCompliant = true;
            LOG.info("Schema duplicates test passed. {} files checked in total", this.totalSchemasFound);
        } else {

            this.numberOfViolatingSchemas = offenders.size();
            this.isCompliant = false;
            final String report = generateReport(schemaFilesList, offenders);
            LOG.error(report);
        }
        return this.isCompliant;
    }

    /**
     * creates a list with schema files which appear more than once
     */
    private List<String> generateSchemaDuplicationList(Map<String, Long> schemaOccurrenceMap) {
        return schemaOccurrenceMap.entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(e -> e.getKey())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Creates a structure with the count of occurrences of each file
     */
    private Map<String, Long> generateSchemaOccurrenceCount(List<SchemaFile> schemaFilesList) {
        return schemaFilesList
                .stream()
                .map(SchemaFile::getSchemaFileName)
                .collect(groupingBy(Function.identity(), counting()));
    }

    /**
     * Lists all the schemas that it can find in all schema containers (directories or jars)
     */
    private List<SchemaFile> listAllSchemaFilesFromContainers(List<File> schemaContainers) {

        final List<SchemaFile> schemaFilesList = new ArrayList<>();

        //Discard all non .json URLs
        for (File container : schemaContainers) {
            if (isJAR(container)) {
                //Pull all the schema files from the directory in the JAR
                schemaFilesList.addAll(listSchemaFilesFromJar(container));
            } else {
                //Pull all the schema files from that directory
                schemaFilesList.addAll(listSchemaFilesFromDir(container));
            }
        }
        return schemaFilesList;
    }

    /**
     * Returns a structure will all schemas for that directory
     */
    List<SchemaFile> listSchemaFilesFromDir(File directory) {

        return listSchemaFilesFromDir(directory, directory);
    }

    /**
     * Returns a structure will all schemas for that directory
     *
     * @param baseContainer the container where the files are found
     */
    private List<SchemaFile> listSchemaFilesFromDir(File baseContainer, File directory) {

        List<File> files = asList(directory
                .listFiles(d -> d.getAbsolutePath().toLowerCase().endsWith(JSON_SCHEMA_FILE_EXTENSION) || d.isDirectory()));

        List<SchemaFile> returnList = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                returnList.addAll(listSchemaFilesFromDir(baseContainer, file));
            } else {
                SchemaFile schemaFile = new SchemaFile(baseContainer, file);
                try {
                    schemaFile.setOriginalFileContents(FileUtils.readFileToString(file));
                } catch (IOException e) {
                    throw new SchemaCheckerException(e);
                }
                returnList.add(schemaFile);
            }

        }
        return returnList;
    }

    /**
     * Lists all the schema containers (dirs and jars) which are likely to contain schemas from the
     * classpath
     */
    List<File> retrieveSchemaContainers() {
        Enumeration<URL> urlEnumeration = null;
        try {
            urlEnumeration = SchemaDuplicateChecker.class.getClassLoader().getResources(JSON_SCHEMA_BASE_PATH);
        } catch (IOException e) {
            throw new SchemaCheckerException(e);
        }
        List<File> schemaFileList = new ArrayList<>();

        while (urlEnumeration.hasMoreElements()) {
            final URL url = urlEnumeration.nextElement();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found this schema dir {}", url.toString());
            }
            final String protocol = url.getProtocol();

            File file = null;
            if (protocol.equalsIgnoreCase(JAR_PROTOCOL)) {
                String jarFilepath = extractJarPath(url);
                file = new File(jarFilepath);
            } else if (protocol.equalsIgnoreCase(FILE_PROTOCOL)) {
                try {
                    file = Paths.get(url.toURI()).toFile();
                } catch (URISyntaxException e) {
                    throw new SchemaCheckerException(e);
                }
            }
            schemaFileList.add(file);
        }
        return schemaFileList;
    }

    /**
     * Generates a report with the issues found
     */
    public String generateReport(List<SchemaFile> schemaFiles, Map<String, Set<String>> offenders) {

        StringBuilder sb = new StringBuilder();
        sb.append("Summary of offending schema files:\n\n");
        for (Map.Entry<String, Set<String>> entrySet : offenders.entrySet()) {
            String filename =entrySet.getKey();
            Set<String> setOfChecksums = entrySet.getValue();

            sb.append("******** ").append(filename).append(" ********\n");

            final List<Patch> patches = generatePatchListForChecksumSet(schemaFiles, setOfChecksums);

            int index = 0;
            for (String c : setOfChecksums) {
                //get all files with this checksum
                sb.append(format("   PrettyPrint's sha256: %s%n", c));
                List<SchemaFile> filesMatch = findSchemaFiles(schemaFiles, filename, c);
                filesMatch.forEach(schemaFile -> {

                    sb.append("   + ");
                    if (schemaFile.isInsideJar()) {
                        sb.append(format("[%s]", schemaFile.getContainerFile().getAbsolutePath()));
                        sb.append(schemaFile.getFile().getPath());
                    } else {
                        sb.append(schemaFile.getFile().getAbsolutePath());
                    }

                    sb.append("\n");
                });
                sb.append("\n");

                if (index + 1 < setOfChecksums.size()) {
                    List<Delta> deltas = patches.get(index++).getDeltas();

                    deltas.forEach(e -> sb.append(e.toString()).append("\n"));
                }

                sb.append("\n");
            }
            sb.append("\n\n");
        }
        return sb.toString();

    }

    /**
     * Generates a patch list for a given set of checksums
     */
    private List<Patch> generatePatchListForChecksumSet(final List<SchemaFile> schemaFiles, final Set<String> setOfChecksums) {
        final List<Patch> patches =
                new ArrayList<>();
        Iterator<String> it = setOfChecksums.iterator();
        String currentChecksum = null;
        String nextChecksum = null;
        while (it.hasNext()) {
            currentChecksum = nextChecksum;
            if (currentChecksum == null) {
                currentChecksum = it.next();
            }
            if (it.hasNext()) {
                nextChecksum = it.next();
            } else {
                break;
            }
            final String a = currentChecksum;
            final String b = nextChecksum;

            String contentsA = getOriginalFileContents(schemaFiles, a);
            String contentsB = getOriginalFileContents(schemaFiles, b);
            patches.add(DiffUtils.diff(convertFileContentToLines(contentsA),
                    convertFileContentToLines(contentsB)));

        }
        return patches;
    }

    /**
     * Finds all the schema files for the given filename and pretty print checksum
     */
    private List<SchemaFile> findSchemaFiles(final List<SchemaFile> schemaFiles, String filename, String prettyPrintChecksum) {
        return schemaFiles
                .stream()
                .filter(se -> se.getSchemaFileName().equals(filename) && se.getPrettyPrintChecksum().equals(prettyPrintChecksum))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the file contents matching the pretty print hash
     */
    private String getOriginalFileContents(List<SchemaFile> schemaFiles, String hash) {
        Optional<String> contents = schemaFiles.stream().filter(e -> e.getPrettyPrintChecksum().equals(hash)).map(SchemaFile::getOriginalFileContents).findAny();
        if (contents.isPresent()) {
            return contents.get();
        } else {
            return null;
        }
    }

    /**
     * Returns all schemas files found in the jar
     */
    public List<SchemaFile> listSchemaFilesFromJar(File jar) {

        try {
            final JarFile jarFile = new JarFile(jar);
            return listSchemaFilesFromJar(jar, jarFile);
        } catch (IOException e) {
            throw new SchemaCheckerException(e);
        }
    }

    /**
     * Returns all schemas files found in the jar
     */
    public List<SchemaFile> listSchemaFilesFromJar(final File jar, final JarFile jarFile) {
        try {
            return jarFile
                    .stream()
                    .filter(e -> (e.getName().startsWith(JSON_SCHEMA_BASE_PATH) && e.getName().toLowerCase().endsWith(JSON_SCHEMA_FILE_EXTENSION)))
                    .map((JarEntry e) -> {
                        File file = new File(e.getName());
                        SchemaFile schemaFile = new SchemaFile(jar, file);
                        try {
                            InputStream inputStream = jarFile.getInputStream(e);
                            if (inputStream == null) {
                                throw new SchemaCheckerException(format("Failed to read file %s inside jar %s", e.getName(), jar.getAbsolutePath()));
                            }
                            schemaFile.setOriginalFileContents(IOUtils.toString(inputStream));
                        } catch (IOException ioe) {
                            throw new SchemaCheckerException(ioe);
                        }
                        return schemaFile;
                    })
                    .collect(toList());
        } finally {
            IOUtils.closeQuietly(jarFile);
        }
    }

    /**
     * Gets the total amount of schemas found during the check execution
     */
    public int getTotalSchemasFound() {
        return this.totalSchemasFound;
    }


    /**
     * Gets the number of unique schema files (filename based) found containing different schemas
     */
    public int getNumberOfViolatingSchemas() {
        return numberOfViolatingSchemas;
    }
}
