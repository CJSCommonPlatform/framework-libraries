# Effective JSON Schema Maven Plugin

## Background

JSON schemas in the CPP platform are designed to be composable. Shared definitions —
UUID patterns, post code formats, common domain types — live in central library schemas
(e.g. `http://justice.gov.uk/domain/core/common/definitions.json`) and are referenced
from context-specific schemas using `$ref`:

```json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "properties": {
    "caseId": {
      "$ref": "http://justice.gov.uk/domain/core/common/definitions.json#/definitions/uuid"
    }
  }
}
```

This composition model keeps schemas DRY but creates a practical problem: to understand
what a schema actually validates you must mentally (or programmatically) chase every `$ref`
across multiple JARs. Validators, code generators, and documentation tools all need the
complete schema in one place.

The `effective-json-schema-maven-plugin` solves this in the same spirit as Maven's
`help:effective-pom` — it produces a **single, self-contained document** with every
external `$ref` resolved and inlined, written to `target/effective-json-schemas/` at
build time.

---

## What "effective" means

Given a source schema that references an external definition:

**Source (`src/raml/json/schema/feature-permission.json`):**
```json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "permissionId": {
      "$ref": "http://justice.gov.uk/domain/core/common/definitions.json#/definitions/uuid"
    }
  }
}
```

The plugin produces an effective schema where the referenced definitions file is inlined
under a sanitised key in a top-level `definitions` block, and the `$ref` is rewritten to
point at that local copy:

**Output (`target/effective-json-schemas/feature-permission.json`):**
```json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "definitions": {
    "justice_gov_uk_domain_core_common_definitions": {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "id": "http://justice.gov.uk/domain/core/common/definitions.json",
      "definitions": {
        "uuid": {
          "pattern": "[a-fA-F0-9]{8}-...",
          "type": "string"
        }
      }
    }
  },
  "properties": {
    "permissionId": {
      "$ref": "#/definitions/justice_gov_uk_domain_core_common_definitions/definitions/uuid"
    }
  }
}
```

The effective schema is valid JSON Schema draft-04. All `$ref` values point to `#/…`
(local document) rather than external URLs, making the schema fully self-contained.

---

## Maven coordinates

| Artifact | Group ID | Artifact ID |
|---|---|---|
| Maven plugin | `uk.gov.justice.schema` | `effective-json-schema-maven-plugin` |
| Generation library | `uk.gov.justice.schema` | `catalog-effective-json-schema-generation` |

Both are versioned as part of `cp-framework-libraries` (`json-schema-catalog` sub-project).

---

## How it activates automatically

The plugin is configured in `cpp-platform-maven-service-parent-pom` so every context
module with a `src/raml` directory gets it with zero additional pom changes.

### In `pluginManagement`

Default configuration is declared in `pluginManagement` (applies to all modules):

```xml
<plugin>
    <groupId>uk.gov.justice.schema</groupId>
    <artifactId>effective-json-schema-maven-plugin</artifactId>
    <version>${framework-libraries.version}</version>
    <executions>
        <execution>
            <id>generate-effective-json-schemas</id>
            <configuration>
                <sourceDirectory>${schema.generation.source.directory}</sourceDirectory>
                <outputDirectory>${project.build.directory}/effective-json-schemas</outputDirectory>
            </configuration>
            <goals><goal>generate-effective-json-schemas</goal></goals>
            <phase>process-sources</phase>
        </execution>
    </executions>
</plugin>
```

`schema.generation.source.directory` defaults to `src/raml/json/schema`.

### In the `raml-catalog-generation` profile

The profile activates automatically when a module contains a `src/raml` directory:

