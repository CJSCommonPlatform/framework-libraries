# jsonschema-pojo-generator
Generator for domain event POJOs defined in json schemas

[![Build Status](https://travis-ci.org/CJSCommonPlatform/jsonschema-pojo-generator.svg?branch=master)](https://travis-ci.org/CJSCommonPlatform/jsonschema-pojo-generator) [![Coverage Status](https://coveralls.io/repos/github/CJSCommonPlatform/jsonschema-pojo-generator/badge.svg?branch=master)](https://coveralls.io/github/CJSCommonPlatform/jsonschema-pojo-generator?branch=master)

## Overview
jsonschema-pojo-generator is a maven plugin used to generate POJOs from a json schema and provides the following:

* Creates java POJOs from json schema documents
* All generated POJOs can be parsed to and from json using libraries such as Jackson or Gson
* No annotations on the generated POJO so not tied to one particular library
* Maven plugin - generation is part of the build
* Class generation can be modified using Plugins
* Can override generation by writing your own version of a POJO

See further documentation on the **[Wiki](https://github.com/CJSCommonPlatform/jsonschema-pojo-generator/wiki)**
