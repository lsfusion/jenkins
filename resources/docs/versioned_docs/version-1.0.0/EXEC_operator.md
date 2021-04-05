---
title: 'EXEC operator'
---

The **EXEC** operator creates an [action](Actions.md) that [executes](Call_EXEC_.md) another action.

### Syntax

    [EXEC] actionId(expression1, ..., expressionN)

### Description

The **EXEC** operator creates an action that executes another action, passing it the values of [expressions](Expression.md) as parameters.

### Parameters

*actionId*

[Action ID](IDs.md#propertyid-broken). 

*expression1, ..., expressionN*

A list of expressions whose values will be passed to the action being executed as arguments. The number of expressions must be equal to the number of parameters of the action being executed.

*operator*

An operator that creates the action being executed.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=exec"/>

  
