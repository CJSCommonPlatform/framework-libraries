# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased

### Changed

- Updated common bom to 1.4.0
- Excluding SLF4J implementation enforcement
- Corrected parent POM and updated associated dependencies from change


## [1.1.2] - 2016-06-14

### Changed

- Corrected parent POM to release version
- Minor fixes to log messages

## [1.1.1] - 2016-05-12

### Added

- RAML and JSON schema validation mojo for syntax checking without generation
- Add source paths to generator configuration
- Add an option to look for RAML files in the classpath, allowing generation to be done on Maven dependencies not just local files

### Changed

- Skip directory cleaning before generation

## [1.1.0] - 2016-04-18

### Added

- Support for passing custom properties to plugin configuration

## [1.0.0] - 2016-03-15

### Added

- Initial release with basic code generation support
