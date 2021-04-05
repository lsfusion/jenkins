---
title: 'How-to: CASE/IF/OVERRIDE'
---

## Example 1

### Task

We have a set of white books and black books.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample1"/>

We need to define a property that returns the color of a given book.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution1"/>

These two expressions provide identical results.

## Example 2

### Task

We have multiple purchase orders to suppliers for books. For each purchase order defined it's status if it was placed, agreed and delivered. In this example these statuses are declared as [data](Data_properties_DATA_.md) properties, but in more sophisticated cases they may be calculated.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample2"/>

We need to obtain the current status of an order.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution2"/>

## Example 3

### Task

We have a set of books described in **Example 1**.

We need to set a markup for the book and also provide an option for setting a default value.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution3"/>

## Example 4

### Task

The same set of books from **Example 1**, but categorized.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample4"/>

We need to set a markup for a book and also provide an option for setting a default value for the corresponding category.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution4"/>

## Example 5

### Task

We have a set of enumerated books.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=sample5"/>

We need to find the number following the maximum book number.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCIO&block=solution5"/>

We use the operator (+) instead of the regular operator +, because otherwise if no books are found, then the standard increment by 1 will return **NULL**.
