#!/usr/bin/env bash

mvn -f integration-test/pom.xml clean integration-test -P jsonschema-pojo-genereator-integration-test
