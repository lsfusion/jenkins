---
title: 'ACTIVATE operator'
---

The **ACTIVATE** operator creates an [action](Actions.md) that [activates](Activation_ACTIVATE_.md) a specified [form](Forms.md), tab, property, or action on a form

### Syntax 

    ACTIVATE FORM formName
    ACTIVATE TAB formName.componentSelector
    ACTIVATE PROPERTY formPropertyId

### Description

The **ACTIVATE** operator creates an action that activates a form, a tab, a property or an action on a form. 

### Parameters

*formName*

Form name. [Composite ID](IDs.md#cid-broken).

*componentSelector*

Design component [selector](DESIGN_instruction.md#selector-broken). The component must be a tab in the tab panel.

*formPropertyId*

The global [ID of a property or action on a form](IDs.md#formpropertyid-broken) which should get focus.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=activate"/>

  
