# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased
### Changed
- Added schema catalog resolver to enable loading schemas from JSON objects

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
