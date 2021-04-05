---
title: 'INTERNAL operator'
---

The **INTERNAL** operator creates an [action](Actions.md) that executes an [internal call](Internal_call_INTERNAL_.md).

### Syntax

The operator has two forms:

    INTERNAL className [(classId1, ..., classIdN)] [NULL]
    INTERNAL <{anyTokens}> [NULL]

### Description

The **INTERNAL** operator creates an action which calls the code written in **Java**. The first form of the operator allows you to specify the fully qualified name of a Java class. This class must be inherited from the **lsfusion.server.physics.dev.integration.internal.to.InternalAction** Java class and must contain the **executeInternal** method which is executed when the action is called.

The second form of the operator allows to write some **Java** code inside the **<{...}\>** block. This block contents will be the code of **executeInternal** method for the generated Java class. In this code you can refer to the only parameter of the **executeInternal** method – the **context** parameter of the **lsfusion.server.logics.action.controller.context.ExecutionContext** class.

### Parameters

*className*

The fully qualified name of the Java class. [String literal](Literals.md#strliteral-broken).

*classId1, ..., classIdN*

A list of [class IDs](IDs.md#classid-broken) of the action arguments. If not specified, the created action will have no parameters.

*NULL*

If this keyword is used, you can pass **NULL** parameters to the action.

*anyTokens*

Source code written in **Java**. 

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=custom"/>

  
