# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.17.13] - 2020-04-09
## Changed
- Added new create method to FileServiceTestClientTest that allows the file id to be passed in

## [1.17.12] - 2019-10-23
## Changed
- Update utilities to version 1.20.3

## [1.17.11] - 2019-08-14
## Changed
- Update utilities to version 1.20.2

## [1.17.10] - 2019-07-11

## Changed
- Updated common-bom version to 2.4.0
- Update utilities to version 1.20.1
- Update test-utils to version 1.24.3

## [1.17.9] - 2019-06-25

## Changed
- Updated common-bom version to 2.3.0
- Update utilities to version 1.20.0
- Update test-utils to version 1.24.2

## [1.17.8] - 2019-06-18

### Changed
- Updated common-bom version to 2.2.0 to fix apache tika security issues
- Update utilities to version 1.19.0
- Update test-utils to version 1.24.0

## [1.17.7] - 2019-05-07

## Changed
- Generated new token travis keys
- Update common-bom to version 2.0.2
- Update utilities to version 1.18.0
- Update test-utils to version 1.23.0

## [1.17.6] - 2019-05-07

## Changed
- Update common-bom to version 2.0.2
- Update utilities to version 1.18.0
- Update test-utils to version 1.23.0

## [1.17.5] - 2019-05-07

## Changed
- Re-released in 1.17.6

## [1.17.4-M1] - 2019-04-17

## Changed
- Remove deprecated github_token entry from travis.yml
- Update utilities to version 1.17.0-M2
- Update test-utils to version 1.22.0-M1

## [1.17.4] - 2019-02-04

## Changed
- Update utilities to version 1.16.4
- Update test-utils to version 1.22.0


## [1.17.3] - 2019-02-01

## Changed
- Update common-bom to version 1.29
- Update utilities to version 1.16.3
- Update test-utils to version 1.21.0

## [1.17.2] - 2019-01-08

## Changed
- Update utilities to version 1.16.2
- Update test-utils to version 1.19.1

## [1.17.1] - 2018-11-09

## Changed
- Update utilities to version 1.16.1
- Update test-utils to version 1.18.1

## [1.17.0] - 2018-08-10

## Changed
- Update utilities to version 1.16.0

## [1.16.4] - 2018-08-10

## Changed
- Update utilities to version 1.15.1

## [1.16.3] - 2018-07-26

### Changed
- Updated common-bom version to 1.28.0
- Updated utilities to version 1.15.0 

## [1.16.2] - 2018-06-21

### Changed
- Updated common-bom version to 1.27.0 to fix apache tika security issues

## [1.16.1] - 2018-05-17
- Upgrade Jackson to 2.8.11 to fix Jackson security issues 

## [1.16.0] - 2018-04-13

## Changed
- Updated common-bom to version 1.24.0
- Updated utilities to version 1.13.0
- Updated test-utils to version 1.16.2

## [1.15.0] - 2018-02-09
### Changed
- Updated utilities library to version 1.12.0

## [1.14.0] - 2017-12-11
### Changed
- Updated utilities to version 1.11.0
- Updated test-utils to version 1.15.0

## [1.13.0] - 2017-11-01

### Changed
- Updated common-bom to version 1.21.0
- Updated utilities to version 1.10.0
- Updated test-utils to version 1.13.0

## [1.12.0] - 2017-10-17

### Fixed
- Json Reader not closed

### Changed
- Upgraded to utilities version 1.9.0 for Json Obfuscator rework

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

[Unreleased]: https://github.com/CJSCommonPlatform/microservice_framework/compare/release-1.15.0...HEAD
[1.15.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.14.0...release-1.15.0
[1.14.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.13.0...release-1.14.0
[1.13.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.12.0...release-1.13.0
[1.12.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.11.0...release-1.12.0
[1.11.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.10.0...release-1.11.0
[1.10.0]: https://github.com/CJSCommonPlatform/file-service/compare/release-1.8.0...release-1.10.0
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
