Feature:

  Scenario:
    Given A mower point
    And a todo point
    When I add hours of usages
    And mower maintenance is reached
    Then a todo maintenance on mower should be triggered


  Scenario:
    Given a new tin as inpoint with expiration date
    When the date is closed based on global duration ratio
    Then a todo eat should be triggered

  Scenario:
    Given an openweathermap point giving current rain
    And a velux window closable engine
    When current weather is raining
    Then close velux window

  Scenario:



