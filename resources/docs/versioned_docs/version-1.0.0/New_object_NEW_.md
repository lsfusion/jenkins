---
title: 'New object (NEW)'
---

The *new object* operator creates an [action](Actions.md) that creates objects of a specified [custom class](User_classes.md) for object collections where the value of some [property](Properties.md) (*condition*) is not **NULL**. The condition can be omitted, in which case it is considered to be equal to **TRUE**.

This operator also allows you to set a [primary property](Data_properties_DATA_.md), to whose values the added objects will be written. If no condition is specified, by default this property is considered to be **addedObject**.

The custom class whose objects will be created by this operator must be concrete.

You can also create objects using the corresponding [option](Loop_FOR_.md#addobject) in the [loop](Loop_FOR_.md) operator.

### Language

To declare an action that implements objects creation, use the [**NEW** operator](NEW_operator.md). The **NEW** option in the [**FOR** operator](FOR_operator.md) is also used to implement similar functionality.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=new"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=for"/>
