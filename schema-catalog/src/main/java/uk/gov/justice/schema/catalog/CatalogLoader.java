package uk.gov.justice.schema.catalog;

import static java.lang.String.format;

import uk.gov.justice.schema.catalog.pojo.Catalog;
import uk.gov.justice.schema.catalog.pojo.CatalogWrapper;
import uk.gov.justice.schema.catalog.pojo.Group;
import uk.gov.justice.schema.catalog.pojo.Schema;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CatalogLoader {

    private static final String JSON_CATALOG_LOCATION = "json/schema/schema_catalog.json";

    private static final Logger LOG = LoggerFactory.getLogger(CatalogLoader.class);
    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";
    private static final String PROTOCOL_VFS = "vfs";


    private final ObjectMapperProducer objectMapperProducer = new ObjectMapperProducer();

    @Inject
    SchemaLoader schemaLoader;

    protected List<URL> listAllCatalogsFromClasspath() {
        Enumeration<URL> urlEnumeration = null;
        List<URL> urls = null;

        LOG.info(format("Listing all schema definition files matching: %s", JSON_CATALOG_LOCATION));

        try {
            urlEnumeration = CatalogLoader.class.getClassLoader().getResources(JSON_CATALOG_LOCATION);
            urls = enumTOList(urlEnumeration);

        } catch (IOException e) {
            LOG.error("Failed to list the catalogs from the classpath", e);

        }
        return urls;
    }


    public void loadCatalogsFromClasspath() {
        //list all catalogs
        List<URL> catalogURLs = listAllCatalogsFromClasspath();

        if (catalogURLs == null || catalogURLs.isEmpty()) {
            LOG.info("No catalog files found in the classpath");
            return;

        }

        loadCatalogs(catalogURLs);
    }

    public void loadCatalog(URL url) {
        List<URL> list = new ArrayList<URL>();
        list.add(url);
        loadCatalogs(list);
    }

    protected void loadCatalogs(List<URL> catalogURLs) {

        if (catalogURLs == null || catalogURLs.isEmpty()) {
            LOG.info("No catalog files found in the classpath");
            return;

        }

        //read catalogs and create a dictionary of POJOs
        Map<URL, Catalog> catalogPojoMap = readCatalogsAndValidate(catalogURLs);

        if (catalogPojoMap.isEmpty()) {
            LOG.info("No catalogs were read");
            return;
        }

        Map<String, URL> schemaLocationMap = new HashMap<>();
        for (URL url : catalogPojoMap.keySet()) {
            Catalog catalog = catalogPojoMap.get(url);
            LOG.info("URL (\"{}\") defines catalog (\"{}\")", url.toString(), catalog.getName());

            LOG.info("Protocol of the url is: {}", url.getProtocol());
            LOG.info("{}", url.getFile());


            for (Group group : catalog.getGroup()) {
                LOG.info("Catalog {} defines {}", catalog.getName(), group.getName());

                //base location for the groups
                String baseLocation = group.getBaseLocation();
                boolean isBaseLocationDefined = true;
                //not doing much with this variable here
                if (baseLocation == null || baseLocation.equals("")) {
                    isBaseLocationDefined = false;
                }
                for (Schema schema :
                        group.getSchema()) {

                    LOG.info("Found schema catalog definition id: {}", schema.getId());

                    //TODO
                    //(Feature) Translate the URL to an absolute URL
                    //URL newURL = new URL()
                    URL resolvedURL = null;
                    try {
                        //TODO fix the resolution of the urls
                        resolvedURL = resolveLocation(url, group.getBaseLocation(), schema.getLocation());
                    } catch (MalformedURLException e) {
                        LOG.error(format("Failed to generate url for %s", url.toString(), baseLocation, schema.getLocation()), e);
                        break;
                    }


                    schemaLocationMap.put(schema.getId(), resolvedURL);

                }
            }
        }


        //obtain the id -> jar + location mapping for each schema


        schemaLoader.bulkLoadSchemas(schemaLocationMap);

        //load schema


    }


    private URL resolveLocation(URL catalogUrl, String baseLocation, String location) throws MalformedURLException {

        URL url = null;

        boolean ignoreBaseLocation = false;
        if (baseLocation == null || baseLocation.equals("")) {
            ignoreBaseLocation = true;
        }

        File file = new File(location);
        if (file.getAbsolutePath().equals(location)) {
            ignoreBaseLocation = true;
        }

        if (!ignoreBaseLocation) {
            file = new File(baseLocation, location);
        }

        if (catalogUrl.getProtocol().toLowerCase().equals(PROTOCOL_FILE)) {
            url = new URL(PROTOCOL_FILE + ":" + file.getAbsolutePath());
        } else if (catalogUrl.getProtocol().toLowerCase().equals(PROTOCOL_JAR)) {
            String jarFilename = extractJarName(catalogUrl.getFile());
            url = new URL(PROTOCOL_JAR + ":" + PROTOCOL_FILE + ":" + jarFilename + "!" + file.getAbsolutePath());
        } else if (catalogUrl.getProtocol().toLowerCase().equals(PROTOCOL_VFS)) {

            String jarFilename = extractVFSContextName(catalogUrl.getFile());
            url = new URL(PROTOCOL_VFS + ":" + jarFilename + file.getAbsolutePath());
        } else {
            return new URL("localhost:80892/UNDEFINED");
        }

        LOG.info("URL translated into {}", url.toString());

        return url;
    }

    private String extractVFSContextName(String file) {
        final String searchIndex = ".jar";

        int start = 0;
        int end = file.toLowerCase().indexOf(searchIndex, start);
        return file.substring(start, end + searchIndex.length());
    }

    public static String extractJarName(String jarURL) {
        final String searchIndex = PROTOCOL_FILE + ":";

        int start = jarURL.toLowerCase().indexOf(searchIndex);
        int end = jarURL.indexOf('!', start);
        return jarURL.substring(start + searchIndex.length(), end);

    }

    protected Map<URL, Catalog> readCatalogsAndValidate(List<URL> catalogURLs) {
        Map<URL, Catalog> map = new HashMap<>();

        for (URL url : catalogURLs) {

            String jsonString = null;
            InputStream in = null;
            try {
                LOG.info("Attempting to read file {}", url);
                in = url.openStream();
                jsonString = IOUtils.toString(in, Charset.defaultCharset());

                //File file = new File(url.toURI());
                //jsonString = FileUtils.readFileToString(file, Charset.defaultCharset());

            } catch (IOException e) {
                LOG.error(format("Failed to read %s", url.toString()), e);
                break;
            } finally {
                IOUtils.closeQuietly(in);
            }
            try {
                if (jsonString != null) {
                    Catalog catalog = objectMapperProducer.objectMapper().readValue(jsonString, CatalogWrapper.class).getCatalog();

                    //add the validation step here
                    map.put(url, catalog);
                }
            } catch (IOException e) {
                LOG.error(format("Failed to convert to a Catalog Pojo: %s", jsonString), e);
            }


        }

        return map;
    }

    /**
     * Validates the catalogs and returns a list of validated catalogs
     */
    private List<String> validateCatalogs(List<String> catalogJsonContents) {
        return catalogJsonContents;

    }

    private <T> List<T> enumTOList(Enumeration<T> e) {
        List<T> list = new ArrayList<T>();
        while (e.hasMoreElements())
            list.add(e.nextElement());
        return list;
    }


    public static void main(String args[]) throws URISyntaxException

    {
        CatalogLoader catalogLoader = new CatalogLoader();
        catalogLoader.loadCatalogsFromClasspath();
        List<URL> urls = catalogLoader.listAllCatalogsFromClasspath();
        Paths.get(urls.get(0).toURI()).getFileName();
    }

}
