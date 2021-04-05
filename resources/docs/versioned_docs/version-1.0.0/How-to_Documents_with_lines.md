---
title: 'How-to: Documents with lines'
---

## Example 1

### Condition

We have the orders and their specification lines.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseDocument&block=sample1"/>

We need to create a form with a list of orders where the user can add, edit or delete them.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseDocument&block=solution1"/>

We do not add a link to the order for the line object on the *order* form, since when adding the object using *NEW*, the system will automatically set up this link based on the *FILTERS* block.

## Example 2

### Condition

Similar to Example 1.

We need to add order specification to the order list form.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseDocument&block=solution2"/>

It may be convenient that the user can view the order contents without editing it.

  
