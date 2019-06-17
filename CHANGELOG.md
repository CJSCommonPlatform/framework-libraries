# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

### Added
- Poller and MultiIteratingPoller classes

## [1.18.0] - 2019-05-03

### Changed
- Updated test-utils version in parent pom to 1.23.0

## [1.17.0-M3] - 2019-05-03

### Added
- Yaml parser
- Updated bom-config

## [1.17.0-M2] - 2019-04-16

### Changed
- Update changelog with correct details
- Remove deprecated github_token entry from travis.yml

## [1.17.0-M1] - 2019-01-08
- Remove deprecated github_token entry from travis.yml
- Updated test-utils to 1.22.0-M1

## [1.16.4] - 2019-02-04

### Changed
- Updated test-utils to 1.22.0

## [1.16.3] - 2019-02-01

### Changed
- Updated test-utils to 1.21.0
- Updated common bom to 1.29.0

## [1.16.2] - 2019-01-08

### Changed
- Updated test-utils to 1.19.1

## [1.16.1] - 2018-11-08

### Changed
- Updated test-utils to 1.18.1

## [1.16.0] - 2018-11-05

### Added
- Moved in CDI producers from Framework and JobManager

## [1.15.1] - 2018-08-10
- Fix additional properties NullPointer exception.

## [1.15.0] - 2018-07-26

### Changed
- Add Integer based enum capability for serialisation and deserialization of Json 

## [1.14.0] - 2018-07-20

### Changed
- Configured object mapper to ignore unexpected properties, to support additionalProperties = true

## [1.13.2] - 2018-06-21

### Changed
- Updated common-bom version to 1.27.0 to fix apache tika security issues

## [1.13.1] - 2018-05-17
- Upgrade Jackson to 2.8.11 to fix Jackson security issues 

## [1.13.0] - 2018-04-13

### Added
- ObjectMapperProducer method for creating and object mapper with a JmsFactory

## [1.12.1] - 2018-03-06

### Fixed
- Exception thrown by ObjectMapper if additional properties contains a null value

## [1.12.0] - 2018-02-09

### Added
- Moved LoggerUtils to utilities project.

## [1.11.0] - 2017-11-15

### Added
- Added additional properties functionality.

## [1.10.0] - 2017-11-01

### Added
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

[Unreleased]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.12.0...HEAD
[1.12.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.11.0...release-1.12.0
[1.11.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.10.0...release-1.11.0
[1.10.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.9.0...release-1.10.0
[1.9.0]: https://github.com/CJSCommonPlatform/utilities/compare/release-1.8.0...release-1.9.0
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
