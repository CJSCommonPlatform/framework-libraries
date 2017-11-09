package uk.gov.justice.schema.catalog.generator;

import uk.gov.justice.generation.SchemaPojoGeneratorFactory;
import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GenerateCatalogPojos {


    public static void main(String args[]) {
        SchemaPojoGeneratorFactory schemaPojoGeneratorFactory = new SchemaPojoGeneratorFactory();
        Generator<File> generator = schemaPojoGeneratorFactory.create();

        System.out.println(Paths.get("json/schema/").toAbsolutePath());

        List<Path> sourcePaths = new ArrayList<Path>();
        sourcePaths.add(Paths.get("schema-catalog/src/main/resources/json/schema/"));

        PojoGeneratorProperties pojoGeneratorProperties = new PojoGeneratorProperties();

        GeneratorConfig config = new GeneratorConfig(Paths.get("schema-catalog/src/main/resources/json/schema/"), Paths.get("schema-catalog/src/main/java"), "uk.gov.justice.schema.catalog.pojo", pojoGeneratorProperties, sourcePaths);


        generator.run(Paths.get("schema-catalog/json/schema/json_catalog.schema.json").toFile(), config);

      //  new Schema.Builder().withId("bla").withLocation("").build();
       // new Group("", "", null);
        //new Catalog("",)


    }
}
