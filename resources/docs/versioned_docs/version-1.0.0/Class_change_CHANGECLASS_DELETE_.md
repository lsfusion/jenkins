---
title: 'Class change (CHANGECLASS, DELETE)'
---

The *class change* operator creates an [action](Actions.md) that assigns the given [class](Classes.md) to all objects where value of a particular [property](Properties.md) (*condition*) is not equal to **NULL**. The condition can be omitted, in which case it is considered to be equal to **TRUE**.  


:::note
The platform also has a builtin changeClass action with two parameters: the first defines the object for which you want to change the class, and the second defines an object of the new class. Since it is much more difficult to determine the possible values of a new class when using the builtin action than in the case of an operator (for which the new class is specified explicitly), it is recommended that you use the operator (and not the builtin action)
:::

If there is a non-**NULL** value of some [data property](Data_properties_DATA_.md) for which the "changed" object is either its parameter or the value itserf, then this value is automatically changed to **NULL**.


:::note
This behavior is implemented by analogy with [computed](Calculated_events.md) and [simple](Simple_event.md) events.
:::

### Language

To declare an action that implements a change of object classes, use the [**CHANGECLASS** operator](CHANGECLASS_operator.md) or [the **DELETE** operator](DELETE_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=delete"/>


<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=changeclass"/>
