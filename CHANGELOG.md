# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased
### Fixed
- Json Reader not closed

## [1.11.1] - 2017-09-20

### Changed
- Upgraded to utilities version 1.8.1 for JSON logging fix

### [1.11.0] - 2017-08-22

### Changed
- Upgrade Utilities to 1.8.0

### [1.10.0] - 2017-08-02

### Changed
- Fix for BLOB columns with updated liquibase
- Use more recent parent and common-boms (that are in Bintray)
- Use bintray for releases
- Use common Travis CI build process

## [1.8.2] - 2017-07-03
### Fixed
- Close the connection when file not found or inconsistent data

## [1.8.1] - 2017-04-12
### Fixed
- Connection not being closed on metadata retrieval

## [1.8.0] - 2017-03-15
### Added
- Metadata SQL provider strategy producer that allows setting of strategy Class as JNDI GlobalValue 
fileservice.metadata.sql.provider.strategy 

## [1.7.0] - 2017-03-14
### Changed
- upgrade utilities to 1.5.0

## [1.6.0] - 2017-03-13
### Changed
- Move sql providers into their own modules

## [1.5.0] - 2017-03-13
### Added
- Add automatic detection of the file content type

## [1.4.0] - 2017-02-28
### Fixed
- Re-released due to a failed artifactory deployment

## [1.3.0] - 2017-02-28

### Changed
- Upgraded version of utilities to 1.1.0

## [1.2.0] - 2017-02-28

### Removed
- Removed dependency on Microservices Framework

## [1.1.0] - 2017-02-20

### Fix
- Fix javadoc error, breaking the release process

## [1.0.0] - 2017-02-15

### Added
- Initial version of the File Service

[Unreleased]: https://github.com/CJSCommonPlatform/microservice_framework/compare/release-1.8.2...HEAD
[1.8.2]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.8.1...release-1.8.2
[1.8.1]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.8.0...release-1.8.1
[1.8.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.7.0...release-1.8.0
[1.7.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.6.0...release-1.7.0
[1.6.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.5.0...release-1.6.0
[1.5.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.4.0...release-1.5.0
[1.4.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.3.0...release-1.4.0
[1.3.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.2.0...release-1.3.0
[1.2.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.1.0...release-1.2.0
[1.1.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.0.0...release-1.1.0
[1.0.0]: https://github.com/CJSCommonPlatform/file-service/commits/release-1.0.0
