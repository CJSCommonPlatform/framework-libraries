# Domain Test DSL

A DSL for testing aggregates and events using domain language

* Given/When/Then based
* A feature file contains scenarios in plain English which business people can understand
* Java code tests the aggregates mentioned in feature files
* Java code verifies the events and their data

## How it works

**Scenario:** Add a recipe in system

**Given** no previous events in system

**When** addRecipe to a Recipe using add-recipe

**Then** the recipe-added

_add-recipe.json_

```json
{
 "recipeId": "5c5a1d30-0414-11e7-93ae-92361f002671",
 "name": "cheese cake",
 "glutenFree": true,
 "ingredients": [{
   "name": "custard",
   "quantity": 2
 }]
}
```

_recipe-added.json_

```json
{
"_metadata": {
   "name": "example.recipe-added"
 },
"recipeId": "5c5a1d30-0414-11e7-93ae-92361f002671",
"name": "cheese cake",
"glutenFree": true,
"ingredients": [{
    "name": "sugar",
    "quantity": 500
  }]
}
```
