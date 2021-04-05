---
title: 'ACTIVE FORM operator'
---

The **ACTIVATE FORM** operator creates an [action](Actions.md) that checks the [activeness](Activity_ACTIVE_.md) of a [form](Forms.md).

### Syntax

    ACTIVE FORM formName

### Description

The **ACTIVE FORM** operator creates an action that writes the activeness value of the specified form to the **System.isActiveForm \[\]** property. If the form is active, **TRUE** is written.

### Parameters

*formName*

Form name. [Composite ID](IDs.md#cid-broken).

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=activeform"/>

  
