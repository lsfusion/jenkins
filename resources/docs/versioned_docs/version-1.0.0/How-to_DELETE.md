---
title: 'How-to: DELETE'
---

## Example 1

### Task

We have an order with a given date and buyer and also with lines that refer to the books.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseDelete&block=sample1"/>

We need to create an action that deletes the book for which no orders have been placed.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseDelete&block=solution1"/>

## Example 2

### Task

Similar to **Example 1**.

We need to create an action that clears the order by deleting all its lines.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseDelete&block=solution2"/>

Both these implementations are acting similarly.
