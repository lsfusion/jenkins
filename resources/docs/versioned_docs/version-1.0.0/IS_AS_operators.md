---
title: 'IS, AS operators'
---

**IS**, **AS** operators create an [action](Properties.md) that implements [classification](Classification_IS_AS_.md).

### Syntax

    expression IS className
    expression AS className

### Description

The **IS** operator creates an action which returns **TRUE** if the value of the [expression](Expression.md) belongs to the specified class.

The **AS** operator creates a property which returns the expression value if this value belongs to the specified class.

### Parameters

*expression*

An expression which value is checked for belonging to the class.

*className*

Class name. [Class ID](IDs.md#classid-broken).

### Examples 


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=isas"/>

**  
**
