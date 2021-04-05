---
title: 'RECURSION operator'
---

The **RECURSION **operator creates a [property](Properties.md) that implements [recursion](Recursion_RECURSION_.md).

### Syntax 

    RECURSION initialExpr STEP stepExpr [CYCLES YES | CYCLES NO | CYCLES IMPOSSIBLE]

### Description

The **RECURSION** operator creates a property that implements recursion. [Expressions](Expression.md) that describe the next step of the recursion may access not only the property parameters but also the parameters at the previous step. This access has the syntax **$name**, where **name** is the name of the parameter.

### Parameters

*initialExpr*

An expression whose value is the initial property.

*stepExpr*

An expression whose value is a property of a recursion step. Allows a special syntax (**$name**) to access the value of the **name** parameter in the previous step.

*CYCLES YES*

        Specifies that cycles are allowed.

*CYCLES NO*

        Specifies that cycles are not allowed. This option is used by default.

*CYCLES IMPOSSIBLE*

        Specifies that cycles are not possible.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=recursion1"/>

**  
**

Note that Fibonacci numbers can be implemented without adding the to parameter:

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=recursion2"/>

In the current implementation, however, the platform optimizer is less focused on working with numbers, so it cannot yet determine that the step function is increasing and stop the recursion on its own, artificially creating the corresponding condition, as is done in the above example. Even more questions arise when this property needs to be displayed in a dynamic list (and in a static list this cannot be done at all, since the number of non-**NULL** values is infinite). In this case, the current order in this list must also be taken into account and also pushed into the query. These limitations will be removed in future versions, but in the current version it is recommended to take them into account.
