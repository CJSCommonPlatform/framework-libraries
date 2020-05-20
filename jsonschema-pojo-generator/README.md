# jsonschema-pojo-generator
Generator for domain event POJOs defined in json schemas

_Moved into Framework Libraries from its original location as a project 
in [Common Platform](https://github.com/CJSCommonPlatform). 
For previous versions please refer 
[here](https://github.com/CJSCommonPlatform/jsonschema-pojo-generator)._

## Overview
jsonschema-pojo-generator is a maven plugin used to generate POJOs from a json schema and provides the following:

* Creates java POJOs from json schema documents
* All generated POJOs can be parsed to and from json using libraries such as Jackson or Gson
* No annotations on the generated POJO so not tied to one particular library
* Maven plugin - generation is part of the build
* Class generation can be modified using Plugins
* Can override generation by writing your own version of a POJO

See further documentation on the **[Wiki](https://github.com/CJSCommonPlatform/jsonschema-pojo-generator/wiki)**
