Feature: AggregateWithParameters

  Scenario: Previous events are applied, so new event occurs, We are testing if the method is ability of the code to select the correct method implemented

    Given something happened
    When you checkOrderParameters to a AggregateWithParameters using a argsWithPrimitiveParameters
    Then something happened

  Scenario: Previous events are applied, so new event occurs, We are testing if the method is ability of the code to select the correct method implemented starting with "no"

    Given note created
    When you createNote to a AggregateWithParameters using a note
    Then note created