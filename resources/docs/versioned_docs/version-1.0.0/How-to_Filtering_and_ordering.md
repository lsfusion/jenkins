---
title: 'How-to: Filtering and ordering'
---

## Example 1

### Condition

There are remaining books in stock.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFilter&block=sample1"/>

We need to create a form to display the balances of books in a given stock in alphabetical order.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFilter&block=solution1"/>

Option 1 sets up a fixed filter that the user cannot remove. Option 2 allows the user to choose between predefined criteria (by default the one for which the *DEFAULT* option is set).

## Example 2

### Condition

Similar to **Example 1**.

We need to create a form to display remaining books in several warehouses, with the possibility of filtering by a specific warehouse. Ordering should be first by warehouse, and within that by book title.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFilter&block=solution2"/>

In this case a warehouse cannot be declared via the *OBJECTS* block, because then not specifying a warehouse for filtering will not be an option.

## Example 3

### Condition

There is a list of orders for certain customers

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFilter&block=sample3"/>

We need to create a form to display the list of orders allowing to filter by date and/or customer. By default, orders should be in descending date order.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFilter&block=solution3"/>

It should be noted that the dates in this case should always be selected (by default, the current date will be set when the form is opened). But it is possible not to select a customer.

Also, note that what is set in *ORDER BY* is not an expression but a specific property added to the form. Thus, we cannot order by a property that has not been added to the form.
