# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [3.0.0-M8] - 2018-05-14
###Added
- Add name to EventStream

## [3.0.0-M7] - 2018-05-11
###Added
- EventSourceTransformation interface for cloning and clearing a stream

## [3.0.0-M6] - 2018-05-09
### Updated
- EventSource interface updated.
-   UUID cloneStream(final UUID streamId) method removed
-   void clearStream(final UUID streamId) method removed

## [3.0.0-M5] - 2018-04-26
### Added
- framework-api-subscription module
- EventSourceName annotation
- SubscriptionName annotation
- SubscriptionManager interface and SubscriptionJmsProcessor interface

## [3.0.0-M4] - 2018-04-10
### Added
- Metadata interface changed added source

## [3.0.0-M2] - 2018-03-22
### Added
- Update release version number to 3.0.0-M2

## [3.0.0-M1] - 2018-03-19
### Added
- EventSource interface changed
-   Stream<EventStream> getStreams();
-   Stream<EventStream> getStreamsFrom(final long position);
- EventStream interface changed
-   deprecated long getCurrentVersion();
-   added long size();
-   added long getPosition();


## [2.1.0] - 2018-03-13
### Added
- Retrieving Stream of EventStream

## [2.0.1] - 2018-03-09

## [2.0.0-M1] - 2018-02-14

### Added
- Exception JsonSchemaValidatonException
- Interface JsonValidationLoggerHelper
- Interface NamedToMediaTypeConverter
- SynchronousDirectAdapterCache interface to remove dependency on implementation in core
- InterceptorContextProvider service provider
- Make all providers consistent as interfaces

## 1.1.0 - 2018-01-17

### Changed
- Added getComponentName to InterceptorContext

## 1.0.0 - 2018-01-15

### Added
- Initial copy of API from microservice original framework project

[Unreleased]: https://github.com/CJSCommonPlatform/framework-api/compare/release-1.0.0...HEAD
[1.0.0]: https://github.com/CJSCommonPlatform/framework-api/commits/release-1.0.0
