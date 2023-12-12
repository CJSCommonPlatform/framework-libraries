# Change Log
All notable changes to this project will be documented in this file, which loosely follows the guidelines on [Keep a CHANGELOG](http://keepachangelog.com/).

This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [17.3.2] - 2023-12-12
### Changed
- Add retry mechanism to jobstore

## [17.3.1] - 2023-11-27
### Changed
- Update common-bom to 17.2.1

## [17.3.0] - 2023-11-09
### Added
- Added dependencies required by various contexts to the framework-libraries-common-bom

## [17.2.0] - 2023-11-03
### Changed
- Centralise all generic library dependencies and versions into maven-common-bom
- Update common-bom to 17.2.0
### Removed
- Removed dependency on apache-drools as it's not used by any of the framework code
### Security
- Update common-bom to fix various security vulnerabilities in org.json, plexus-codehaus, apache-tika and google-guava

## [17.1.1] - 2023-07-11
### Changed
- Update to JUnit 5
- Update junit dependencies
    - Cucumber to 7.12.1
    - Mockito to 5.3.1
    - Openejb to 9.1.0
- Update surefire and failsafe plugins

## [17.0.2] - 2023-06-14
### Security
- Update org.json to version 20230227 to fix **security vulnerability CVE-2022-45688**
  Detail: https://nvd.nist.gov/vuln/detail/CVE-2022-45688

## [17.0.1] - 2023-05-10
### Changed
- Removed unnecessary logging of 'skipping generation' message in pojo generator

## [17.0.0] - 2023-05-05
### Changed
- Update to Java 17
- Make pojo generator to perform null safe assignment of additionalProperties inside constructor 
- Change 'additionalProperties' Map in generated pojos to HashMap to allow serialization
- Update maven-common-bom to version 17.0.0
- UtcClock.now() now always truncates the time to milliseconds
### Removed
- Remove illegal-access argument from surefire plugin from plugin management (through maven-parent-pom 17.0.0)
- Remove illegal-access argument from surefire plugin

## [11.0.1] - 2023-02-01
### Changed
- Downgraded maven minimum version to 3.3.9 until the pipeline maven version is updated
- Add cover all token to travis settings
- 
## [11.0.0] - 2023-01-26
### Changed
- Update to Java 11
    - Change from `com.jayway.restassured.path.json.JsonPath` to `io.restassured.path.json.JsonPath`
    - replace org.reflections with classgraph for classpath scanning in the generators
    - Jaxb libraries needed for java 11 as they're no longer included by default
        - com.sun.xml.bind:jaxb-core
        - javax.xml.bind:jaxb-api
        - com.sun.xml.bind:jaxb-impl
- Bumped version to 11.0.0 to match the new framework version
- Update to JEE 8
- DeadLetterQueueBrowser now has 
  - A default name of `jms.queue.DLQ` rather than the original name of `DLQ`
  - A new constructor to pass the name in if you don't want the default name
- MessageConsumerClient is now idempotent when calling `startConsumer(...)`
- Updated API path for alfresco read material endpoint
- Removed strict checking of liquibase.properties files
- Updated to log4j 2; all dependencies on log4j 1 removed
- Cherry-picked from java 8
  - Changed the file service to make hard delete of files from postgres on delete rather than just marking them as deleted
  - Updated maven to always run the integration tests as part of the build
  - Removed support for in memory databases for filestore. Postgres is now required
### Added
- New builder `MessageConsumerClientBuilder` that allows ActiveMQ connection parameters to be specified  
- Added healthcheck api classes as a new module
- Added healthcheck api libraries to bom
- New integration test in pojo generators to test using a unique package name for dependant objects
### Fixed
- Removed hard dependency on javaee-api in jobstore to prevent the setting javaee-api.jar in context wars 

## [7.2.2] - 2020-11-18
### Removed
- Moved EmptyFeatureFetcher to microservices-framework 

## [7.2.1] - 2020-11-18
### Added
- Added a default version of the FeatureFetcher that returns an empty list of Features to 
allow integration tests to run 

## [7.2.0] - 2020-11-13
### Added
- Moved timer bean utilities in from event-store 
- Added support for FeatureControl annotations for feature-toggling code as enabled/disabled

## [7.1.5] - 2020-10-15
### Changed
- Builders of generated pojos now have a `withValuesFrom(...)` method to allow the builder to 
be initialised with the values of another pojo instance  

## [7.1.4] - 2020-10-14
### Changed
- Change import of `org.junit.Assert.assertThat` to `org.hamcrest.MatcherAssert.assertThat` as 
it's now deprecated in Junit 

## [7.1.3] - 2020-10-14
### Changed
- Security updates to apache.tika, commons.beanutils, commons.guava and junit in common-bom
- Updated common-bom to 7.1.1

## [7.1.2] - 2020-10-09
### Added
- New LazyValue class to fetch values using a java Supplier and caches for future access
### Changed
- Removed logging of Resource URL scanning message during maven build

## [7.1.1] - 2020-09-25
### Changed
- Annotation validation plugin now accepts event names that start with `administration.events`
as well as `<service-name>.events`

## [7.1.0] - 2020-09-23
### Changed
- Updated parent maven-framework-parent-pom to version 2.0.0
- Updated maven-common-bom version to 7.1.0
- Moved to new Cloudsmith.io repository for hosting maven artifacts
- Updated encrypted properties in travis.yaml to point to cloudsmith

## [7.0.11] - 2020-09-08
### Changed
-   `JsonObjectToObjectConverter`, `ObjectToJsonObjectConverter` and `ObjectToJsonValueConverter`  
removed from CDI and are now injected using a producer: `JsonObjectConvertersProducer`
### Added
-   Now test utility class `JsonObjectConvertersFactory` to allow these classes
to easily be used in tests

## [7.0.10] - 2020-08-13
### Changed
-   Separate audit jms connection factory
### Added
-   New annotation `ConnectionFactoryName` to allow a separate message broked to be specified for auditing

## [7.0.9] - 2020-07-07
### Changed
- Pojo generator now handles Optionals correctly:
    - Any getter of an Optional field will return Optional.empty() if the field is null
    - 'with' methods in builders are overloaded for Optional fields to take both the raw Object and an Optional
    
## [7.0.8] - 2020-06-02
### Changed
- Updated common bom to 7.0.3

## [7.0.7] - 2020-05-28
### Removed
- Removed wiremock-service sub project, keeping it external instead

## [7.0.5] - 2020-05-27
### Added
- Added File Service Alfresco as a sub module
- Combined the domain-test-dsl repo into framework-libraries
- Combined the wiremock-service repo into framework-libraries

## [7.0.4] - 2020-05-21
### Removed
- Removed framework-generators from the bom's dependency management

## [7.0.3] - 2020-05-21
### Changed
- Moved Embedded Artemis dependencies to maven-common-bom

## [7.0.2] - 2020-05-20
### Changed
- Updated maven-plugin-plugin version to 3.6.0
- Combined the annotation-validator-maven-plugin repo into framework-libraries
- Combined the embedded-artemis repo into framework-libraries
- Updated sub-project's README.md to point to their old locations 

### Changed
## [7.0.1] - 2020-05-18
- Set framework-utilities version to 7.0.1

## [7.0.0] - 2020-05-18

### Changed
- Set framework-utilities version to 7.0.0
- Combined the following repos into framework-libraries
    file-service
    generator-maven-plugin
    job-manager
    json-schema-catalog
    json-transformer
    jsonschema-pojo-generator
    raml-maven

### Added
- file-service 1.17.13: now set to version 7.0.0
- generator-maven-plugin 2.7.3: now set to version 7.0.0
- job-manager 4.3.2: now set to version 7.0.0
- json-schema-catalog 1.7.6: now set to version 7.0.0
- json-transformer 1.0.10: now set to version 7.0.0
- jsonschema-pojo-generator 1.7.6: now set to version 7.0.0
- raml-maven 1.6.9: now set to version 7.0.0
    
