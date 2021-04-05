---
title: 'How-to: CRUD'
---

## Example 1

### Task

We have a set of predefined book types.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCRUD&block=sample1"/>

We need to create a form to select a type from the list.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCRUD&block=solution1"/>

*DIALOG* indicates that this form will be used for selecting a type from the list (e. g. when the user wants to change the book type).

## Example 2

### Task

We have a set of books with given titles.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCRUD&block=sample2"/>


:::note
It is recommended that you add all the **name** properties to the **id** group. Values of this property will help identify the object in case of the constraint violations. It will also be added to automatic forms when no edit (EDIT) or list (LIST) forms are defined for the class.
:::

  

We need to create a form with a list of books where the user can add, edit or delete them.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCRUD&block=solution2"/>

## Example 3

### Task

We have a set of book genres with given titles.

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCRUD&block=sample3"/>

We need to create a form with a list of genres where the user can add, edit or delete them, and one more form with a list of genres but without these options.

### Solution

<CodeSample url="https://documentation.lsfusion.org/sample?file=UseCaseCRUD&block=solution3"/>

Use this scheme (with three forms instead of two) when you want to allow users to select genres and prevent any accidental changes to the genre information. In this case, the user will be able to edit genres only on a dedicated form.
