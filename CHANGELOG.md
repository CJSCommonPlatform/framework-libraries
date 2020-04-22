# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [4.3.0] - 2020-04-22
### Changed
- UnifiedSearchIndexer now takes an JsonEnvelope rather than a raw JsonEnvelope payload

## [4.2.1] - 2019-12-11
### Added
- Added framework-api-system-errors to dependency management

## [4.2.0] - 2019-12-10
### Added
- New SystemErrorService interface for reporting of system errors

## [4.1.0] - 2019-09-19
### Removed
- Moved PublishedEventSource to event-store

## [4.0.1] - 2019-07-11
### Changed
- Updated common-bom to version 2.4.0

## [4.0.0] - 2019-06-20
### Added
- JmsCommandHandlerDestinationNameProvider interface, the implementation will be generated for command handlers
- Add EVENT_INDEXER component
- Add Unified Search APIs
- Add method for getting alternative DataSources from EventStoreDataSourceProvider
- EventStoreDataSourceProvider interface to framework-api-event-source module
- PublishedEventSource for accessing published events

### Changed
- Updated common-bom to version 2.3.0
- Update test-utils version to 1.24.2
- Update parameters for method currentOrderedEventsWith in EventBufferService by adding component
- Renamed method names in PublishedEventSourceTransformation
- Renamed LinkedEventSourceTransformation interface to PublishedEventSourceTransformation
- Renamed LinkedEventException to PublishedEventException
- Moved findEventsSince method from EventSource to PublishedEventSource

### Removed
- Removed send method that takes a Destination, from JmsEnvelopeSender interface

## [3.3.0] - 2019-02-13
### Changed
- Added pojo compatible methods to Sender and Requester interfaces
- Initial implementation of validator for Event annotation class

## [3.2.0] - 2019-02-01
### Changed
- Update common bom version to 1.29.0

## [3.1.0] 2019-01-08
### Added
- findEventsSince(...) method to the EventSource interface
- eventNumber to Metadata interface
- previousEventNumber to Metadata interface
- Path to JsonMetadata for eventNumber and previousEventNumber
- event methods to MetadataBuilder

### Removed
- startSubscription() method from SubscriptionManager

## [3.1.0-M2] 2018-12-03
### Added
- findEventsSince(...) method to the EventSource interface

## [3.1.0-M1] 2018-11-23
### Added
- eventNumber to Metadata interface
- previousEventNumber to Metadata interface
- Path to JsonMetadata for eventNumber and previousEventNumber
- event methods to MetadataBuilder

## [3.0.1] - 2018-11-15
### Added
- Priority loading of EnveloperProvider if more than one provider on the classpath
- Priority loading of EnvelopeProvider if more than one provider on the classpath
- Priority loading of JsonEnvelopeProvider if more than one provider on the classpath
- Add pageSize to the EventStream readFrom(...) method

## [3.0.0] - 2018-11-07
### Changed
- First release of Framework Api 3.0.0

## [3.0.0-M14] - 2018-07-12
### Added
- Accepted Status With Envelope Entity Response Strategy name

## [3.0.0-M13] - 2018-06-22
### Changed
- Updated common-bom version to 1.27.0 to fix apache tika security issues

## [3.0.0-M12] - 2018-06-20
### Changed
-  Remove InterceptorChainProcessor from SubscriptionJmsProcessor.

## [3.0.0-M11] - 2018-06-06
### Changed
-  Change SubscriptionJmsProcessor and SubscriptionManager interfaces.

## [3.0.0-M10] - 2018-05-29
### Changed
-  Remove default event source name from EventSourceName.

## [3.0.0-M9] - 2018-05-21

### Changed
- Updated common-bom version to 1.26.0 for Jackson version 2.8.11

## [3.0.0-M8] - 2018-05-14
### Added
- Add name to EventStream

## [3.0.0-M7] - 2018-05-11
### Added
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

## [2.2.0] - 2018-07-10

### Added
- Accepted Status With Envelope Entity Response Strategy name

## [2.1.2] - 2018-06-20

### Changed
- Update common bom to 1.27.0 to fix apache tika security issues

## [2.1.1] - 2018-06-20

### Added
- Add pageSize to the EventStream readFrom(...) method

## [2.0.3] - 2018-05-16

### Changed
- Downgraded Jackson version to 2.8.11 to fix bug with single argument constructors

## [2.0.2] - 2018-05-15

### Changed
- Updated common-bom version to 1.25.0 for Jackson version 2.9.5

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
