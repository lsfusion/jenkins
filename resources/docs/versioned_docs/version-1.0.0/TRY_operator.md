---
title: 'TRY operator'
---

The **TRY** operator creates an [action](Actions.md) that executes another action with [exception handling](Exception_handling_TRY_.md).

### Syntax

    TRY action [CATCH catchAction] [FINALLY finallyAction]

### Description

The **TRY** operator creates an action that executes another action and handles exceptions within it. 

An operator form without the **FINALLY** keyword creates an action that executes another action and intercepts errors that are thrown in it. In this case, the error is not passed anywhere.

An operator form with the **FINALLY** keyword creates an action that executes another action, intercepts errors that occur, then, regardless of whether an error was thrown or not, executes the action specified in the **FINALLY** block, and then, if an error was thrown, passes this error to the top action in the stack.

### Parameters

*action*

A [context-dependent operator](Action_operator.md#contextdependent) that describes an action to be executed with exception handling.

*catchAction*

A context-dependent operator that describes an action to be executed if an error is thrown while executing the action. Here the error message will be written to the property **System.messageCaughtException \[\]**, the java error stack will be written to **System.javaStackTraceCaughtException \[\]**, and the LSF stack will be written to ****System.lsfStackTraceCaughtException \[\].****

*finallyAction*

A context-dependent operator that describes an action to be executed after the action being executed, regardless of whether or not an error has been thrown.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=try"/>

**  
**
