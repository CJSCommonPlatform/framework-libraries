# Json Schema Catalog
This library is intended as a json version of XML Catalogs: 
https://www.oasis-open.org/committees/entity/spec-2001-08-06.html

## Overview
There are two distinct parts to the Json Schema Catalog project:

* Schema Catalog Generation Maven plugin - generates schema_catalog.json file from a set of JSON 
Schema files.
* JSON Validation using a single or multiple schema_catalog.json files provided on the class path 
to resolve schema ids.

The JSON schema id provides a unique identification for a schema and allows a $ref inside a schema to 
be resolved to a specific schema at the given URI.  This project intervenes on the normal resolving 
of the $ref and instead resolves the schema id with a schema catalog file.

### Schema Catalog Generation
Json Schema Catalog is a Maven Plugin which allows easy inclusion of references in json schema 
documents to other json schemas, without the need to host the fragment schemas on a server, and/or 
to solve the problems of paths if the fragment schemas are stored on a file system. This allows for 
common standard schema definitions like *address* or *UUID* to be commonly used in all json schema 
documents and for these fragment schemas to be shared as jars.

Json Schema Catalog works by building a catalog of all json schemas found at a known location on the
 classpath and mapping the actual location (on the classpath) to the id of the json schema file.

For example: given a simple schema *person-schema.json*

    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "id": "http://justice.gov.uk/domain/person-schema.json",
      "type": "object",
      "properties": {
        "firstName": {
          "type": "string"
        },
        "lastName": {
          "type": "string"
        },
        "address": {
          "$ref": "http://justice.gov.uk/standard/fragments/address.json"
       }
      }
    }

Which defines a schema fragment *address.json* using the url http://justice.gov.uk/standard/fragments/address.json 
(which is also the id of address.json)

    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "id": "http://justice.gov.uk/standard/fragments/address.json",
      "type": "object",
        "properties": {
          "addressline1": {
            "type": "string"
        },
        "addressline2": {
          "type": "string"
        },
        "city": {
          "type": "string"
        },
        "postcode": {
          "type": "string"
        }
      }
    }
        
