Feature: StatelessAggregate

  Scenario: No previous events

    Given no previous events
    When you doSomething to a StatelessAggregate using a argsA
    Then something happened

  Scenario: One previous event

    Given something happened
    When you doSomething in a StatelessAggregate with an argsA
    Then something happened

  Scenario: One previous event with qualifier

    Given cake ordered
    And something happened
    When you doSomething in a StatelessAggregate with an argsA
    Then something happened

  Scenario: Two previous events

    Given something happened, something happened
    When you doSomething to a StatelessAggregate using a argsA
    Then something happened

  Scenario: No new events

    Given something happened
    When you doNotDoSomething to a StatelessAggregate using a argsA
    Then no events occurred

  Scenario: Two new events

    Given something happened
    When you doSomethingTwice to a StatelessAggregate using a argsA
    Then something happened, something happened

