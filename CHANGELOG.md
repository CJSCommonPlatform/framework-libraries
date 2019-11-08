# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased

## [4.2.3] - 2019-11-08
### Changed
- Update to framework version 6.3.0

## [4.2.2] - 2019-10-24
### Changed
- Update to framework version 6.2.2
- Update to utilities version 1.20.3

## [4.2.1] - 2019-10-18
### Changed
- Update to framework version 6.2.1

## [4.2.0] - 2019-10-15
### Changed
- Update to framework version 6.2.0

## [4.1.0] - 2019-10-01
### Changed
- Update to framework version 6.1.1

## [4.0.8] - 2019-09-23
### Changed
- Update to framework version 6.0.16

## [4.0.7] - 2019-09-19
### Changed
- Update to framework version 6.0.15

## [4.0.6] - 2019-09-11
### Changed
- Update to framework version 6.0.14

## [4.0.5] - 2019-09-08
### Changed
- Update to framework version 6.0.12

## [4.0.4] - 2019-08-30
### Changed
- Update to framework version 6.0.11

## [4.0.3] - 2019-08-29

### Changed
- Update to framework version 6.0.10

## [4.0.2] - 2019-08-21

### Changed
- Update to framework version 6.0.9

## [4.0.1] - 2019-08-19

### Changed
- Update to framework version 6.0.6

## [4.0.0] - 2019-08-15

### Changed
- Update to framework version 6.0.2
- Replace JdbcRepositoryHelper with PreparedStatementWrapperFactory and JdbcResultSetStreamer
- Update to common-bom version 2.4.0
- Update to test-utils version 1.24.3
- Update to utilities version 1.20.2

## [3.1.4] - 2019-03-21

### Changed
- Revert common-bom back to version 1.28.0
- Revert test-utils back to version 1.19.1
- Revert utilities back to version 1.16.2
- Update framework version to 5.1.2

## [3.1.3] - Bad Release - 2019-02-04

### Changed
- Update to common-bom version 1.29.0
- Update to test-utils version 1.22.0
- Update to utilities version 1.16.4
- Update to framework version 5.1.1

## [3.1.2] - 2019-01-22

### Changed
- Use event-store version 1.1.2

## [3.1.1] - 2019-01-16

### Changed
- Update to framework version 5.1.0
- Use event-store version 1.1.1

## [3.0.0] - 2018-11-22

### Changed
- Update to framework version 5.0.4
- Use event-store version 1.0.4

## [2.2.0] - 2018-11-05

### Changed
- Update utilities to version 1.16.0

### Removed
- LoggerProducer and InitialContextProducer

## [2.1.0]

### Changed
- Task is only run if the next start time is before or equal to the current time

## [2.0.2]

### Changed
- Adjusted logging level of jobScheduler to cleanup server logs.

## [2.0.1]

### Changed
- JobScheduler start-wait and timer-interval property names to be milliseconds rather 
than seconds

### Added
- Added a dependency from job-executor to utilities core to allow ValueProducer to be 
used to get the JobScheduler JNDI properties  

## [2.0.0] - 2018-08-03

### Changed
- Major refactoring of the code to clearly distinguish task, job and transient execution state

## [1.0.0] - 2018-07-18
- First release