Normally, when person-schema.json is used to validate against a json document, whichever library you 
are using to handle json schema (we are using the excellent 
[Everit Json Schema Validator](https://github.com/everit-org/json-schema)), would load the fragment 
schema from the URI defined in the reference. i.e. http://justice.gov.uk/standard/fragments/address.json.

Instead, we build a catalog of all json schemas found on the classpath (at a known location) which 
maps the id of the schema against it's base location and its path in that location:

    {
      "groups": [
        {
          "baseLocation": "json/schema/standards/",
          "name": "standards",
          "schemas": [
            {
              "id": "http://justice.gov.uk/standards/address.json",
              "location": "address.json"
            },
            {
              "id": "http://justice.gov.uk/standards/complex_address.json",
              "location": "complex_address.json"
            }
          ]
        },
        {
          "baseLocation": "json/schema/context/",
          "name": "context",
          "schemas": [
            {
              "id": "http://justice.gov.uk/context/person.json",
              "location": "person.json"
            }
          ]
        }
      ],
      "name": "schema-service"
    }
        
So, in our example we would map the address.json fragment id 
http://justice.gov.uk/standard/fragments/address.json against it's actual location 
json/schema/standards/address.json.

When a json document is validated, we override the call to the fragment id url and instead return 
an InputStream to the actual fragment schema file which maybe within a dependent jar.

For this to work, each schema file *must* define an id and that id *must* match the reference given 
in the main json schema document.

### Plugin Usage
Add the following to your plugin declaration (edited to taste):

    <plugin>
        <artifactId>generator-plugin</artifactId>
        <groupId>uk.gov.justice.maven.generator</groupId>
        <version>2.3.0</version>
        <executions>
            <execution>
                <id>schema-catalog-generation</id>
                <configuration>
                    <generatorName>uk.gov.justice.schema.catalog.generation.maven.MavenCatalogGeneratorFactory</generatorName>
                    <parserName>uk.gov.justice.schema.catalog.generation.io.parser.ListOfUriParser</parserName>
                    <sourceDirectory>src/json/schema</sourceDirectory>
                    <outputDirectory>${project.build.directory}/generated-resources</outputDirectory>
                    <includes>
                        <include>**/*.json</include>
                    </includes>
                    <excludes></excludes>
                    <generatorProperties implementation="uk.gov.justice.schema.catalog.generation.maven.CatalogGeneratorProperties">
                        <catalogName>${project.artifactId}</catalogName>
                        <jsonSchemaPath>json/schema/</jsonSchemaPath>
                    </generatorProperties>
                </configuration>
                <goals>
                    <goal>generate</goal>
                </goals>
                <phase>generate-sources</phase>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>uk.gov.justice.schema</groupId>
                <artifactId>catalog-generation</artifactId>
                <version>1.0.1</version>
            </dependency>
        </dependencies>
    </plugin>
                    
The relevant parts of this plugin definition are:

* generatorName:       Plugin schema catalog generator factory implementation
* parserName:          Parser implementation to provide list of file URIs
* sourceDirectory:     the location of your json schemas 
* outputDirectory:     the location to output the generated schema catalog
* includes:            the included files
* excludes:            the excluded files
* generatorProperties: the schema catalog implementation properties
* catalogName:         the name to be used for this schema catalog
* jsonSchemaPath:      the base path to the schema files - keeping in mind that the schema_catalog.json 
will be created inside the META-INF

#### Catalog File

The Schema Catalog file is generated in the **META-INF** directory, as **schema_catalog.json**

All schemas referenced will be relative to the base of the jar or war


## JSON Validation

### Schema Catalog Service
A JEE 7 bean has been provided to act as a service to container managed usages.  The service loads 
all schema catalogs from the classpath and allows acces to the schemas by schema id. 

The SchemaCatalogService can be added as a dependency to a project and injected into a bean to provide a 
lookup service for Schema Ids.  For example adding the following dependency to the Maven pom:

    <dependency>
        <groupId>uk.gov.justice.schema</groupId>
        <artifactId>schema-service</artifactId>
        <version>1.0.1</version>
    </dependency>
    
Then the SchemaCatalogService can be used as follows:

    @ApplicationScoped
    public class JsonSchemaValidator {
            
        @Inject
        SchemaCatalogService schemaCatalogService;
        
        public void validate(final String json, final String schemaId) {
            final Optional<Schema> schema = schemaCatalogService.findSchema(schemaId);
    
            if (schema.isPresent()) {
                final JSONObject jsonObject = new JSONObject(envelopeJson);
                schema.get().validate(jsonObject);
            } else {
                // Do something else if no schema found
            }
        }
    } 

### JSON Schema resolving
The JSON schema resolving functionality can be added to a Java project, for example:

    public class SchemaLoaderResolver {
    
        private final RawCatalog rawCatalog;
        private final SchemaClientFactory schemaClientFactory;
        private final JsonToSchemaConverter jsonToSchemaConverter;
    
        public SchemaLoaderResolver(final RawCatalog rawCatalog,
                                    final SchemaClientFactory schemaClientFactory,
                                    final JsonToSchemaConverter jsonToSchemaConverter) {
            this.rawCatalog = rawCatalog;
            this.schemaClientFactory = schemaClientFactory;
            this.jsonToSchemaConverter = jsonToSchemaConverter;
        }
    
        public Schema loadSchema(final String jsonSchema) {
            return jsonToSchemaConverter.convert(
                    jsonSchema,
                    schemaClientFactory.create(rawCatalog));
        }
    }
    
This can be instantiated by the following:

    final CatalogObjectFactory catalogObjectFactory = new CatalogObjectFactory();

    final SchemaLoaderResolver schemaLoaderResolver = new SchemaLoaderResolver(
            catalogObjectFactory.rawCatalog(),
            catalogObjectFactory.schemaClientFactory(),
            catalogObjectFactory.jsonStringToSchemaConverter());
            
    schemaLoaderResolver.loadSchema(jsonSchema)

The CatalogObjectFactory has convenience methods for creating all the required objects for the 
SchemaLoaderResolver.

* RawCatalog - contains all the unresolved schema collected from all schema_catalog.json files 
present on the classpath.
* SchemaClientFactory - provides an instance of the catalog SchemaClient used to resolve all "$ref" 
values that are present in each schema.
* JsonToSchemaConverter - converts a String of json schema into a fully resolved Schema
