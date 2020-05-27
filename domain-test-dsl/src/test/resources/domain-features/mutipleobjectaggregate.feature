Feature: MultipleObjectAggregate

  Scenario: Previous events are applied , so new event occurs

    Given first event
    And second event
    When you performAction in a MultipleObjectAggregate
    Then third-event

  Scenario: Previous events are applied , so new event occurs

    Given first event, second event
    When you performAction in a MultipleObjectAggregate
    Then third-event

  Scenario: First event without the second event, no events occurred

    Given first event
    When you performAction in a MultipleObjectAggregate
    Then no events occurred

  Scenario: Second event without the first event, no events occurred

    Given second event
    When you performAction in a MultipleObjectAggregate
    Then no events occurred

  Scenario: Event order changed, no events occurred

    Given second event
    And first event
    When you performAction in a MultipleObjectAggregate
    Then no events occurred