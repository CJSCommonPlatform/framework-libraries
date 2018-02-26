Feature: StatefulAggregate

  Scenario: Previous events are applied if given, but if no previous event is given then no new event occurs

    Given no previous events
    When you doSomething to a StatefulAggregate using a argsA
    Then something happened

  Scenario: Previous events are applied , so new event occurs

    Given something happened, something happened
    When you doSomething on a StatefulAggregate with an argsA
    Then something-else-happened

  Scenario: Aggregate with no params, previous events are applied, so new events occur

    Given something happened
    When you doNothing on a StatefulAggregate
    Then no events occurred

  Scenario: Previous events are applied, so new event occurs with primitive arguments passed

    Given something happened
    When you doSomething from a StatefulAggregate using a argsA
    Then something-else-happened

  Scenario: Multiple previous events from a single file are applied, no new events occur

    Given multiple things happened
    When you doSomethingTwice on a StatefulAggregate using an argsA
    Then multiple things happened