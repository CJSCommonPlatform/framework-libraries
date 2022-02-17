# Change Log
All notable changes to this project will be documented in this file, which loosely follows the guidelines on [Keep a CHANGELOG](http://keepachangelog.com/).

This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
## [8.0.2] - 2021-10-04
### Changed
- Merged in 7.x.x branch
- Updated common-bom to 8.0.4 to update log4j2 to 2.17.0 

## [8.0.1] - 2021-10-04
### Changed
- Re-release of pom to fix corrupt file in artifactory. No other changes

## [8.0.0] - 2020-09-28
### Changed
- Bumped the version to 8.0.0 to match the final release version of the Java 8 framework
- Removed sonar from the build as it no longer works with Java 8

## [7.3.0] - 2021-12-13
### Changed
- Updated log4j2 to 2.15.0 to fix security vulnerability https://www.randori.com/blog/cve-2021-44228/
- Updated maven-common-pom to 7.2.0

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
    
