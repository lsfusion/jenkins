---
title: 'WHEN instruction'
---

The **WHEN** instruction adds a [simple event](Simple_event.md) handler.

### Syntax 

    WHEN eventClause eventExpr [ORDER [DESC] orderExpr1, ..., orderExprN] DO eventAction;

### Description

The **WHEN** instruction adds a simple event handler. In a condition expression you can implicitly declare local parameters that can then be used in the event handler.

Also, the **ORDER** block can be used to define the order in which the handler will be called for an object collection for which the condition on the simple event has been met. 


:::note
Using the **WHEN** instruction is much like the following instruction:

    ON eventClause FOR eventExpr [ORDER [DESC] orderExpr1, ..., orderExprN] DO eventAction;

but it also has [a number of advantages](Simple_event.md).
:::

### Parameters

*eventClause*

[Event description block](Event_description_block.md). Describes the [base event](Events.md) for the created handler.

*eventExpr*

An [expression](Expression.md) whose value is used as a condition for the created simple event. If the obtained property does not contain the [**PREV**](Previous_value_PREV_.md) operator, the platform automatically wraps it into the [**CHANGE**](Property_change_CHANGE_.md) **operator**.

*eventAction*

A [context-dependent operator](Action_operator.md#contextdependent) that describes an action to be added as an event handler.

*DESC*

Keyword. Specifies a reverse iteration order for object collections. 

*orderExpr1, ..., orderExprM*

A list of expressions that defines the order in which handlers will be called for object collections for which an event condition has been met. To determine the order, first the value of the first expression is used; then, if equal, the value of the second is used, etc. 

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=when"/>

**  
**
