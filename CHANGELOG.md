# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.20.0] - 2019-01-31

### Added
- JsonObjectBuilderWrapper now supports adding JsonArray attribute

## [1.19.1] - 2018-12-13

### Fixed
- ReflectionUtil now handles fields in super class

## [1.19.0] - 2018-12-07

### Changed
- Update common-bom to version 1.28.0
- Removed Optional from ReflectionUtil field accessors

## [1.18.0] - 2018-07-10

### Added
- ARTEMIS\_URI system property support added for configuring Artemis test connections. ARTEMIS\_HOST\_KEY is ignored if URI is present

## [1.17.3] - 2018-06-20

### Changed
- Updated common-bom version to 1.27.0

## [1.17.2] - 2018-05-16

### Changed
- Downgraded Jackson version to 2.8.11 to fix bug with single argument constructors

## [1.17.1] - 2018-05-15

### Changed
- Updated common-bom version to 1.25.0 for Jackson version 2.9.5

## [1.16.3] - 2018-05-02

### Added
- JavaCompilerUtility to fix out of memory error

### Changed
- Deprecate JavaCompilerUtil and convert to use JavaCompilerUtility

## [1.16.2] - 2018-04-11

### Fixed
- Out of memory error when using JavaCompilerUtil

## [1.16.0] - 2018-01-18

### Added
- Added matcher for asserting jsonpath against a schema

### Fixed
- Generate random ZonedDateTime in different time zones and provide option to generate in UTC specific timezone
- Test cases for random ZonedDateTime generators and added logging to show more info when they fail

## [1.15.0] - 2017-11-16

### Added
- Adds support for whitespace in the SchemaDuplicateChecker (false positives)

### Changed
- Refactor SchemaComplianceChecker and renamed to SchemaDuplicateChecker

## [1.14.0] - 2017-11-09

### Added
- Adds the SchemaComplianceChecker to check differences between schemas across multiple modules, aiding in the single war refactoring process

## [1.13.1] - 2017-11-06

### Added
- javax.mail dependency to fix no class found exception

## [1.13.0] - 2017-11-01

### Changed
- Updated common-bom version to 1.21.0 to remove johnzon-core

## [1.12.0] - 2017-10-31

### Changed
- Update common-bom version to 1.20.0 to fix OWASP issues

## [1.10.0] - 2017-10-23

### Added
- Support for HTTP DELETE

### Changed
- Fix travis build, maven version compatibility

## [1.8.0] - 2017-10-12

### Added
- ReflectionUtil for Class methods and fields

## [1.7.0] - 2017-08-21

### Added
- JavaCompilerUtil copied from generators-test-utils

## [1.6.0] - 2017-08-14

### Added

- Added Sonarcloud analysis
- Added matcher for response headers in rest poller

## [1.5.0] - 2017-07-28

### Changed
- use newer parent pom (builds from bintray), no other changes
- Improve Travis CI process, use container based builds

### Fixed
- Ensure SCM information is correct on all modules

## [1.4.0] - 2017-06-15

### Added
- Test logging modules for bringing in the correct logging dependencies for
tests; a module for tests that only needs slf4j-simple, and another for tests
that need log4j (eg, for testing MDC features)

### Changed
- Made dead letter queue browser auto-closable

## [1.3.0] - 2017-05-26
### Added
- Utility for browsing and deleting DLQ messages

## [1.2.0] - 2017-03-03
### Fixed
- Re-release failed previous release

## [1.1.0] - 2017-03-03
### Added
- Utility class for bootstrapping database
- Utility class for creating JDBC connections
- Utility class for loading files from classpath

## [1.0.0] - 2017-02-15

### Added
- Initial project configuration
- Initial test utilities

[Unreleased]: https://github.com/CJSCommonPlatform/test-utils/compare/release-1.3.0...HEAD
[1.3.0]: https://github.com/CJSCommonPlatform/test-utils/compare/release-1.2.0...release-1.3.0
[1.2.0]: https://github.com/CJSCommonPlatform/test-utils/compare/release-1.1.0...release-1.2.0
[1.1.0]: https://github.com/CJSCommonPlatform/test-utils/compare/release-1.0.0...release-1.1.0
[1.0.0]: https://github.com/CJSCommonPlatform/test-utils/commits/release-1.0.0
`
