---
title: 'DELETE operator'
---

The **DELETE** operator creates an [action](Actions.md) that [deletes objects](Class_change_CHANGECLASS_DELETE_.md).

### Syntax

    DELETE expr [WHERE whereExpr]

### Description

The **DELETE** operator creates an action that deletes objects for which a certain condition is met. This operator can add its local [parameter](Actions.md), which will correspond to the objects being iterated. In this case, the **WHERE** block is mandatory. 

### Parameters

*expr*

An [expression](Expression.md) or [typed parameter](IDs.md#paramid-broken). You can either use an already declared parameter as a typed parameter, or declare a new local parameter. When using an expression, new local parameters cannot be added.

*whereExpr*

An [expression](Expression.md) whose value is for the action being created. If not set, it is considered as equal to **TRUE**.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=delete"/>

  
