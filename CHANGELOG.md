# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased

## [1.6.1-M2] - 2019-04-17

### Changed
- Remove deprecated github_token entry from travis.yml
- Updated incorrect changelog detail

## [1.6.1-M1] - 2019-04-16
- Remove deprecated github_token entry from travis.yml 

## [1.6.0] - 2019-03-11

### Added
- Maven Mojo which allows independent dependency setting

## [1.5.0] - 2019-02-18

### Changed
- Surfacing filename responsible for JSON parsing error

## [1.4.5] - 2019-02-05

### Changed
- Updated utilities version to 1.16.4

## [1.4.4] - 2019-02-01

### Changed
- Updated common-bom-version to 1.29.0
- Updated utilities version to 1.16.3
- Updated generator maven plugin to version to 2.6.2

## [1.4.3] - 2019-01-08

### Changed
- Update utilities version to 1.16.2

## [1.4.2] - 2018-11-09

### Changed
- Update utilities version to 1.16.1

## [1.4.1] - 2018-08-10

### Changed
- Update utilities version to 1.15.1

## [1.3.3] - 2018-07-26
### Changed
- Updated common-bom-version to 1.28.0
- Updated utilities version to 1.15.0
- Updated generator maven plugin to version to 2.6.1
- Load catalog resources from Thread context class loader

## [1.3.2] - 2018-07-11
### Changed
- Upgrade generator maven plugin to version 2.6.0

## [1.3.1] - 2018-07-04
### Added
- updateCatalogSchemaCache method to SchemaCatalogResolver for easier updating of the RawCatalog 
using resources that are not on the classpath

## [1.3.0] - 2018-07-04
### Added
- Schema catalog resolver to enable loading schemas from JSON objects

## [1.2.4] - 2018-07-03

### Changed
- Added fix to generate schema with reference to another schema in the same source directory

## [1.2.3] - 2018-06-21

### Changed
- Updated common-bom version to 1.27.0 to fix apache tika security issues

## [1.2.2] - 2018-05-17
- Upgrade Jackson to 2.8.11 to fix Jackson security issues 

## [1.2.1] - 2018-04-05
### Added
- catalog-test-utils artifact
- Schema catalog resolver test utility to load and resolve schema with $ref values 

## [1.2.0] - 2018-02-14
### Changed
- Update generator maven plugin to version 2.4.0

## [1.1.0] - 2018-01-08
### Added
- Changed JsonToSchemaConverted to convert from both json Strings and JsonObject to a Schema Object

## [1.0.1] - 2017-12-19
### Changed
- Change base loaction in the catalog to use schemaLocation property and any path

## [1.0.0] - 2017-12-15
- First release
