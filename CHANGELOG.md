# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.11.0] - 2017-11-15

### Changed
- Added additional properties functionality.

## [1.10.0] - 2017-11-01

### Changed
- Update common-bom version to 1.20.0 to fix OWASP issues

## [1.9.0] - 2017-10-12

### Changed

- Removed dependency on org.json and refactored JSONObjectValueObfuscator.

## [1.8.1] - 2017-09-20

### Fixed
- Potential leak of sensitive information to log files when JSON errors are encountered in JsonObjectToObjectConverter class

## [1.8.0] - 2017-08-21

### Changed

- Travis CI user updated to common CI user
- Test utils upgraded to version 1.7.0

### Added
- Support for enums for ObjectMapper and ObjectMapperProducer
- Added Sonarcloud analysis
- Moved compiler classes to Utilities from Microservices Framework

## [1.7.0] - 2017-07-28

### Changed
- Switch to bintray for releases
- Use improved Travis CI build process
- Consistent SCM settings

## [1.6.0] - 2017-03-15

### Added
- configuration GlobalValue and Value annotation implementations from framework

## [1.5.0] - 2017-03-14

### Added
- beans.xml for CDI for file module

## [1.4.0] - 2017-03-13

### Added
- beans.xml for CDI

## [1.3.0] - 2017-03-13

### Added
- Created new 'file' module and moved content detection into it

## [1.2.0] - 2017-03-10

### Added
- Utility class for detecting the content type of an InputStream
- Upgrade common bom to 1.13.0

## [1.1.0] - 2017-02-28
- JsonObjects and other utility classes moved to here from framework

## [1.0.0] - 2017-02-28

### Added
- Initial project configuration
- Initial Utilities

[Unreleased]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.8.1...HEAD
[1.8.1]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.8.0...release-1.8.1
[1.8.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.7.0...release-1.8.0
[1.7.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.6.0...release-1.7.0
[1.6.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.5.0...release-1.6.0
[1.5.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.4.0...release-1.5.0
[1.4.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.3.0...release-1.4.0
[1.3.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.2.0...release-1.3.0
[1.2.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.1.0...release-1.2.0
[1.1.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.0.0...release-1.1.0
[1.0.0]: https://github.com/CJSCommonPlatform/utilities/commits/release-1.0.0
