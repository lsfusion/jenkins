---
title: 'Object group operator'
---

[Object group](Form_structure.md) operators are used for creating [properties](Properties.md) working with the [current state](Object_group_operations.md) of the object group on the [form](Forms.md).

### Syntax

    FILTER groupObjectId
    VIEW groupObjectId
    ORDER groupObjectId

### Description

Object group operators create properties that accept the same number of parameters as the number of objects in the object group. These operators cannot be used inside [expressions](Expression.md).

The **FILTER** operator creates a property which value is **TRUE** when the object collection passed as parameters meets all the [filtering](Form_structure.md#filters) conditions on the form, otherwise the property value will be **NULL**.

The **VIEW** operator creates a property which value is **TRUE** if the object collection passed as parameters is currently displayed on the form, otherwise, the property value will be **NULL**.

The **ORDER** operator creates a property which value determines the relative order of the object collection on the form passed as a parameter. The value of this property is usually used in **ORDER** blocks of the other properties, for example, **[PARTITION](PARTITION_operator.md)**, **[FOR](FOR_operator.md)**, etc.

### Parameters

*groupObjectId*

Global [object group ID](IDs.md#groupobjectid-broken).

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=groupobject"/>

**  
**