```xml
<profile>
    <id>raml-catalog-generation</id>
    <activation>
        <file><exists>src/raml</exists></file>
    </activation>
    <build>
        <plugins>
            <!-- catalog-generation-plugin runs first at generate-sources -->
            <plugin>
                <groupId>uk.gov.justice.schema</groupId>
                <artifactId>effective-json-schema-maven-plugin</artifactId>
                <version>${framework-libraries.version}</version>
                <executions>
                    <execution>
                        <id>generate-effective-json-schemas</id>
                        <goals><goal>generate-effective-json-schemas</goal></goals>
                        <phase>process-sources</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

Any module with `src/raml` will have both the catalog generation plugin (phase:
`generate-sources`) and the effective schema plugin (phase: `process-sources`) active.

---

## Build lifecycle

```
generate-sources    → catalog-generation-plugin builds META-INF/schema_catalog.json
                      (indexes this module's own schemas)

process-sources     → effective-json-schema-maven-plugin
                      1. Loads all schemas from dependency JARs (via their catalogs, then
                         via full classpath scan as fallback)
                      2. Loads all schemas from the local source directory
                      3. For each .json file in the source directory, resolves and inlines
                         every external $ref into a self-contained effective schema
                      4. Writes results to target/effective-json-schemas/
```

The ordering matters: the catalog-generation plugin runs first so the local module's
schemas are available before the inliner runs.

---

## Plugin parameters

| Parameter | Default | Required | Description |
|---|---|---|---|
| `sourceDirectory` | `${schema.generation.source.directory}` | yes | Directory containing source JSON schema files to process |
| `outputDirectory` | `${project.build.directory}/effective-json-schemas` | no | Directory where effective schemas are written |

---

## Schema discovery — how `$ref` targets are resolved

The plugin builds a map of `{ schemaId → rawJson }` before inlining. It populates this
map from three sources, in precedence order:

### 1. Dependency JAR catalogs (highest precedence)

Every dependency JAR that contains a `META-INF/schema_catalog.json` is opened. The
catalog lists each schema's `id` and its path within the JAR. Those schemas are read
and added to the map first.

This is the primary mechanism for resolving refs to platform library schemas
(e.g. `cpp-platform-core-domain`) and shared domain schemas.

### 2. Full classpath scan (fallback)

After catalog-based loading, every `.json` entry in every dependency JAR is scanned.
If a file contains a top-level `"id"` field and can be parsed, it is added to the map
via `putIfAbsent` — catalog-loaded schemas always take precedence.

This fallback handles JARs that carry JSON schema files but have no
`META-INF/schema_catalog.json` (e.g. schemas bundled by a context that does not
publish a catalog).

### 3. Local source directory

Finally, every `.json` file in the configured `sourceDirectory` that contains an `"id"`
field is added to the map (again using `put`, so local schemas override dependency
schemas with the same ID).

---

## Fragment references

A `$ref` that includes a JSON Pointer fragment:

```json
{ "$ref": "http://justice.gov.uk/domain/core/common/definitions.json#/definitions/uuid" }
```

is inlined so the full referenced schema is placed in `definitions` and the fragment
path is preserved:

```json
{ "$ref": "#/definitions/justice_gov_uk_domain_core_common_definitions/definitions/uuid" }
```

The definition name is derived from the schema's base URL by stripping the protocol,
replacing non-alphanumeric characters with underscores, and removing the `.json` suffix:

| Schema URL | Definition name |
|---|---|
| `http://justice.gov.uk/domain/core/common/definitions.json` | `justice_gov_uk_domain_core_common_definitions` |
| `http://justice.gov.uk/standards/complex_address.json` | `justice_gov_uk_standards_complex_address` |

---

## Edge cases

### Circular references

If schema A references schema B which references schema A, the plugin detects the cycle
and leaves the back-reference pointing at the external URL rather than causing infinite
recursion.

### Unresolvable `$ref`

If a `$ref` target cannot be found in any dependency JAR or the local source directory,
the `$ref` is left unchanged in the output. A warning is not emitted — this preserves
forward compatibility when a schema references a peer that is not yet on the classpath.

### Schema file with invalid JSON or duplicate keys

If a source schema file cannot be parsed (e.g. duplicate JSON keys), the file is
skipped with a `WARN` log entry and all other schemas are still processed:

```
[WARNING] Skipping 'src/raml/json/schema/bad.json': could not parse as JSON schema (Duplicate key "name" ...)
```

Similarly, any classpath-scanned schema that fails the JSON parser is silently skipped
and does not block other schemas from being resolved.

---

## Running manually

To regenerate effective schemas for a single module without running the full build:

```bash
# In a context API module (e.g. usersgroups-query-api)
mvn process-sources -Denforcer.skip=true
```

The output lands in `target/effective-json-schemas/` mirroring the relative path
structure of the source directory:

```
target/effective-json-schemas/
├── feature-permission.json
├── group-details-schema.json
├── user-details-schema.json
└── ...
```

---

## Example output walkthrough

Source schema (`person.json`) references two external schemas:

```json
{
  "id": "http://justice.gov.uk/context/person.json",
  "type": "object",
  "properties": {
    "home_address":          { "$ref": "http://justice.gov.uk/standards/address.json" },
    "correspondence_address":{ "$ref": "http://justice.gov.uk/standards/complex_address.json#/definitions/complex_address" },
    "name": { "type": "string" },
    "nino": { "type": "string" }
  },
  "required": ["nino","name","home_address","correspondence_address"]
}
```

Effective schema output:

```json
{
  "id": "http://justice.gov.uk/context/person.json",
  "type": "object",
  "definitions": {
    "justice_gov_uk_standards_address": {
      "type": "object",
      "properties": {
        "addressline1": { "type": "string" },
        "city":         { "type": "string" },
        "postcode":     { "type": "string" },
        "addressline2": { "type": "string" }
      },
      "required": ["addressline1","city","postcode"]
    },
    "justice_gov_uk_standards_complex_address": {
      "type": "object",
      "definitions": {
        "complex_address": {
          "type": "object",
          "properties": {
            "addressline1": { "type": "string" },
            "city":         { "type": "string" },
            "postcode":     { "type": "string" },
            "country":      { "type": "string" }
          },
          "required": ["addressline1","city","postcode","country"]
        }
      }
    }
  },
  "properties": {
    "home_address":           { "$ref": "#/definitions/justice_gov_uk_standards_address" },
    "correspondence_address": { "$ref": "#/definitions/justice_gov_uk_standards_complex_address/definitions/complex_address" },
    "name": { "type": "string" },
    "nino": { "type": "string" }
  },
  "required": ["nino","name","home_address","correspondence_address"]
}
```

---

## Module structure

```
json-schema-catalog/
├── catalog-effective-json-schema-generation/   # Core library (no Maven API dependency)
│   └── src/main/java/.../generation/effective/
│       ├── CatalogJsonSchemaLoader.java         # Loads schemas from JARs + source dir
│       ├── DefinitionNameFactory.java           # Converts schema URLs to definition keys
│       ├── EffectiveJsonSchemaGenerator.java    # Orchestrates inlining per schema file
│       ├── EffectiveJsonSchemaObjectFactory.java# Wires components together (no DI framework)
│       ├── EffectiveJsonSchemaWriter.java       # Writes output files
│       ├── EffectiveJsonSchemaGenerationException.java
│       └── JsonSchemaInliner.java               # Core $ref resolution and rewriting
│
├── effective-json-schema-maven-plugin/          # Thin Maven Mojo wrapper
│   └── src/main/java/.../catalog/maven/
│       └── EffectiveJsonSchemaMojo.java
│
└── effective-json-schema-plugin-it/             # Integration test (runs the plugin for real)
    └── src/test/java/.../it/
        └── EffectiveJsonSchemaGenerationIT.java
```

The core library (`catalog-effective-json-schema-generation`) has no dependency on the
Maven plugin API and can be used directly in tests or tooling without a Maven runtime.

---

## Testing

### Unit tests

Every class has a dedicated JUnit 5 test using `@ExtendWith(MockitoExtension.class)`.
Run from the library module:

```bash
cd catalog-effective-json-schema-generation
mvn test -Denforcer.skip=true
```

### Integration test

`effective-json-schema-plugin-it` is a self-contained Maven module that carries real
JSON schema files (with cross-schema `$ref` relationships) and runs the plugin as part
of its own build. The IT test class reads the generated files from
`target/effective-json-schemas/` and asserts on their content.

```bash
cd effective-json-schema-plugin-it
mvn clean verify -Denforcer.skip=true
```

The IT covers:

- Schemas with no external refs are passed through unchanged
- External refs are inlined into `definitions`
- Fragment refs (`schema.json#/definitions/foo`) are rewritten correctly
- The schema `"id"` field is preserved in the output
- Nested definitions (schema referencing schema referencing schema) are all inlined
