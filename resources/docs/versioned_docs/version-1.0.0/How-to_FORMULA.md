---
title: 'How-to: FORMULA'
---

## Example 1

### Condition

We have a list of orders.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormula&block=sample1"/>

We need to export this list to CSV and keep the date in the ISO format (YYYY-MM-DD).

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormula&block=solution1"/>

To solve this task we use the [FORMULA](FORMULA_operator.md) operator to create a new property that takes a date and returns its value as a string in the YYYY-MM-DD format. The expression contains [to\_char](https://www.postgresql.org/docs/11/functions-formatting.html) which is a standard PostgreSQL function.

## Example 2

### Condition

Similar to **Example 1**. New lines containing quantity and amount have been added to the orders.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormula&block=sample2"/>

We need to export all the lines from a given order as CSV file in which quantities and amounts are shortened to 3 and 2 characters respectively. In addition, the numbers must be split into triads.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormula&block=solution2"/>

We create the toString property that takes two parameters (numeric value and format) and returns a value of the **TEXT** type. When exporting, we pass the required format as the second parameter.

## Example 3

### Condition

Similar to **Example 2**.

We need to add a column that will be marked when the given order number contains only digits.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseFormula&block=solution3"/>

Since single quotes are used in the formula, make sure to [escape](https://en.wikipedia.org/wiki/Escape_character) them with a backslash **\\\\**.

Note that the native **BOOLEAN** type allows only 2 values: **TRUE** and **NULL**. Therefore, when composing a logical expression, make sure to convert its negative value to **NULL**. In addition, the platform must explicitly know whether the expression can return an undefined value. This is why the keyword **FORMULA** must be followed by the corresponding marker.

At the database level, the **BOOLEAN** type is stored as numeric value (1 or null), and therefore the properties of this type must also return a numeric value. The developer must check that the return type of the expression matches the specified type. Otherwise, the behavior will be unpredictable (but in most cases a request will simply return an error).

Keep in mind that if any property composed by the **FORMULA** operator receives **NULL** as argument, then the overall result will always be **NULL**.

  
