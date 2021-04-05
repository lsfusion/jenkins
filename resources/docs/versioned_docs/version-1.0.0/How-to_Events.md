---
title: 'How-to: Events'
---

## Example 1

### Condition

We have an order with a date, a number and a marker of whether it is closed.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=sample1"/>

We need to make it so that orders are closed automatically at the end of the day.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution1"/>

In the first case, the event will only be executed in one transaction at the time the expression inside the [SET](Change_operators_SET_CHANGED_..._.md) operator changes. That is, at the moment when the order date becomes smaller than the current date. However, if the user manually changes the order date to one greater than the current date and saves, the system will automatically execute this event and close the order. Therefore, the second option is preferable, since it will only come into effect when the current date changes at midnight.

## Example 2

### Condition

Similar to **Example 1**, but the order contains lines with quantity, price and total.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=sample2"/>

We need to make it so that when the price or quantity changes, their product is automatically written to the total amount.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution2"/>

Events of type **LOCAL** count all property changes relative not to the state of the database but to the values at the time of the previous occurrence of this event (or rather, the end of its handling). We need to check that **sum** has not changed, to avoid erasing changes made by previous changes. For example, if order lines have been imported from a file, in which the quantity, price and amount are recorded, then this event will not be triggered.

## Example 3

### Condition

Similar to **Example 2**, but a book is specified for the order line. Each book also has a default price.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=sample3"/>

We need to make sure that when the book is changed, the price is automatically set to the order line. This event should only work on the order edit form.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution3"/>

Event

## Example 1

### Condition

We have an order with a date, a number and a marker of whether it is closed.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=sample1"/>

We need to make it so that orders are closed automatically at the end of the day.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution1"/>

In the first case, the event will only be executed in one transaction at the time the expression inside the [SET](Change_operators_SET_CHANGED_..._.md) operator changes. That is, at the moment when the order date becomes smaller than the current date. However, if the user manually changes the order date to one greater than the current date and saves, the system will automatically execute this event and close the order. Therefore, the second option is preferable, since it will only come into effect when the current date changes at midnight.

## Example 2

### Condition

Similar to **Example 1**, but the order contains lines for the quantity, price and total.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=sample2"/>

We need to make it so that when the price or quantity changes, their product is automatically recorded as the total amount.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution2"/>

Events of type **LOCAL** count all property changes relative not to the state of the database but to the values at the time of the previous occurrence of this event (or rather, the end of its handling). We need to check that **sum** has not changed, to avoid erasing changes made by previous changes. For example, if order lines have been imported from a file, in which the quantity, price and amount are recorded, then this event will no longer take effect.

## Example 3

### Condition

Similar to **Example 2**, but a book is specified for the order line. Each book also has a default price.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=sample3"/>

We need to make sure that when the book is changed, the price is automatically appended to the order line. This event should only work on the order edit form.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution3"/>

In this case, the event will be triggered only when the book is changed or set. When a book is dropped, the price will not change.

Without the **FORMS** block, this event would be triggered by any change to the order line book. For example, when an order was imported in any other form.

## Example 4

### Condition

Similar to **Example 1**.

We need to organize logging of the deletion of orders

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseEvents&block=solution4"/>

When deleting an order and triggering an event with the **DROPPED** modifier, it is important to remember that the object no longer exists and all properties that take it as input will give **NULL** values. Therefore, we need to access them not directly, but through the **PREV** operator.
