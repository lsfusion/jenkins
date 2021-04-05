---
title: 'Property change (CHANGE)'
---

The *property change* operator allows you to change the values of one property (*write*) to the value of another property (*read*) for all object collections for which the value of a third property (*condition*) is not **NULL**. The condition can be omitted (in which case it is considered to be equal to **TRUE**).

### Changeable properties {#changeable}

In general, the property to be written should be [data](Data_properties_DATA_.md), but the platform also allows writing to properties created using the [selection](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md) operator. In this case, the platform determines the condition that is met in this selection operator for the created property; the property corresponding to that condition is written to. Accordingly, all properties that can be written to we'll call *mutable*.


:::note
In addition to the above, mutable properties are also properties created using the [extremum operator](Extremum_MAX_MIN_.md) and [logical operators](Logical_operators_AND_OR_NOT_XOR_.md) (which are basically varieties of the selection operator)
:::

### Language

To declare an action that implements property change, use the [**CHANGE** operator](CHANGE_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=assign"/>
