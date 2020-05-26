#!/usr/bin/env bash


run_liquibase() {
  local DATABASE=${1/[.-]/_}
  local DB_USER=${2/[.-]/_}
  local DB_PASS=${3}

  mvn -f jobstore-liquibase/pom.xml \
    -Dliquibase.url=jdbc:postgresql://localhost:5432/$DATABASE \
    -Dliquibase.username=$DB_USER \
    -Dliquibase.password=$DB_PASS \
    -Dliquibase.logLevel=info \
    resources:resources \
    liquibase:update
}

####################################################################################################
# Creates the Job Manager tables in the JobStore DB for a specific context, the DB should already exist
#
# USAGE: ./runLiquibaseForJobstore.sh <your context jobstore db> username password
#
# e.g. ./runLiquibaseForJobstore.sh notificationnotifyjobstore notificationnotify notificationnotify
####################################################################################################
run_liquibase $1 $2 $3