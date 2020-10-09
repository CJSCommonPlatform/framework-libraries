# Change Log
All notable changes to this project will be documented in this file, which loosely follows the guidelines on [Keep a CHANGELOG](http://keepachangelog.com/).

This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added
- New LazyValue class to fetch values using a java Supplier and cache for future access

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
    
