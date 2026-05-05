# cp-framework-libraries

`uk.gov.justice.framework.libraries:framework-libraries`

Common libraries used by the CPP microservice framework. This project provides the APIs, utilities, code generators, and testing tools that the rest of the framework stack depends on.

## Position in the hierarchy

```
maven-framework-parent-pom
└── cp-framework-libraries  ← this project
    └── framework-libraries-bom  (imported by cp-microservice-framework and cp-event-store)
```

`cp-microservice-framework` imports `framework-libraries-bom` to consume the artifacts from this project.

## Modules

| Module | Artifact | Description |
|---|---|---|
| `framework-api` | `framework-api-*` | Java API contracts (interfaces and annotations) for CQRS/ES — `@Handles`, `@ServiceComponent`, `Sender`, `Envelope`, `JsonEnvelope`, `Requester` |
| `framework-utilities` | `utilities-core`, `utilities-file`, `test-utils-*` | Core utilities (UUID generation, date/time helpers, JSON utilities) plus a set of test utilities used across all projects |
| `generator-maven-plugin` | `generator-maven-plugin` | Maven plugin that drives RAML-based code generation (REST adapters, messaging adapters, JMS listeners) |
| `json-schema-catalog` | `json-schema-catalog-*` | JSON Schema catalog — resolves `$ref` URIs to local schema files, mirroring the XML Catalog concept |
| `jsonschema-pojo-generator` | `pojo-generation-plugin`, `jsonschema-pojo-generator` | Generates Java POJOs from JSON Schema definitions; used for domain event payload classes |
| `raml-maven` | `raml-maven-plugin`, `raml-maven` | RAML parsing support for Maven projects; validates RAML files and extracts schema/action metadata |
| `annotation-validator` | `annotation-validator-maven-plugin` | Maven plugin that validates `@Handles`, `@ServiceComponent`, and other framework annotations are applied correctly |
| `domain-test-dsl` | `domain-test-dsl` | Fluent DSL for writing aggregate unit tests — builds event sequences and asserts raised events |
| `job-manager` | `job-manager` | Priority-aware multi-threaded job and task executor; worker slots and priority percentages are JNDI-configurable (see `jndi-configuration.md`) |
| `json-transformer` | `json-transformer` | JSON document transformation utilities (Jolt-based and custom) |
| `framework-datasources` | `framework-datasources` | CDI producers for `@EventStoreDataSource`, `@ViewStoreDataSource`, and `@FileStoreDataSource` JNDI datasource lookups |
| `framework-libraries-bom` | `framework-libraries-bom` | BOM that imports all `framework-libraries` artifacts at a consistent version |

## Key APIs

**Envelope / messaging**
- `JsonEnvelope` — the universal message wrapper carrying a `Metadata` header and a `JsonValue` payload
- `Envelope<T>` — typed variant for domain command/event payloads
- `Metadata` — carries the action name, stream ID, causation chain, session info

**Dispatch**
- `Sender` — fire-and-forget command dispatch (`sender.send(envelope)`)
- `Requester` — request/response query dispatch (`requester.request(envelope)`)
- `@Handles("action.name")` — marks a method as the handler for a named action

**Service component**
- `@ServiceComponent(SERVICE_COMPONENT)` — CDI qualifier that identifies a bean as a framework service component (command API, command handler, event listener, etc.)

**Testing**
- `domain-test-dsl` — `given(events).when(command).thenExpect(event)` style aggregate tests
- `test-utils-core` / `test-utils-common` — builder helpers, envelope factories, mock producers

## Build

```bash
# Build and install all modules
mvn clean install

# Skip integration tests (no PostgreSQL needed)
mvn clean install -DskipTests

# Build a specific module and its dependencies
mvn clean install -pl json-schema-catalog -am
```

## JNDI configuration

See [jndi-configuration.md](./jndi-configuration.md) for JNDI keys used by `job-manager`.
