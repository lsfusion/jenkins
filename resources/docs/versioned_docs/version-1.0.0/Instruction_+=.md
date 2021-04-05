---
title: 'Instruction +='
---

The += instruction adds an implementation (selection option) to an [abstract property](Property_extension.md).

### Syntax

    propertyId (param1, ..., paramN) += implExpr;
    propertyId (param1, ..., paramN) += WHEN whenExpr THEN implExpr;

### Description

The += instruction adds an implementation to an abstract property. The syntax for adding an implementation depends on the type of abstract property. If the abstract property is of type **CASE**, then the implementation should be described using **WHEN ... THEN ...** otherwise, the implementation should be described simply as a property. 

### Parameters

*propertyId*

[ID](IDs.md#propertyid-broken) of the abstract property. 

*param1, ..., paramN*

List of parameters that will be used to define the implementation. Each element is a [typed parameter](IDs.md#paramid-broken). The number of these parameters must be equal to the number of parameters of the abstract property. These parameters can then be used in expressions of the implementation of the abstract property and the selection condition of this implementation.

*implExpr*

[Expression](Expression.md) whose value determines the implementation of an abstract property.

*whenExpr*

An expression whose value determines the selection condition of the implementation of an abstract property (action) that has type **CASE**. 

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=extendproperty"/>

  
