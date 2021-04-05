---
title: 'SEEK operator'
---

The **SEEK** operator creates an [action](Actions.md) that [seeks specified objects](Search_SEEK_.md) on a [form](Forms.md).

### Syntax

    SEEK [FIRST | LAST] formObjectId = expr
    SEEK [FIRST | LAST] formGroupObjectId [OBJECTS formObject1 = expr1, ..., formObjectK = exprk]
    SEEK NULL formGroupObjectId

### Description

The **SEEK** operator creates an action that changes the current objects on a form. There are two forms of this operator. In the first form the required value of a single object on a form is specified (this object may be a part of an object group), in the second form specific object group and the required values for certain objects of this group are specified (these objects shall be called *seek objects*)

If the seek object group contains objects other than seek objects, for these objects (which shall be called *additional*) the object collection that will be selected as current is determined by the options **FIRST** and **LAST**.

If the required object collection is not found for the search objects, the current object collection will be the closest to the required one. The direction in which this closest object collection will be selected is also determined by the options **FIRST** and **LAST**.

Also, in the second form of the operator (when a group of objects is specified) it is possible to reset all objects of the specified group to **NULL**. In this case, the seek direction is not applicable/not specified.

### Parameters

*FIRST*

Keyword. If specified, the current set of objects for:

-   additional objects will be the <u>first</u> matching collection, selected in accordance with the specified order. 
-   main objects, if the required object collection is not found, will be the <u>next</u> closest collection, selected in accordance with the specified order. 

This is the default value. *  
*

*LAST*

Keyword. If specified, the current set of objects for:

-   additional objects will be the <u>last</u>matching collection, selected in accordance with the specified order. 
-   main objects, if the required object collection is not found, will be the <u>previous</u> closest collection, selected in accordance with the specified order. 

*NULL*

Keyword. If specified, the current values of the objects of the specified object group are set as equal to **NULL**.

*formObjectId*

Global [form object ID](IDs.md#groupobjectid-broken) for which the required value is specified.

*expr*

An [expression](Expression.md) whose value is the required value of the form object.

*formGroupObjectId*

A global [ID for an object group](IDs.md#groupobjectid-broken) for whose objects required values are specified.

*formObject1 ...  formObjectK*

List of form object names. May contain only a part of the objects of the specified object group. An object name is defined by a [simple ID](IDs.md#id-broken).

*expr1 ... exprk*

A list of expressions whose values are the required values of the corresponding objects in the specified group of objects.

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=seek"/>

**  
**
