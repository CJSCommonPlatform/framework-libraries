# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

### Changed
- Upgrade to use parent POM [1.4.1](https://github.com/CJSCommonPlatform/maven-parent-pom/releases/tag/release-1.4.1)
- Uplift wiremock to the forked version

## [1.14.0] - 2017-03-15
### Added
- add Http Mime

## [1.13.0] - 2017-03-10
### Added
- Add Apache Tika

## [1.12.0] - 2017-03-02

### Removed
- Remove JBoss exclusions

## [1.11.0] - 2017-03-02
- Failed release; do not use

## [1.10.0] - 2017-02-22
### Added 
- Uplift RESTEasy version and exclude conflicting dependencies

## [1.9.0] - 2017-02-14
### Added
- Add maven-plugin-annotations

## [1.8.0] - 2017-02-13

### Added 
- dependency on rest easy-multipart
- dependencies used by raml maven plugin

## [1.7.0] - 2017-02-10

### Added
- maven-plugin-testing-harness
- maven-compat
- maven-core

## [1.6.0] - 2017-01-19

### Added
- Postgres driver, apache.commons-dbcp2 and hamcrest library versions

## [1.5.1] - 2016-12-15

### Changed
- Update org.json version to 20160810

## [1.5.0] - 2016-12-01

### Changed
- Depend on MoJ forked json schema library as it has fix for date-time format

## [1.4.0] - 2016-11-14

### Added
- Add hamcrest regex library

### Changed
- Resolved logging transitive dependency conflicts

## [1.3.0] - 2016-11-02

### Added
- Add jolokia dependencies

### Changed
- Dependencies to resolve maven enforcer conflicts
- Property ordering and removing duplicate declarations

## [1.2.0] - 2016-10-05

### Changed

- Changed artemis.jms.client.version from 1.2.0 to 1.3.0

### Added

- Add artemis.jms.version 1.3.0
- Add artemis-ra library
- Add artemis-jms-server library
- Add artemis-service-extensions library
- Add wildfly-client-all library
- Add javassist version
- Add hamcrest date library
- Dropwizard metrics core
- Weld 2.4.0.Final
- JCommander 1.48


## [1.1.0] - 2016-08-18

### Added

- Add java 8 matchers version


## [1.0.0] - 2016-07-28

### Added

- Initial release of common BOM

[Unreleased]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.14.0...HEAD
[1.14.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.13.0...release-1.14.0
[1.13.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.12.0...release-1.13.0
[1.12.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.11.0...release-1.12.0
[1.11.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.0.0...release-1.11.0
[1.10.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.9.0...release-1.10.0
[1.9.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.8.0...release-1.9.0
[1.8.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.7.0...release-1.8.0
[1.7.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.6.0...release-1.7.0
[1.6.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.5.0...release-1.6.0
[1.5.1]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.5.0...release-1.5.1
[1.5.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.4.0...release-1.5.0
[1.4.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.3.0...release-1.4.0
[1.3.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.2.0...release-1.3.0
[1.2.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.1.0...release-1.2.0
[1.1.0]: https://github.com/CJSCommonPlatform/maven-common-bom/compare/release-1.0.0...release-1.1.0
[1.0.0]: https://github.com/CJSCommonPlatform/maven-common-bom/commits/release-1.0.0
