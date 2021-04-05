---
title: 'FORM instruction: Overview'
sidebar_label: Overview
---

The **FORM** instruction creates a [form](Forms.md). 

### Syntax

    FORM name [caption] formOptions
        formBlock1
        ...
        formBlockN
    ;

After specifying the form name and caption, form options *formOptions: * are specified in an arbitrary order

    IMAGE path 
    AUTOREFRESH period 

After the form options, the blocks of the form *formBlock1 ... are described in the arbitrary order. formBlockN*: 

    OBJECTS ... 
    TREE ...
    PROPERTIES ...
    FILTERS ...
    [EXTEND] FILTERGROUP ...
    ORDER ...
    EVENTS ...
    REPORT propertyExpression
    FORMEXTID extID
    EDIT className OBJECT objectName
    LIST className OBJECT objectName 

### Description

The **FORM** instruction declares a new form and adds it to the current [module](Modules.md). In addition, this instruction is used to describe [the form structure](Form_structure.md), as well as its [static](Static_view.md) and partially [interactive](Interactive_view.md) (except [form design](Form_design.md)) views. At the beginning of the instruction, name and captions are specified, followed by form options and the declaration containing an arbitrary number of blocks describing the structure of the form. They can be used in any order, provided that each block is declared after the blocks whose elements it uses. Each block can be used any number of times.

### Parameters

*name*

Form name. [Simple ID](IDs.md#id-broken). The name must be unique within the current [namespace](Naming.md#namespace).

*caption*

Form caption. [String literal](IDs.md#strliteral-broken). If the caption is not defined, the form's name will be its caption.

***Form options** (formOptions)*

IMAGE path

The relative path to the file with the image that will be used as the form icon. 

*path* - path to the file. String literal. The path is relative to the  **images** directory.

AUTOREFRESH period

Specifying the [automatic form update](Interactive_view.md#extra) period. If the option is not specified, the form will not be updated automatically.

*period* - a period of time in seconds. [Integral literal](IDs.md#intliteral-broken). 

***Form blocks** (*formBlock1 ... formBlockN*)*

OBJECTS ...

Adds objects to the form. [Object block syntax](Object_blocks.md) .

TREE ...

Adds an object tree to the form. [Syntax of the object tree block](Object_blocks.md#tree).

PROPERTIES ...

Adds [properties](Properties.md) and [actions](Actions.md) to the form. [Syntax of the property and action block](Properties_and_actions_block.md).

FILTERS ...

Adds fixed filters to the form. [Syntax of the fixed filters block](Filters_and_sortings_block.md#fixedfilters-broken).

\[EXTEND\] FILTERGROUP ...

Adds a group of filters to the form or extends an existing one. [Syntax of a filter group block](Filters_and_sortings_block.md#filtergroup).

ORDER ...

Adds sorting options to the form. [Syntax of the order block](Filters_and_sortings_block.md#sort).

EVENTS ...

Defines actions that are executed on specific events. [Syntax of the event block](Event_block.md#events-broken)[.](Event_block.md)

*FORMEXTID extID*

Specifying the name to be used to [export/import](Structured_view.md#extid) this form. Used only in the [structured](Structured_view.md) view.

*extId*

String literal.

REPORT propertyExpression

        Specifying the property whose value will be used as the name of the report file for an empty group. You can use the names of already declared objects on the form as parameters. It is assumed that the values of these objects will be passed when the form is opened in the print view (if it doesn't happen, they will be considered equal to NULL).

*propertyExpression*

[Expression](Expression.md).

EDIT сlassName OBJECT objectName

Sets the current form as the [edit](Interactive_view.md#edtClass) form for all objects of the specified class.

*className* – the name of the [custom](User_classes.md) . When editing objects of this class, the created form will be opened. Defined with a [composite ID](IDs.md#cid-broken).

*objectName* – the name of the form object that will be used for editing. Defined with a [simple ID](IDs.md#id-broken).

LIST сlassName OBJECT objectName

Sets the current form as the [list form](Interactive_view.md#edtClass) for the object of the specified class. 

*className* – the name of the [custom](User_classes.md) class whose objects will be listed using the created form. Defined with a composite ID.

*objectName* – the name of the form object whose current value will be used as the object being selected. Defined with a simple ID.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=form"/>

  
