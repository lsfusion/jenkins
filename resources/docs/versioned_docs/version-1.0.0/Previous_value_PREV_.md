---
title: 'Previous value (PREV)'
---

The *previous value* operator creates a [property](Properties.md) that returns the value of the specified property at the beginning of the session (that is, the current value in the database ignoring the session changes).

:::caution
This operator is calculated differently inside the [event](Events.md#change) handling: here it returns the value at the time of the previous occurrence of this event (or rather, at the time when all its handling were completed).
:::

### Language

To declare a property that returns a previous value, use the [**PREV** operator](PREV_operator.md). 

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=prev"/>
