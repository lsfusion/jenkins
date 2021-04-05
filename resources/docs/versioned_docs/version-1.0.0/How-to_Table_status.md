---
title: 'How-to: Table status'
---

## Example 1

### Condition

We have the concept of books, for which title, genre and price are defined.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormProperties&block=sample1"/>

We need to display the number of books on the form, taking into account filters made by the user.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormProperties&block=solution1"/>

The solution uses the [FILTER](Filter_FILTER_.md) operator, which returns **TRUE** if the object is included in the current filter on the form.

## Example 2

### Condition

Similar to **Example 1**.

We need to display the serial number of the book in the table with books taking into account the current filter and order made by the user.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormProperties&block=solution2"/>

The property with the current order, defined using the [ORDER](Order_ORDER_.md) operator, is not declared as a separate named property but is used directly in the expression.

The form obtained in both examples, with user-defined filter and order, will look like this:

![](attachments/46367766/46367772.png)
