---
title: 'AFTER instruction'
---

The **AFTER** instruction  calls an [action](Actions.md) after calling another action. 

### Syntax

    AFTER action(param1, ..., paramN) DO aspectAction;

### Description

The **AFTER** instruction defines an action (let's call it an *aspect*) that will be called after the specified action.

### Parameters

*action*

The [ID](IDs.md#propertyid-broken) of the action after which the aspect will be called.

*param1, ..., paramN*

List of action parameter names. Each name is defined [by a simple ID](IDs.md#id-broken). These parameters can be accessed when defining an aspect.

*aspectAction*

A [context-dependent action operator](Action_operator.md#contextdependent) describing the aspect.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=after"/>

  
