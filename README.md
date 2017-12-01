# Json Schema Catalog


## Overview
Json Schema Catalog is a Maven Plugin which allows easy inclusion of references in json schema documents to other json schemas, without the need to host the fragment schemas on a server, and/or to solve the problems of paths if the fragment schemas are stored on a file system. This allows for common standard schema definitions like *address* or *UUID* to be commonly used in all json schema documents and for these fragment schemas to be shared as jars.

Json Schema Catalog works by building a catalog of all json schemas found at a known location on the classpath and mapping the actual location (on the classpath) to the id of the json schema file.

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

Which defines a schema fragment *address.json* using the url http://justice.gov.uk/standard/fragments/address.json (which is also the id of address.json)

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
    
Normally, when person-schema.json is used to validate against a json document, whichever library you are using to handle json schema (we are using the excellent Everit json validator: https://github.com/everit-org/json-schema), would load the fragment schema from the URI defined in the reference. i.e. http://justice.gov.uk/standard/fragments/address.json.

Instead, we build a catalog of all json schemas found on the classpath (at a known location) which maps the id of the schema against it's base location (e.g. jar etc.) and its path in that location:

    {
      "catalog": {
      "name": "example catalog",
      "groups": [
        {
          "name": "fragments",
          "baseLocation": "jar://CommonSchemas.jar!//context/schemas",
          "schemas": [
            {
              "id": "http://justice.gov.uk/standard/fragments/address.json",
              "location": "fragments/addresses"
            },
            {
              "id": "http://justive.gov.uk/fragments/uuid.json",
              "location": "fragments/uuid.json"
            }
          ]
        },
        {
          "name": "domain",
          "baseLocation": "file:///context/domain",
          "schemas": [
            {
              "id": "http://justive.gov.uk/domain/person.json",
              "location": "domain/person.json"
              }
            ]
          }
        ]
      }
    }
    
So, in our example we would map the address.json fragment id http://justice.gov.uk/standard/fragments/address.json against it's actual location jar://CommonSchemas.jar!//context/schemas/fragments/addresses/address.json.

When a json document is validated, we override the call to the fragment id url and instead return an InputStream to the actual fragment schema file.

