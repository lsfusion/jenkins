---
title: 'Selection (CASE, IF, MULTI, OVERRIDE, EXCLUSIVE)'
---

The *selection* operator creates a property that determines for a set of *conditions *which condition is met, and returns the value of the *result* corresponding to that condition. If none of the conditions is met, then the value of the created property will be **NULL**. 

All conditions and results are defined as some properties and/or parameters. Accordingly, a condition *is satisfied* if the value of the property or parameter by which this condition is set is not equal to **NULL**. 

### Polymorphic form {#poly}

The platform also allows you to define a condition and the corresponding result with one property. In this case, the condition may be either matching the property's [signature](Property_signature_CLASS_.md), or the property itself. We will call this the*polymorphic* form of the operator.


:::note
Note that the [extremum operator](Extremum_MAX_MIN_.md) and logical operators basically are also varieties of the selection operator (and of its polymorphic form, i.e. the conditions and result are defined by one property)
:::

### Mutual exclusion of conditions {#exclusive}

The selection operator lets you specify that all its conditions are *mutually exclusive*. If this option is specified, and the conditions are not in fact mutually exclusive, the platform will throw the corresponding error.

It is worth noting that this check is no more than a hint to the platform (for better optimization), and also a kind of self-checking on the part of the developer. However, in many cases it allows you to make the code more transparent and readable (especially with the polymorphic form of the selection operator).

### Implicit definition

This operator supports [implicit definition](Property_extension.md) using the technique of extensions, which allows, in particular, to implement polymorphism in the form that is common practice in OOP.

### Single form {#single}

The *single *form of the selection operator checks exactly one condition. If this condition is met, the value of the specified result is returned. It is also possible to specify an *alternative result *which value is returned if the condition is not met.


:::note
Type of mutual exclusion and implicit definition do not make sense/are not supported for this form of the operator
:::

### Determining the result class

The result class of the selection operator is the common ancestor (builtin or [user-defined](User_classes.md#commonparentclass)) of its operands.

### Language

To create a property implementing a general form of selection, the **[CASE](CASE_operator.md)** operator is used. The polymorphic form of the selection operator is implemented using the **[MULTI](MULTI_operator.md)**, **[OVERRIDE](OVERRIDE_operator.md) and [EXCLUSIVE](EXCLUSIVE_operator.md)**,** operators; the**single form using the **[IF](IF_operator.md)** and **IF ... THEN** operator (the only operator that allows the specification of an alternative result).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=case"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=multi"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=override"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=exclusive"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=if"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=ifthen"/>

**  
**

  
