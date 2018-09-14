# Change Log
All notable changes to this project will be documented in this file, which follows the guidelines 
on [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to 
[Semantic Versioning](http://semver.org/).

## Unreleased

## [2.1.0]

### Changed
- Task is only run if the next start time is after or equal to the current time

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
