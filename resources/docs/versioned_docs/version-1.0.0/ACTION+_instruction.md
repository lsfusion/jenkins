---
title: 'ACTION+ instruction'
---

The **ACTION+**  instruction adds an implementation (branching condition) to an [abstract action](Action_extension.md).

### Syntax

    [ACTION] actionId(param1, ..., paramN) + { implAction }
    [ACTION] actionId(param1, ..., paramN) + WHEN whenExpr THEN { implAction }

### Description

The **ACTION+** instruction adds an implementation to an abstract action. The syntax for adding an implementation depends on the type of abstract action. If the abstract action is of type **CASE**, then the implementation should be described using **WHEN ... THEN ...** otherwise, the implementation should be described simply as an action. 

### Parameters

**actionId*  
*

[ID](IDs.md#propertyid-broken) of the abstract action. 

*param1, ..., paramN*

List of parameters that will be used to define the implementation. Each element is a [typed parameter](IDs.md#paramid-broken). The number of these parameters must be equal to the number of parameters of the abstract action. These parameters can then be used in the implementation operator of the abstract property and in the selection condition expression of this implementation.

*implAction*

[Context-dependent action operator](Action_operator.md#contextdependent) whose value determines the implementation of the abstract action. 

*whenExpr*

An [expression](Expression.md) whose value determines the selection condition of the implementation of an abstract property (action) that has type **CASE**. 

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=extendaction"/>

  
