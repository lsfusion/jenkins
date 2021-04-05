---
title: 'ACTIVE TAB operator'
---

The **ACTIVE TAB** operator creates a [property](Properties.md) that checks if specified tab is [active](Activity_ACTIVE_.md).

### Syntax 

    ACTIVE TAB formName.componentSelector

### Description

The **ACTIVE TAB** operator creates a property that returns **TRUE** if the specified tab is active on a [form](Forms.md). 

### Parameters

*formName*

Form name. [Composite ID](IDs.md#cid-broken).

**componentSelector*  
*

Design component [selector](DESIGN_instruction.md#selector-broken). The component must be a tab in the tab panel.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=OperatorPropertySample&block=activetab"/>
