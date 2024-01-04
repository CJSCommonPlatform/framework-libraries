#!/usr/bin/env bash

CONTEXT_NAME=framework
FRAMEWORK_LIBRARIES_VERSION=17.5.1-SNAPSHOT

#fail script on error
set -e


function runFileStoreLiquibase() {
    echo "Running filestore Liquibase"
    java -jar target/jobstore-liquibase-${FRAMEWORK_LIBRARIES_VERSION}.jar --url=jdbc:postgresql://localhost:5432/${CONTEXT_NAME}jobstore --username=${CONTEXT_NAME} --password=${CONTEXT_NAME} --logLevel=info update
    echo "Finished running filestore liquibase"
}



runFileStoreLiquibase