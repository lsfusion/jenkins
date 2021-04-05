---
title: 'CONSTRAINT instruction'
---

The **CONSTRAINT** instruction creates a [constraint](Constraints.md).

### Syntax

    CONSTRAINT eventClause constraintExpr [CHECKED [BY propertyId1, ..., propertyIdN]] MESSAGE messageExpr;

### Description

The **CONSTRAINT** instruction creates a constraint. If the constraint is violated, the user will be shown the message defined in the instruction.

Also, by using the **CHECKED** option you can use the constraint when showing dialogs for changing properties whose values may violate the constraint if changed. In this instance an additional filter will be set in the dialog so that, when the property value changes, the constraint is not violated. If it is necessary to limit the set of properties for which the above filtering will be performed, the list of properties can be specified after the keyword **BY** .


:::note
Creating a constraint is pretty similar to the following instruction:

    constraintProperty = constraintExpr;
    WHEN eventClause [=GROUP MAX constraintProperty()]() DO {
        PRINT outConstraintPropertyForm MESSAGE NOWAIT;
        CANCEL;
    }

but it also has [a number of advantages](Constraints.md).
:::

### Parameters

*eventClause*

[Event description block](Event_description_block.md). Describes [the event](Events.md) upon occurrence of which the created constraint will be checked.

*constraintExpr*

An [expression](Expression.md) whose value is a condition for the constraint being created. If the obtained property does not contain the **PREV** operator, the platform automatically wraps it into the **SET**operator.

*propertyId1, ..., propertyIdN*

List of [property IDs](IDs.md#propertyid-broken). When showing change dialog for each property in that list, options that violate the created constraint will be filtered.

*messageExpr*

An expression whose value is shown as a message to the user when the set constraint is violated. It may be either a [string literal](IDs.md#strliteral-broken) or a property without parameters.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=InstructionSample&block=constraint"/>

**  
**
