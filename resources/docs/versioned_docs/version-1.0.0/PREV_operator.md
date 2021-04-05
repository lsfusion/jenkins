---
title: 'PREV operator'
---

The **PREV** operator creates a property using a [previous value operator](Previous_value_PREV_.md).

### Syntax

    PREV(propExpr)

### Description

The **PREV** operator creates a property that returns the value of another property at the start of the current session (or at the time of the previous event in [event](Events.md#change) mode) - i.e., the value that existed before the changes that were made in the current session.


:::note
It's important to understand that **PREV** is not a built-in property with [composition](Composition_JOIN_.md) but an operator. Thus, in particular **PREV**(f(a)) is not equal to \[**PREV**(a)\](f(a)).
:::

### Parameters

*propExpr*

[Expressions](Expression.md) whose value defines the property for which the previous value must be obtained.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=prev"/>

**  
**
