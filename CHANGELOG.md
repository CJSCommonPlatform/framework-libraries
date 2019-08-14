# Change Log
All notable changes to this project will be documented in this file, which loosely follows the guidelines on [Keep a CHANGELOG](http://keepachangelog.com/).

This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.7.2] - 2019-08-14
### Changed
- Upgraded utilities to version 1.20.2
- Upgraded json-schema-catalog to version 1.7.4

## [1.7.1] - 2019-07-12
### Changed
- Upgraded common-bom to version 2.4.0
- Upgraded framework-api to version 4.0.1
- Upgraded utilities to version 1.20.1
- Upgraded test-utils to version 1.24.3
- Upgraded generator maven plugin to version 2.7.2
- Upgraded json-schema-catalog to version 1.7.3

## [1.7.0] - 2019-06-25
### Changed
- Upgraded common-bom to version 2.3.0
- Upgraded framework-api to version 4.0.0
- Upgraded utilities to version 1.20.0
- Upgraded test-utils to version 1.24.2
- Upgraded generator maven plugin to version 2.7.1
- Upgraded json-schema-catalog to version 1.7.2

## [1.6.2] - 2019-05-13
### Changed
- framework-api-version -> 4.0.0-M19

## [1.6.1] - 2019-05-03
### Changed
- common-bom.version -> 2.0.2
- framework-api-version -> 4.0.0-M18
- utilities.version -> 1.18.0
- test-utils.version ->1.23.0
- generator-maven-plugin.version -> 2.7.0
- json-schema-catalog.version -> 1.7.0

## [1.6.0] - 2019-03-11
### Added
- Maven Mojo which allows independent dependency setting

### Changed
- Upgraded json-schema-catalog to version 1.6.0

## [1.5.8] - 2019-02-19
### Changed
- Upgraded json-schema-catalog to version 1.5.0

## [1.5.7] - 2019-02-04
### Changed
- Upgraded utilities to version 1.16.4
- Upgraded test-utils to version 1.22.0
- Upgraded json-schema-catalog to version 1.4.5

## [1.5.6] - 2019-02-01
### Changed
- Upgraded common-bom to version 1.29.0
- Upgraded framework-api to version 3.2.0
- Upgraded utilities to version 1.16.3
- Upgraded test-utils to version 1.21.0
- Upgraded generator maven plugin to version 2.6.2
- Upgraded json-schema-catalog to version 1.4.4

## [1.5.5] - 2019-01-08
### Changed
- Upgraded framework-api to version 3.1.0
- Upgraded utilities to version 1.16.2
- Upgraded test-utils to version 1.19.1
- Upgraded json-schema-catalog to version 1.4.3

## [1.5.4] - 2018-11-15
### Changed
- Upgraded framework-api to version 3.0.1

## [1.5.3] - 2018-11-09
### Changed
- Upgraded framework-api to version 3.0.0
- Upgraded utilities to version 1.16.1
- Upgraded test-utils to version 1.18.1
- Upgraded json-schema-catalog to version 1.4.2

## [1.5.2] - 2018-08-10
### Changed
- Upgraded utilities to version 1.15.1
- Upgraded json schema catalog to version 1.4.1

## [1.5.1] - 2018-08-10
### Fixed
- Optionals plugin ordering

## [1.5.0] - 2018-07-26
### Changed
- Added integer enum support.
- Upgraded common bom to version 1.28.0
- Upgraded utilities to version 1.15.0
- Upgraded generator maven plugin to version 2.6.1
- Upgraded json schema catalog to version 1.4.0

## [1.4.2] - 2018-07-11
### Changed
- Upgraded framework api to version 2.2.0
- Upgraded generator maven plugin to version 2.6.0
- Upgraded json schema catalog to version 1.3.2

## [1.4.1] - 2018-07-04
### Changed
- Moved catalogue resolving and cache update functionality to json-schema-catalog repo

## [1.3.7] - 2018-07-03
### Changed
- Added fix to generate schema with reference to another schema in the same source directory

## [1.3.6] - 2018-06-21
### Changed
- Updated common-bom version to 1.27.0 to fix apache tika security issues

## [1.3.5] - 2018-05-17
### Changed
- Fix upgrade Jackson to 2.8.11

## [1.3.4] - 2018-05-17
### Changed
- Upgrade Jackson to 2.8.11 to fix Jackson security issues 

## [1.3.3] - 2018-02-16
### Fixed
- Builder with only additional properties present

## [1.3.2] - 2018-02-14
### Fixed
- Array example and added example of using reference array item

## [1.3.1] - 2018-02-14
### Added
- Factory for SchemaDefinitionParser to enable uint testing 

## [1.3.0] - 2018-02-14	
	
### Added	
- Logging and skipping of schema, if the schema fails to load or resolve correctly	
- Support for classpath pojo generation	

## [1.2.1] - 2018-01-24

### Fixed
- Fix of improperly created build constructor in Builder plugin if additionalProperties are used

## [1.2.0] - 2018-01-08

### Changed
- Integrate Json Schema Catalog schema id resolution

## [1.1.1] - 2017-12-07

### Fixed
- Schemas are now validated to ensure that they have an id

## [1.1.0] - 2017-12-04

### Added
- Enum is generated with valueFor method, converts value string and returns optional enum type
- Plug in schema id parser for generating base package name
- Add support for relative id uri
- Add validation of schema id as a valid uri

### Changed
- Upgrade everit to version 1.6.0
- Move parsing of json schema file to app start up
- Refactor class name parser to allow prefix names
- Remove root class name generation from the filename
- Update additional properties plugin to accept map.
- Refactor tests to run correctly on Windows os

### Fixed
- Remove sonar issues

## [1.0.0] - 2017-10-04

### Added
- Initial release to support generation of Java POJOs from JSON Schemas
