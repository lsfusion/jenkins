---
title: 'ASK operator'
---

The **ASK** operator creates an action that shows the user a message in a [dialog form](Show_message_MESSAGE_ASK_.md#dialog).

### Syntax

    ASK expression 
    [[alias =] YESNO]
    [DO actionOperator [ELSE elseOperator]]

### Description

The **ASK** operator creates an action that asks the user for confirmation/asks the user a question.

### Parameters

*expression*

An expression whose value is a message string. If the value is **NULL**, the question will not be asked, but the action will be executed.

*YESNO*

If specified, the user is asked a question (Yes/No)

*alias*

The name of the local parameter in which the user's response to the question will be written (Yes = **TRUE**, No = **NULL**). [Simple ID](IDs.md#id-broken).

*actionOperator*

A [context-dependent action operator](Action_operator.md#contextdependent) that is executed if the input was completed successfully. Both upper parameters and the user response parameter (if the question was Yes/No) can be used as parameters

*elseActionOperator*

A [context-dependent action operator](Action_operator.md#contextdependent) that is executed if the input was cancelled. Only upper parameters can be used as parameters.

### Example


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=ask"/>

  
