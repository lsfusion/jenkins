---
title: 'ON instruction: Overview'
sidebar_label: Overview
---

The **ON** instruction adds an [event](Events.md) handler.

### Syntax 

    ON eventClause eventAction;

### Description

The **ON** instruction adds an event handler for the specified event. 

### Parameters

*eventClause*

This [event description block](Event_description_block.md) describes an event for which a handler needs to be added.

*eventAction*

This [context-dependent action operator](Action_operator.md#contextdependent) describes the event handler.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=on"/>

 

*  
*
