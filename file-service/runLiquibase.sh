#!/usr/bin/env bash
mvn -f file-service-liquibase/pom.xml -Dliquibase.url=jdbc:postgresql://localhost:5432/fileservice -Dliquibase.username=fileservice -Dliquibase.password=fileservice -Dliquibase.logLevel=info resources:resources liquibase:update
