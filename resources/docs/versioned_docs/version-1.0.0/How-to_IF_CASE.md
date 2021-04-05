---
title: 'How-to: IF/CASE'
---

## Example 1

### Condition

We have a list of books associated with certain categories. Each book is assigned a price.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseIfCase&block=sample1"/>

We need to create an action that sets a given price for books associated with the specific category and a fixed price for all other books. When no category is selected, the error message must appear.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseIfCase&block=solution1"/>

## Example 2

### Condition

Similar to **Example 1**.

We need to create an action that sets pre-defined prices for books associated with any of the three categories and sets zero price for all other books.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseIfCase&block=solution2"/>
