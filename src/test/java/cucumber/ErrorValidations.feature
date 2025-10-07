@tag
Feature: Error Validation

  @ErrorValidation
  Scenario Outline: Negative test of submitting the order
    Given I landed on Ecommerce Page
    When Logged in with username <name> and password <password>
    Then "Incorrect email or password." message is displayed

    Examples:
      | name                 | password       |
      | rrolas10@hotmail.com | Hqtzgl03521810 |
