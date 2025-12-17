Feature: Product Catalog

  Background:
    Given user is on the home page

  Rule: Customer should be able to search for products by name
    Example: The one where user searches for an Adjustable Wrench
      When user searches for "Adjustable Wrench"
      Then "Adjustable Wrench" should be displayed

    Example: The one where user searches for a more general term
      When user searches for "saw"
      Then the following products should be displayed
        | Wood Saw     |
        | Circular Saw |
      And the following products are displayed in table
        | Product      | Price  |
        | Wood Saw     | $12.18 |
        | Circular Saw | $80.19 |

    Example: User searches for a product that does not exist
      When user searches for "tratata"
      Then no product should be displayed
      And the text "There are no products found." is displayed

  Rule: Customer should be able to narrow down their search by category
    Example: The one where user want to see only Hand Saw category
      When user searches for "saw"
      And user filters by "Hand Saw" category
      Then the following products are displayed in table
        | Product  | Price  |
        | Wood Saw | $12.18 |

  Rule: Customer should be able to search by criteria
    Scenario Outline: User sorts by different criteria
      When user sorts by "<criteria>"
      Then the first product displayed should be "<product>"
      Examples:
        | criteria           | product             |
        | Name (A - Z)       | Adjustable Wrench   |
        | Name (Z - A)       | Wood Saw            |
        | Price (High - Low) | Drawer Tool Cabinet |
        | Price (Low - High) | Washers             |
