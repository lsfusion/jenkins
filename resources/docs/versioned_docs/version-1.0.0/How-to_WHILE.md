---
title: 'How-to: WHILE'
---

## Example 1

### Condition

We have an order for which a date is defined.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseWhile&block=sample1"/>

We need to display a message containing the number of orders for each date from a given interval.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseWhile&block=solution1"/>

Both these options will provide the same result.

The **sum** property defined in the **Time** system [module](Modules.md) is used to add one day to the given date.

## Example 2

### Condition

Similar to **Example 1**. We have also defined order lines, so that each line contains the (full) amount and the discount.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseWhile&block=sample2"/>

We need to create an action that "distributes" given discount by line, starting from the line with the largest amount.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseWhile&block=solution2"/>

## Example 3

### Condition

The logic for changing the balance for the book is defined as follows:

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseWhile&block=sample3"/>

We need to create an action that will calculate the accumulated (integral) balance for a given time period.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseWhile&block=solution3"/>
