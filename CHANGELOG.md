# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased

## Added

- Sonarcloud analysis

## Changed

- Switched to common CI user

## [1.6.0] - 2017-07-29

### Changed
- Switch to bintray for releases
- Use common Travis CI build process

### Added
- Set SCM section consistently in all poms

### Fixed
- Fixed common-bom usage
- Fixed mixed line endings

## [1.5.0] - 2017-04-12

### Changed
- Updated lintchecker to pass MavenProject for reflections
- Restructured lintchecker mojo tests

## [1.4.0] - 2017-02-14

### Added
- New lint checker module to verify raml files
- Refactored BetterAbstractMojoTestCase to its own module so it can be reused to test lintcheck rules

## [1.3.0] - 2016-11-15

### Changed

- Updated parent pom to 1.2.0

## [1.2.0] - 2016-11-14

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
