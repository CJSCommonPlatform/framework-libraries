# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to
[Semantic Versioning](http://semver.org/).

## [Unreleased]

## [4.0.0-M14] - 2019-04-29
### Changed
- Update parameters for method currentOrderedEventsWith in EventBufferService

## [4.0.0-M13] - 2019-04-24
### Deleted
- Removed deprecated classes and methods

## [4.0.0-M12] - 2019-04-24
## Changed 
- Add method for getting alternative DataSources from EventStoreDataSourceProvider

## [4.0.0-M11] - 2019-04-23
## Added
- add INDEXER component
- add SEARCH APIs

## [4.0.0-M10] - 2019-04-23
## Added
- EventStoreDefaultDataSourceProvider interface to framework-api-event-source module

## [4.0.0-M9] - 2019-04-17
## Changed 
- Remove deprecated github_token entry from travis.yml
- Update test-utils to 1.22.0-M1

## [4.0.0-M8] - 2019-04-16
## Changed 
- Renamed method names in PublishedEventSourceTransformation

## [4.0.0-M7] - 2019-04-12
## Changed 
- LinkedEventSourceTransformation interface to PublishedEventSourceTransformation
- LinkedEventException to PublishedEventException

## [4.0.0-M6] - 2019-04-12
### Added
- PublishedEventSource for accessing published events

### Changed
- Moved findEventsSince method from EventSource to PublishedEventSource

## [4.0.0-M5] - 2019-03-29
### Changed
- Change caller/shutterable to a simple String in the event classes

## [4.0.0-M4] - 2019-03-27
### Added
- Removed ApplicationController and moved to jee eventing

## [4.0.0-M3] - 2019-03-26
### Added
- Catchup for Subscription event for informing the app of the cathcup lifecycle of a subscription

## [4.0.0-M2] - 2019-03-19
### Added
- Add support for syncing linked events after stream transformation with transformed events

## [4.0.0-M1] - 2019-03-07
### Added
- Merged in event-buffer-api from event-store as framework-api-event-store
- Merged in event-listener-interceptors from event-store as framework-api-event-listener-interceptors

## [3.4.0-M2] - 2019-03-04
### Added
- JMX dependency to framework-api-bom

## [3.4.0-M1] - 2019-02-19
### Added
- Shuttering, catchup and jmx interfaces

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
