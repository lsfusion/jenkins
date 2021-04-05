---
title: 'Recursion (RECURSION)'
---

The *recursion* operator is an operator that creates a [property](Properties.md) which sequentially performs two operations:

1.  Recursively builds an intermediate property (result) with an additional first parameter (operation number) as follows:
    1.  result(0, o1, o2, ..., oN) = initial(o1, ..., oN), where initial is an *initial* property.
    2.  result(i+1, o1, o2, ..., oN) = step(o1, ..., oN, $o1, $o2, ..., $oN) IF result(i, $o1, $o2, ..., $oN), where step is a *step* property.
2.  For all values of the obtained property, it calculates the given [aggregate function](Set_operations.md#func) grouping by all its parameters except the operation number.

Currently, only two types of aggregate functions are supported for the recursion operator: **SUM** and **OR**. The latter is used in the case when the initial value and step are of class **BOOLEAN.** **SUM** is used in all other cases.

Note that sets of objects may begin to repeat after a certain number of iterations. In this case, we say that a cycle is formed. There are three policies for working with cycles:

1.  **CYCLES YES** - cycles are allowed. In this case, when a cycle is detected, the value of the property will be equal to the maximum allowed value for the value class of this property. This policy is not supported when the initial value and step are of class **BOOLEAN**.
2.  **CYCLES NO** (default) - cycles are not allowed. It works similarly to the previous policy, but an additional constraint is created that the value of the obtained property should not be equal to the maximum value (which just means that a cycle has formed for this set of objects).
3.  **CYCLES IMPOSSIBLE** - cycles are impossible. As a rule, it is used if there is a counter among the objects which increases at each iteration and, as a result, cannot be repeated.

When using the recursion operator, it is important to make sure that the first step execution process is finite, that is, the step value will sooner or later become **NULL**. (This refers primarily to a **CYCLES IMPOSSIBLE** policy because otherwise the recursion will stop at the first cycle found). If this condition is not met, the operation will be forced to stop depending on the settings of the SQL server.

### Language

To declare a property that implements recursion, use the [**RECURSION** operator](RECURSION_operator.md).

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=recursion1"/>
