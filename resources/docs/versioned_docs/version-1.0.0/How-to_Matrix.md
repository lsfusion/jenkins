---
title: 'How-to: Matrix'
---

## Example 1

### Condition

We have the concepts of books and buyers defined. A price is defined for each book and buyer.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseColumns&block=sample1"/>

We need to create a form with a single table where rows will contain books and columns will contain buyers. Each cell should have a price for its book and buyer.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseColumns&block=solution1"/>

Object **c** will not be displayed on the form, since no properties have been added for it.

The form will look like this:

![](attachments/46367544/46367547.png)

## Example 2

### Condition

Similar to **Example 1**, only there is a deferred payment period for each book and buyer.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseColumns&block=sample2"/>

We need to create a form with a single table where rows will contain books and columns will contain buyers. For each buyer, there will be two columns next to them: price and deferred payment period. We need to highlight columns with deferred payment information in yellow. It should be possible to choose which buyers to display in the columns.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseColumns&block=solution2"/>

Only objects that meet the filter condition for object **c** will be shown in the columns. Besides, the columns will be shown in the same order as the objects.

All properties with the same ID after the word **COLUMNS** will go side by side for the same objects. In this case, the price and the deferred payment period for the first buyer, then the price and deferral for the second one, and so forth. If IDs were different or unspecified, prices for all buyers would have been shown first followed by deferred payment values.

The form will look like this:

![](attachments/46367544/46367551.png)

## Example 3

### Condition

We need to create a form containing a matrix with a specified number of rows and columns. The user should be able to check each cell.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseColumns&block=solution3"/>

In real projects, we recommend limiting the number of rows and columns during data entry or filtering. Otherwise, if the user enters too many rows or columns, the system will generate a CTE query for the **iterate** property which, in turn, will produce a table containing entered (very big) number of records and that may cause the server database to crash.

Result:

![](attachments/46367544/46367557.png)
