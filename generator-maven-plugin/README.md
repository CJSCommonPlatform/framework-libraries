# Generator Maven Plugin

This project contains a plugin for using [RAML](http://raml.org/) or other documents like Json Schema within Maven projects for generation of code.

_Moved into Framework Libraries from its original location as a project 
in [Common Platform](https://github.com/CJSCommonPlatform). 
For previous versions please refer 
[here](https://github.com/CJSCommonPlatform/generator-maven-plugin)._


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
                    <groupId>uk.gov.justice.maven.generator</groupId>
                    <artifactId>raml-for-testing-io</artifactId>
                    <version>1.7.0</version>
                    <classifier>raml</classifier>
                </dependency>
                <dependency>
                    <groupId>uk.gov.justice.maven.generator</groupId>
                    <artifactId>generator-raml-parser</artifactId>
                    <version>1.7.0</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```
