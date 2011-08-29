@foo
Feature: Basic Arithmetic
  Scenario Outline: Adding
    When I add <operand a> and <operand b>
    Then the result is <result>
    
    # Try to change one of the values below to provoke a failure
    Examples:
      | operand a | operand b | result |
      |  12       |  5        |  17    |
      |  20       |  5        |  25    |