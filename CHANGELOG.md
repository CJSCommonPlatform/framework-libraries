# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased

## [2.7.0] - 2019-05-03

### Changed
- Updated common-bom version to 2.0.2


## [2.7.0-M1] - 2019-04-16

### Changed
- - Remove deprecated github_token entry from travis.yml

## [2.6.2] - 2019-02-01

### Changed
- Updated common-bom version to 1.29.0

## [2.6.1] - 2018-07-26

### Fixed
- Classpath processing issue in GenerateGoalProcessor

### Changed
- Updated common-bom version to 1.28.0

## [2.6.0] - 2018-07-11

### Added
- Handle source and classpath directory in generator-maven-plugin

## [2.5.1] - 2018-06-21

### Changed
- Updated common-bom version to 1.27.0 to fix apache tika security issues

## [2.5.0] - 2018-05-17
- Upgrade Jackson to 2.8.11 to fix Jackson security issues 

## [2.5.0-M2] - 2018-03-28
### Changed
- Subscription generator plugin moved to the framework.

## [2.5.0-M1] - 2018-03-19
### Added
- Update to milestone release

## [2.4.0] - 2018-02-13
### Added
- Support for FileParser Factory

## [2.3.0]
### Changed
- plugin basePackageName property is optional

## [2.2.0] - 2017-11-07
### Changed
- Updated common-bom to version 1.21.0

## [2.1.0] - 2017-10-18
### Changed
- Update common-bom to version 1.19.0 to use latest everit version

## [2.0.0] - 2017-09-25
### Added
- GeneratorFactory interface to allow factory creation of Generators
- GeneratorProperties interface for custom implementation of property class

## [1.7.0] - 2017-08-17
### Added
- Json schema parser
- Specify a parser alongside a generator
