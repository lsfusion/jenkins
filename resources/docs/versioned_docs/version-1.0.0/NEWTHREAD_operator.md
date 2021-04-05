---
title: 'NEWTHREAD operator'
---

The **NEWTHREAD** operator creates an [action](Actions.md) that executes another action in a [new thread](New_threads_NEWTHREAD_NEWEXECUTOR_.md).

### Syntax

    NEWTHREAD action [CONNECTION connectionExpr]
    NEWTHREAD action SCHEDULE [PERIOD periodExpr] [DELAY delayExpr]

### Description

The **NEWTHREAD** operator creates an action that executes another action in a new thread. When the **CONNECTION** keyword is used, you can specify the connection which will be used during the action execution. There is also a second form of the **NEWTHREAD** operator for triggering an action using the scheduler. This form usage is determined by the presence of the **SCHEDULE** keyword.  

### Parameters

*action *

A [context dependent operator](Action_operator.md#contextdependent) that defines an action to be executed in the new thread.

*connectionExpr*

An [expression](Expression.md) which value is a [property](Properties.md) that returns an object of the **SystemEvents.Connection** class. Defines the connection for which this action will be performed.  

*periodExpr*

An expression which value is a property that returns the length of the action repetition period in milliseconds. If not specified, the action will be executed once.

*delayExpr*

An expression which value is a property that returns the delay before the first execution of the action in milliseconds. If not specified, the action will be executed without delay.

 **Examples**


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=newthread"/>

  
