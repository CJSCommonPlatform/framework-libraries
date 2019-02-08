# Annotation Validator Maven Plugin

This project contains a plugin for validating annotations.
Currently, it has two purposes (and they are optionally controlled):

- _**Report generation**_ - the plugin can be configured to run in the background to surface validation failures in the form 
of a CSV report.  This report is generated per annotation on which validation is performed
- _**Fail build**_ - Enforce build failure if validation fails  

## Plugin configuration parameters

| Parameter             |  Description                          |
|-----------------------|---------------------------------------|
| _generateReport_        | (Boolean) Generates report per annotation class using format (annotation-plugin-validation-result-{package.annotation-class-name}.csv in the base director path for the module|
| _failBuildOnError_      | (Boolean) Fails build on encountering validation errors if set to true|
| _serviceName_           | (String)  Name of the service against which plugin validation is being executed|
| _validationSkip_        | (Boolean) Skips annotation validation in its entirety if set to true|
| _annotations.annotation_| (List) fully qualified class names for annotations|

## Usage

Sample configuration to generate validation report for a specific annotation (e.g., `uk.gov.justice.domain.annotation.Event`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>uk.gov.justice.maven</groupId>
            <artifactId>annotation-validator-maven-plugin</artifactId>
            <version>LATEST</version> <!-- pick a suitable version -->
            <configuration>
                <generateReport>true</generateReport>
                <failBuildOnError>false</failBuildOnError>
                <serviceName>structure</serviceName>
                <validationSkip>false</validationSkip>
                <annotations>
                    <annotation>uk.gov.justice.domain.annotation.Event</annotation>
                </annotations>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Further work

* Refector plugin to separate out framework specific validation logic
