---
title: 'How-to: NEWSESSION'
---

## Example 1

### Condition

We have an order with a number and a posted/not posted flag.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=sample1"/>

We need to create an action that will post this order in a separate change session and then will add it to the form containing the list of orders.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=solution1"/>

If you do not "wrap" the action that sets the **isPosted** property in the [NEWSESSION](NEWSESSION_operator.md) operator, then the [APPLY](APPLY_operator.md) operator will also write other changes (including those made on the **orders** form) to the database.

## Example 2

### Condition

Similar to **Example 1**, except that the dedicated edit form is available for the order.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=sample2"/>

We need to create an action that creates a new order and open the edit form for it. This action must be added to the form containing the list of orders.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=solution2"/>

If you do not use the **NEWSESSION** operator, then the object for the new order will be created in the [change session](Change_sessions.md) of the **orders** form. And if the user closes the form without saving, then all the changes "will remain" in the change session for the form, and the created order will be displayed in the form containing the list of orders.

## Example 3

### Condition

Similar to **Example 2**, except that the order can be marked.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=sample3"/>

We need to create an action that will delete the marked orders and immediately save the changes to the database (so that the user does not need to click "Save").

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=solution3"/>

By default, a new session ignores changes made in the "upper" session. To make the selected property available in the new session, use the **NESTED** block of operators. Otherwise, the **selected** property will always be NULL. Alternatively, you can use the **NESTED LOCAL** block instead of specifying particular properties. In this case, changes made to all local properties in the upper session will be visible.

## Example 4

### Condition

Similar to  **Example 2**, except that the payment logic for the order has been added.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=sample4"/>

We need to create a button on the form for opening a separate edit form for payments in this order.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseNewSession&block=solution4"/>

If you use the [NESTEDSESSION](NESTEDSESSION_operator.md) operator, then all the changes made in the "upper" change session will be available in the nested session. If the user closes the form without clicking OK, then all changes made directly in the form will be lost. If the user clicks OK, then the changes will be written to the "upper" change session rather than to the database. They will be written to the database along with the changes made in the main **orders** form.

It is not allowed to use **NEWSESSION** here simply because the **orderPayments** form will not be able to see the newly created order which has not yet been added to the database (since changes made in the "upper" session are not visible in the nested one), and thus the behavior will be unpredictable.

If we do not use any session management operator at all, then if the user make any changes in the **orderPayments** form and clicks the Close button, the changes will still be "saved", though the user might not expect that.
