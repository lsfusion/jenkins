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


```lsf
changePrice(Sku s, DATE d, NUMERIC[10,2] price)  { price(s, d) <- price; }
AFTER changePrice(Sku s, DATE d, NUMERIC[10,2] price) DO MESSAGE 'Price was changed'; // A message will be shown after each call to changePrice
```

  
