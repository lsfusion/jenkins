---
title: 'How-to: Using objects as templates'
---

## Example 1

### Condition

We have a book for which a name and price are defined. List and edit forms are defined for the book.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCreate&block=sample1"/>

We need to make a button that creates a new book based on the current one, and opens a form to edit it.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCreate&block=solution1"/>

When the button is pressed, a new [change session](Change_sessions.md) will be created within which the book will be created and the new form will work with. If the user closes the form without saving, the new book will not be saved to the database and will be lost. The word **TOOLBAR** specifies that this button should be displayed in the toolbar of the table with the list of orders.

## Example 2

### Condition

We have the concepts of order documents and invoices, as well as forms for listing and editing them. Lines are defined for each document.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCreate&block=sample2"/>

We need to implement an [action](Actions.md) that will create an invoice based on an order and open a form for editing it.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCreate&block=solution2"/>

## Example 3

### Condition

Similar to **Example 2**.

We need to implement an [action](Actions.md) that will open a dialog for the invoice with a list of orders and add lines from the selected one.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCreate&block=solution3"/>

No manipulation with change sessions is required because the button will be called from the account edit form and changes will occur within that session.
