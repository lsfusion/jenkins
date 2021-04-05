---
title: 'How-to: SEEK'
---

## Example 1

### Condition

We have a defined logic for books and categories. A form has been created with a list of books categorized.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseSeek&block=sample1"/>

We need to create an action that creates a new book, automatically assigns it to the current category and then makes this book active once the user saves and closes the form.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseSeek&block=solution1"/>

After closing the form, we need to call the [SEEK](SEEK_operator.md) operator which will make the added object active.

## Example 2

### Condition

Similar to **Example 1**. We have also added the customer logic. The user can set a price for each customer and book in the dedicated form.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseSeek&block=sample2"/>

We need to add a default customer whose data will be populated when the user opens the form.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseSeek&block=solution2"/>

The property with the default customer is added to the Settings form on the General tab. The current object will change once the user opens the form, since the [ON INIT](Event_block.md) event will be triggered.

## Example 2

### Condition

Let's assume that we have a report form for which a date range is specified.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseSeek&block=sample3"/>

We need to create buttons that will modify the interval to the last week, current month or last month.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseSeek&block=solution3"/>

Date properties can be found in the **Time** [system module](Modules.md) which is loaded at the very beginning using the **REQUIRE** instruction.
