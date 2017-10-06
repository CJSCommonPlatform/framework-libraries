package uk.gov.justice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

import org.everit.json.schema.loader.SchemaClient;

public class CPPSchemaClient implements SchemaClient {

    private static SchemaDictionary schemaDictionary = new SchemaDictionary();

    @Override
    public InputStream get(String url) {

        System.out.println("requested to retrieve url: " + url);

        return schemaDictionary.get(url);
    }

    public void loadSchemas(String path) throws FileNotFoundException {


            loadSchema(path);

    }

    private void loadSchema(String file) {
        try {
            schemaDictionary.loadSchema(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
