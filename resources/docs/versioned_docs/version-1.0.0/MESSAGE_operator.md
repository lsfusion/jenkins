---
title: 'MESSAGE operator'
---

The **MESSAGE** operator creates an [action](Actions.md) that shows the user a [message](Show_message_MESSAGE_ASK_.md).** **

### Syntax

    MESSAGE expression [syncType]

### Description

The **MESSAGE** operator creates an action which shows a window with a text message to the user. The text message can be a string constant or a more complex [expression](Expression.md) which value is or can be converted to a string.

### Parameters

*expression*

An expression which value is the message text.

*syncType*

Specifies when the created action should be completed:

-   **WAIT** - after the client completes the action (closes the message). This value is used by default.
-   **NOWAIT** - right after the information is ready for sending to the client (the message text is read). If several **MESSAGE** **NOWAIT** actions are called during the execution of some continuous action, they do not create separate messages but are concatenated with the previous messages of the same type. A single concatenated message will be shown to the user at the end of the continuous action as a result.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=message"/>

  
