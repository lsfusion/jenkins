---
title: 'How-to: Data entry'
---

## Example 1

### Condition

We have a form that displays a list of books. We need to implement an option for entering a name only in upper case. Group change, copy&paste, and similar features must also be supported.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInput&block=sample1"/>

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInput&block=solution1"/>

  

## Example 2

### Condition

We have a form that displays a list of books. In this form, the user can specify a genre, so that only books of this genre will be displayed. We also have a form containing the list of orders where you can also apply a filter by genre. Each book has a restricted/allowed flag, and the order may contain only allowed books.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInput&block=sample2"/>

We need to replace the book selection mechanism on the order form so that the genre selection form is called. Additional requirements:

-   The genre specified in the order form must be set as default value for the filter by genre
-   The current book from the order must be set as default value (if it has been selected)
-   The book in the order must be resettable (i. e. it must be possible to set the "Undefined" value)
-   Only allowed books can be selected
-   Group change, copy&paste, and similar features must also be supported for the field.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseInput&block=solution2"/>

  
