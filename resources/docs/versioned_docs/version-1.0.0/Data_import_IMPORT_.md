---
title: 'Data import (IMPORT)'
---

The *data import *operator creates an [action](Actions.md) which reads a file from the value of some [property](Properties.md), then, depending on its [format](Structured_view.md), defines the columns (fields) of data in this file, after which it [writes](Property_change_CHANGE_.md) the value of each column (field) to the corresponding property (parameter) - import *assignment*. The mapping of columns to properties can go in either column or name order.

Rows, in turn, are mapped during import to objects of specified classes (let's call these objects *imported*). In the current platform implementation, there can be at most one object and the specified class must be either [numeric](Built-in_classes.md) or a [specific user-defined class](User_classes.md#abstract). Rows are mapped to the imported object as follows:

-   for numeric classes: all imported rows are numbered in the order in which they appear in the file (starting from 0).
-   for specific user-defined classes: [a new object](New_object_NEW_.md) of the specified class is created for each row.

You can also define an import *condition*: this is a property in which the [default value](Built-in_classes.md) of the property value class is written for each row (as opposed to import destination in which column values are written).

### General case

It should be noted that data import is a special case of (syntactic sugar for) [form import](In_a_structured_view_EXPORT_IMPORT_.md#importForm), in which the imported form is created automatically and consists of:

-   one [group of objects](Form_structure.md#objects) named **value** whose objects correspond to imported objects (not created if there are no imported objects)
-   imported properties. The [property group](Form_structure.md#propertygroup-broken) for the properties that are created on the form is the [builtin](Groups_of_properties_and_actions.md#builtin) group **System.private**.
-   a filter equal to the defined condition.

Accordingly, the behavior of the data import operator (for example, determining the names of the resulting columns / keys, [processing of **value**](Structured_view.md#value), etc.) is completely determined by the behavior of the form import operator (as if the above form were passed to it as a parameter).

### Language

To declare an action that imports data, use the [**IMPORT** operator](IMPORT_operator.md).

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=import"/>
