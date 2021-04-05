---
title: 'How-to: FOR'
---

## Example 1

### Task

We have a list of books with titles.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=sample1"/>

We need to find all the books containing a given line and display a message with their names and internal codes.

### Task

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=solution1"/>

Use the isSubstring property (defined in the **Utils** system [module](Modules.md)) to identify whether a given line contains another line.

## Example 2

### Task

Similar to **Example 1**.

We need to create an action that creates 100 new books with certain titles.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=solution2"/>

Both these implementations will provide the same result.

To solve this task, use the **iterate** property (defined in the **Utils** system module) which returns **TRUE** for all integers in the range.

## Example 3

### Task

Similar to **Example 1**, but with the order logic. Each order contains lines where books and discount prices are specified.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=sample3"/>

We need to create an action that applies a discount to all the lines with prices above 100.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=solution3"/>

Both these implementations will provide the same result.

## Example 4

### Task

Similar to **Example 3**, but a default price was added for each book.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=sample4"/>

We need to create an action that adds all the books with prices above 100 to the order.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFor&block=solution4"/>

Both these implementations will provide the same result.
