---
title: 'Instruction<- WHEN'
---

The **<- WHEN** instruction creates a [calculated event](Calculated_events.md).

### Syntax

    propertyId(param1, ..., paramN) <- valueExpr WHEN eventExpr;

### Description

The **<-WHEN** instruction creates a calculated event for the [property](Data_properties_DATA_.md) specified on the left side of the instruction. This operator ** ** can declare its own local parameters when specifying the property whose value will [change](Property_change_CHANGE_.md). These parameters can then be used in expressions of the condition and value to which the property will change.

Only one calculated event can be defined for a property. 

### Parameters

*propertyId*

[ID of the property](IDs.md#propertyid-broken) whose value will be changed when the event occurs.

*param1, ..., paramN*

[Typed parameters](IDs.md#paramid-broken) properties whose value will be changed when the event occurs. The number of these parameters must be equal to the number of parameters of the property being changed.

valueExpr

The expression to whose value the property value must be changed.

*eventExpr*

An expression whose value is a condition for the generated event.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=setwhen"/>

**  
**
