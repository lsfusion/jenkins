---
title: 'NEWSESSION operator'
---

The **NEWSESSION** operator creates an [action](Actions.md) that executes the other action in a [new session](New_session_NEWSESSION_NESTEDSESSION_.md).

### Syntax

    NEWSESSION [NEWSQL] [nestedBlock] action 

where *nestedBlock* has one of two possible syntaxes:

    NESTED LOCAL
    NESTED (propertyId1, ..., propertyIdN)

### Description

The **NEWSESSION** operator creates an action which executes the other action in a new session.

If the **NESTED** keyword is specified, the changes of the [local properties](Data_properties_DATA_.md#local) will be visible in the new session. If the **LOCAL** keyword is specified, changes of all the local properties will be visible, otherwise, a list of the local properties whose changes need to be visible in the new session should be specified. Also, changes to these local properties in the new session will get to the current session when applying changes in this new session.

### Parameters

*NEWSQL*

If this keyword is specified, a new SQL connection will be created. In this case, the block containing the **NESTED** keyword will be ignored.

*LOCAL*

If this keyword is specified, changes to all the local properties will be visible in the new session.

*propertyId1, ..., propertyIdN*

A list of local properties whose changes will be visible in the new session. Each list element must be a [property ID](IDs.md#propertyid-broken).

*action* 

A [context-dependent action operator](Action_operator.md#contextdependent) that defines an action to be executed in the new session.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=newsession"/>

  
