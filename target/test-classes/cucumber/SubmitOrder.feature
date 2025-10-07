@tag
Feature: Purchase the order from Ecommerce Website

  Background:
    Given I landed on Ecommerce Page

  @Regression
  Scenario Outline: Positive test of submitting the order
    Given Logged in with username <name> and password <password>
    When I add product <productName> to Cart
    And Checkout <productName> and submit the order
    Then "Thankyou for the order." message is displayed on ConfirmationPage

    Examples:
      | name                 | password       | productName |
      | rrulas10@hotmail.com | Hqtzgl03521810 | ZARA COAT 3 |
