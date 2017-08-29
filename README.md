# jsonschema-pojo-generator
Generator for domain event POJOs defined in json schemas

[![Build Status](https://travis-ci.org/CJSCommonPlatform/jsonschema-pojo-generator.svg?branch=master)](https://travis-ci.org/CJSCommonPlatform/jsonschema-pojo-generator) [![Coverage Status](https://coveralls.io/repos/github/CJSCommonPlatform/jsonschema-pojo-generator/badge.svg?branch=master)](https://coveralls.io/github/CJSCommonPlatform/jsonschema-pojo-generator?branch=master)

### Overview
jsonschema-pojo-generator is a maven plugin used to generate porous (events and commands) from the json schema which defines the command/event in the raml directory.

So given a json schema file **mycontext.events.recipe-added.json**


    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "id": "",
      "type": "object",
      "properties": {
        "recipeId": {
          "id": "/recipeId",
          "type": "string",
          "description": "Uniquely identifies the recipe",
          "name": "id of recipe",
          "title": "id of recipe"
        },
        "name": {
          "id": "/name",
          "type": "string",
          "description": "Name of the recipe",
          "name": "Name of Cake",
          "title": "Name of Cake"
        },
        "glutenFree": {
          "id": "/glutenFree",
          "type": "boolean"
        },
        "ingredients": {
          "id": "/ingredients",
          "type": "array",
          "items": [
            {
              "id": "ingredient",
              "type": "object",
              "properties": {
                "name": {
                  "id": "name",
                  "type": "string"
                },
                "quantity": {
                  "id": "quantity",
                  "type": "integer"
                }
              }
            }
          ],
          "minItems": 1,
          "description": "List ingredients and quantities for recipe"
        }
      },
      "required": [
        "name",
        "ingredients"
      ]
    }
    
will generate two java classes:

**RecipeAdded**

    public class RecipeAdded {
      private final String name;
  
      private final Boolean glutenFree;
  
      private final List<Ingredients> ingredientsList;
  
      private final String recipeId;
  
      public RecipeAdded(
      		final String name, 
      		final Boolean glutenFree, 
      		final List<Ingredients> ingredientsList, 
      		final String recipeId) {
        this.name = name;
        this.glutenFree = glutenFree;
        this.ingredientsList = ingredientsList;
        this.recipeId = recipeId;
      }
  
      public String getName() {
        return name;
      }
  
      public Boolean getGlutenFree() {
        return glutenFree;
      }
  
      public List<Ingredients> getIngredientsList() {
        return ingredientsList;
      }
  
      public String getRecipeId() {
        return recipeId;
      }
    }   
**Ingredients**
    
    public class Ingredients {
        private final Integer quantity;
    
        private final String name;
    
        public Ingredients(final Integer quantity, final String name) {
            this.quantity = quantity;
            this.name = name;
        }
    
        public Integer getQuantity() {
            return quantity;
        }
    
        public String getName() {
            return name;
        }
    }
    
### To Use:

Add the following plugin to your root maven pom:

	<plugin>
        <artifactId>generator-plugin</artifactId>
        <groupId>uk.gov.justice.maven.generator</groupId>
        <version>${generator-maven-plugin.version}</version>
        <executions>
            <execution>
                <id>internal-jsons</id>
                <configuration>
                    <generatorName>uk.gov.justice.generation.SchemaToJavaGenerator</generatorName>
                    <basePackageName>uk.gov.justice.events.pojo</basePackageName>
                    <sourceDirectory>${basedir}/src/main/resources/events/json/schema</sourceDirectory>
                    <outputDirectory>${project.build.directory}/generated-sources</outputDirectory>
                    <parserName>uk.gov.justice.generation.io.files.parser.FileTypeParser</parserName>
                    <includes>
                        <include>**/*.json</include>
                    </includes>
                    <excludes>
                    </excludes>
                </configuration>
                <goals>
                    <goal>generate</goal>
                </goals>
                <phase>generate-sources</phase>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>uk.gov.justice.generators</groupId>
                <artifactId>pojo-generation-core</artifactId>
                <version>1.0.0</version>
            </dependency>
        </dependencies>
    </plugin>
    
The following properties are relevant and should be adjusted to taste:

**basePackageName:**

	<basePackageName>uk.gov.justice.events.pojo</basePackageName>
	
The package name of the generated pojo classes

**sourceDirectory:**

    <sourceDirectory>${basedir}/src/main/resources/events/json/schema</sourceDirectory>
    
The directory where the json schema file should be found. This is then further refined using maven include/exclude tags:

	<includes>
    	<include>**/*.json</include>
    </includes>
    <excludes>
    </excludes>

**outputDirectory:**

    <outputDirectory>${project.build.directory}/generated-sources</outputDirectory>

The directory that the java code will be generated to. This should usually be the 'generated-sources' directory in target as they will then be compiled into your application during the maven compile phase.    

 

###Types
The application is capable of generating pojos with fields of the following types:

* java.lang.String
* java.lang.Integer
* java.lang.BigDecimal
* java.util.UUID
* java.util.List
* java.time.ZonedDateTime
* *any other pojos generated by the application*

### Containing Object and Name
The outer most element in Json Schema is an Object. This Object will map to either the root command Object or event, and so will need a name for this command/event Object. This is done by convention: the name of the schema file is used. Given a json schema file named

    mycontext.events.recipe-added.json

the name will be everything after the (second from) last full stop to the '.json' extension. In this case 

**recipe-added**

It will then be converted into a standard java class name format. i.e capitalise each word and remove the '-', leaving us with the name of the command/event of

**RecipeAdded**

###Events
If the desired generated object is an event, then the root object should be annotated with the 

    @Event("mycontext.events.recipe-added.json")
   
annotation.

The application uses the *.events.* part of the schema file name to deduce that it should be generating an event and so add the annotation.

To recap, all names of json files should be in the format:

    <context-name>.events/commands.<name-of-event/command>.json
    
### Enums

*To Do...*
    
### Arrays
Arrays are supported albeit in a reduced capacity: arrays must be all of the same type: either all **boolean**, **number**, **string** or **object**. Mixing types is arrays is supported by json schema and json but not by this application. For example, the following arrays are allowed:

*all numbers:*


    [1, 2, 3, 4, 5]
    
*all objects:* 

	[{"first": "object"}, {"second": "object"}]
	

**Not supported**
    
    [3, "different", { "types" : "of values" }]
    
Arrays will be generated as a generic *java.util.List* of the appropriate type. For example an array of numbers 

    [1, 2, 3, 4, 5]
   
will be generated as

 	private final List<Integer> myIntegers;
 	
arrays of strings

	["squantum", "crapulent"]
will become

	private final List<String> strangeWords;
	
    

### Combined Schemas (allOf/oneOf/anyOf)

Combined arrays allow a schema to restrict the data allowed in a particular Object. i.e. oneOf would ensure that only one of the specified properties should be in the json document. 

### Unsupported Keywords
* **not**

### Future updates:
* Fields not marked as required should be generated as java Optional.
    



