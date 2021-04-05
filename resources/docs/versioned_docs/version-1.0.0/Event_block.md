---
title: 'Event block'
---

The event block of the  [FORM instruction](FORM_instruction.md) - a set of constructions controlling [events](Form_events.md) in an interactive form view.

### Syntax

    EVENTS formEventDecl1, ..., formEventDeclN

Where each *formEventDecli* has the following syntax:

    ON eventType eventActionId(param1, ..., paramK) | { eventActionOperator }

### Description

The event block allows to define handlers for [form events](Form_events.md) that occur as the result of certain user actions. Each block can have an arbitrary number of comma-separated event handlers. If several handlers are defined for an event, they are guaranteed to be executed in the order they are defined. 

### Parameters 

*eventType*

Type of form event. It is specified with one of the following keywords:

-   **INIT** 
-   **OK**
-   **OK BEFORE**
-   **OK AFTER**
-   **APPLY**
-   **APPLY BEFORE** 
-   **APPLY AFTER** 
-   **CANCEL**
-   **CLOSE**
-   **DROP**
-   **CHANGE** objName – specifies that the action will be executed when the object *objName* is changed.
-   **QUERYOK**
-   **QUERYCANCEL**

*eventActionId*

The [ID of the action](IDs.md#propertyid-broken), that will be the event handler.

*param1, ..., paramK*

List of action parameters. Each parameter is specified with the object name on the form. The object name, in turn, is specified with a [simple ID](IDs.md#id-broken).

*actionOperator*

[Context-dependent action operator](Action_operator.md#contextdependent). You can use the names of already declared objects on the form as parameters.

  

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=events"/>


