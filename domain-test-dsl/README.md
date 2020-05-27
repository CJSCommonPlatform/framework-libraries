# Domain Test DSL

_Moved into Framework Libraries from its original location as a project 
in [Common Platform](https://github.com/CJSCommonPlatform). For previous versions please refer [here](https://github.com/CJSCommonPlatform/domain-test-dsl)._

A domain-specific language (DSL) for testing aggregate roots and events using domain language:
* Given/When/Then based
* Feature files contain scenarios in plain language that business people can understand
* The _Domain Test DSL_ library automatically tests the Java code implementing aggregate roots according to the behaviour described in the feature files

## Example

The implementation of our business domain is event sourced, so we can describe the current state
(the _Given_ clause) as a list of events that have already happened. When we perform an action on our
domain model (the _When_ clause), the result (the _Then_ clause) will always be a list of events that
have now occurred as a consequence:

_Scenario: Add ingredients to a recipe_

**Given** recipe added

**When** you addIngredients to a Recipe from an ingredients list

**Then** ingredients added

## How it works

The language heavily relies on the simple pattern implemented by the [Aggregate](https://github.com/CJSCommonPlatform/framework-api/blob/master/framework-api-domain/src/main/java/uk/gov/justice/domain/aggregate/Aggregate.java) interface in framework.
Every interaction with an aggregate root follows the same pattern:

1. Events that have occurred previously are applied to the aggregate root
1. An action is performed on the aggregate root by calling one of its methods with some arguments
1. Zero or more events are consequently emitted by the aggregate root

Since the _Given_ and _Then_ clauses are simply lists of event descriptions, the only complex part
of this is the _When_ clause. In order to interpret this, we need it to follow a fixed grammatical structure:

**When** you _\<do something\>_ to an _\<aggregate root\>_ using _\<some new information\>_

* _\<do something\>_ is the action being performed, and has to be the method being called on the aggregate root
* _\<aggregate root\>_ is the name of the Java class implementing the aggregate root
* _\<some new information\>_ is optional, but if present is the data used to perform the action; the arguments passed to the method being called 
* The joining words that can be used are specified by the _Domain Test DSL_ library, but offer a flexible
set of choices designed to allow phrases to read as close to well-written English as possible

In terms of the code implementation, events and input data are provided as JSON files. The example
scenario above translates to the following steps when running the test:

1. Create a new instance of the aggregate root class `Recipe`.
1. Load some data for a `RecipeAdded` event from a JSON file called `recipe-added.json`
1. Apply the `RecipeAdded` event to the `Recipe` aggregate root
1. Load some data for the action from a JSON file called `ingredients-list.json`
1. Call the `addIngredients` method on the `Recipe` using the action data
1. Collect the events emitted from the action method
1. Assert that the collected events match the event data in a file called `ingredients-added.json`

`recipe-added.json`

```json
{
  "_metadata": {
    "name": "recipe-added"
  },
  "recipeId": "5c5a1d30-0414-11e7-93ae-92361f002671",
  "name": "Cheese Cake",
  "glutenFree": true
}
```

`ingredients-list.json`

```json
{
  "ingredients": [{
      "name": "sugar",
     "quantity": 500
    }, {
      "name": "custard",
      "quantity": 2
    }
  ]
}
```

`ingredients-added.json`

```json
{
  "_metadata": {
    "name": "ingredients-added"
  },
  "recipeId": "5c5a1d30-0414-11e7-93ae-92361f002671",
  "ingredients": [{
      "name": "sugar",
     "quantity": 500
    }, {
      "name": "custard",
      "quantity": 2
    }
  ]
}
```

