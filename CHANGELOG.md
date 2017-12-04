# Change Log
All notable changes to this project will be documented in this file, which loosely follows the guidelines on [Keep a CHANGELOG](http://keepachangelog.com/).

This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.1.0] - 2017-12-04

### Added
- Enum is generated with valueFor method, converts value string and returns optional enum type
- Plug in schema id parser for generating base package name
- Add support for relative id uri
- Add validation of schema id as a valid uri

### Changed
- Upgrade everit to version 1.6.0
- Move parsing of json schema file to app start up
- Refactor class name parser to allow prefix names
- Remove root class name generation from the filename
- Update additional properties plugin to accept map.
- Refactor tests to run correctly on Windows os

### Fixed
- Remove sonar issues

## [1.0.0] - 2017-10-04

### Added
- Initial release to support generation of Java POJOs from JSON Schemas
