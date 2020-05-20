# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

### Changed
- Update common-bom to version 1.21.0
- Update artemis version to 1.5.5

## [1.1.0] - 2017-07-28

### Changed

- use newer versions of parent pom and common bom
- use dependencies in the common bom properly
- use common Travis build process
- use SCM variables from parent
- EmbeddedArtemisServer utility class has been refactored into multiple 
  classes mentioned below (added section), clean separation of responsibilities 
  SRP(Single Responsibility Principle)

### Added

- EmbeddedArtemisInitializer utility class, for configuring the server
- EmbeddedJMSServer class, maintains server state and wraps an ActiveMQ EmbeddedJMS
- ServerPermit class, maintains a permit for invoking operations on the server

## [1.0.0] - 2016-10-06

### Added
- Initial release

[Unreleased]: https://github.com/CJSCommonPlatform/embedded-artemis/compare/release-1.0.0...HEAD
[1.0.0]: https://github.com/CJSCommonPlatform/embedded-artemis/commits/release-1.0.0
