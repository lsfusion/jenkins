---
title: 'READ operator'
---

The **READ** operator creates an [action](Actions.md) that [reads a file](Read_file_READ_.md) to a [property](Properties.md) from an external resource.** **

### Syntax

    READ [CLIENT [DIALOG]] urlExpr [TO propertyId]

### Description

The **READ** operator creates an action that reads a file from an external resource at the URL specified, and then writes the result file to the specified property.

The following URL types are supported: 

    [file://]path_to_file
    [s]ftp://username:password[;charset]@host:port[/path_to_file][?passivemode=true|false]

If the value of the property to which the file is written belongs to the **FILE** class, the file extension from the URL is also written to its value along with the file.

### Parameters

*CLIENT*

Keyword. If specified, the action will be executed on the client side. By default, the action is executed on the server.

*DIALOG*

Keyword. If specified, before writing the file a dialog will be shown in which the user can change the specified URL*.* This can be used only when writing to the disk (the URL type is file).* * By default, the dialog is not shown. 

*urlExpr*

An [expression](Expression.md) whose value is the URL from which to read. The value of the expression must be a string type.

*propertyId*

The [ID of the property](IDs.md#propertyid-broken) to which read data should be written. This property must not have parameters and its value must be of a file class (**FILE**, **RAWFILE**, **JSONFILE**, etc.). If this property is not specified, the **System.exportFile** property is used by default.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=read"/>

  
