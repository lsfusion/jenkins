---
title: 'IF ... THEN operator (action)'
---

The **IF ... THEN** operator creates an [action](Actions.md) that implements [branching](Branching_CASE_IF_MULTI_.md#single) with one condition (single form).

### Syntax

    IF condition 
    THEN action
    [ELSE alternativeAction]

### Description

The **IF ... THEN** operator creates an action that implements branching with one condition. When this action is executed, the condition is checked and, if the condition is met, the action specified after the keyword **THEN** is called. There is also the option of specifying an alternative action with the keyword **ELSE**, which is called if the condition is not met.

### Parameters

*condition*

[Expression](Expression.md) defining a condition. If the value of the expression does not equal **NULL**, then the condition is met and the action specified after the keyword **THEN** is called.

*action*

[Context-dependent operator](Action_operator.md#contextdependent) that describes the action that will be executed when the corresponding condition is met.

*alternativeAction*

Context-dependent operator that describes an action that will be executed if the condition is not met.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=ifthena"/>

  
