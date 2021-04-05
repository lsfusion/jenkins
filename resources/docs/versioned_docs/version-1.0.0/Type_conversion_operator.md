---
title: 'Type conversion operator'
---

The type conversion operator creates a [property](Properties.md) that performs [type conversion](Type_conversion.md).

### Syntax

    typeName(expression) 

### Description

The operator creates a property that converts the value of a certain expression into a value of a specified [built-in class](Built-in_classes.md). If conversion is impossible, the value of the property will be **NULL**.

### Parameters

*typeName*

The name of the * *[built-in class](Built-in_classes.md) that the values will be converted into.

*expression*

The [expression](Expression.md) whose value will be converted into the value of the specified built-in class.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=explicitcast"/>

**  
**
