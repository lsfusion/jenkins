---
title: 'BEFORE instruction'
---

The **BEFORE** instruction calls an [action](Actions.md) before calling another action. 

### Syntax

    BEFORE action(param1, ..., paramN) DO aspectAction;

Description

The **BEFORE** instruction defines an action (let's call it an *aspect*) that will be called before the specified one.

### Parameters

*action*

The[ID](IDs.md#propertyid-broken) of the action before which the aspect will be called.

*param1, ..., paramN*

List of action parameter names. Each name is defined [by a simple ID](IDs.md#id-broken). These parameters can be accessed while defining an aspect.

*aspectAction*

A [context-dependent action operator](Action_operator.md#contextdependent) describing the aspect.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=before"/>

  
