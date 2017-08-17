# Generator Maven Plugin
[![Build Status](https://travis-ci.org/CJSCommonPlatform/generator-maven-plugin.svg?branch=master)](https://travis-ci.org/CJSCommonPlatform/generator-maven-plugin) 
[![Coverage Status](https://coveralls.io/repos/github/CJSCommonPlatform/generator-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/CJSCommonPlatform/generator-maven-plugin?branch=master)

This project contains a plugin for using [RAML](http://raml.org/) or other documents like Json Schema within Maven projects for generation of code.

## Usage

For example, to generate code using a generator class `ExampleGenerator` using RAML from an external
Maven dependency:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>uk.gov.justice.maven.generator</groupId>
            <artifactId>generator-plugin</artifactId>
            <version>1.7.0</version>
            <executions>
                <execution>
                    <configuration>
                        <generatorName>
                            uk.gov.justice.raml.maven.test.ExampleGenerator
                        </generatorName>
                        <parserName>uk.gov.justice.maven.generator.io.files.parser.RamlFileParser</parserName>
                        <basePackageName>uk.gov.justice.api</basePackageName>
                        <outputDirectory>${project.build.directory}/generated-sources</outputDirectory>
                        <sourceDirectory>CLASSPATH</sourceDirectory>
                        <includes>
                            <include>**/*external-*.raml</include>
                        </includes>
                        <excludes>
                            <exclude>**/*external-ignore.raml</exclude>
                        </excludes>
                    </configuration>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <phase>generate-sources</phase>
                </execution>
            </executions>
            <dependencies>
                <dependency>
                    <groupId>uk.gov.justice.maven</groupId>
                    <artifactId>raml-for-testing-io</artifactId>
                    <version>1.7.0</version>
                    <classifier>raml</classifier>
                </dependency>
                <dependency>
                    <groupId>uk.gov.justice.maven.generator</groupId>
                    <artifactId>raml-parser</artifactId>
                    <version>1.7.0</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```
