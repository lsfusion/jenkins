---
title: 'Filters and sortings block'
---

The filter and order block of the [FORM instruction](FORM_instruction.md) – add [filters](Form_structure.md#filters) and [orderings](Form_structure.md#sort) to the form structure; add [filter groups](Interactive_view.md#filtergroup) to the interactive form view.

Fixed filter block

### Syntax

    FILTERS expression1, ..., expressionN

### Description

The fixed filters block adds filters that will be automatically applied when any form data is read. One block can list an arbitrary number of filters separated by a comma .

Each filter is defined with an  [expression](Expression.md) that defines the filtering condition. In all expressions and context-dependent action operators you can use the names of the objects already declared on the form as parameters.

### Parameters

*expression1, ..., expressionN*

List of filter expressions.

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=filters"/>

  

### Filter group block {#filtergroup}

### Syntax

    [EXTEND] FILTERGROUP groupName
        FILTER caption1 expression1 [keystroke1] [DEFAULT]
        ...
        FILTER captionN expressionN [keystrokeN] [DEFAULT]

### Description

The filter group block adds a set of filters to the form. A special UI component is then created for them, making it possible to apply one filter at a time. If the keyword **EXTEND** is specified , the component is not created, but used for extension. In one block, you can define a single group of filters consisting of an arbitrary number of filters that will be shown to the user in the order of listing. 

Each filter is defined with an [expression](Expression.md) that defines the filtering condition. In all expressions and context-dependent action operators you can use the names of the objects already declared on the form as parameters.

### Parameters

*groupName*

Internal name of a filter group [Simple ID](IDs.md#id-broken). If the **EXTEND** keyword is specified, the platform will search the form for the created filter group with the specified name — otherwise a new filter group with the specified name will be created.

*caption1, ..., captionN*

Captions that will be shown in the user interface for the corresponding filter being added. Each caption is defined with a [string literal](IDs.md#strliteral-broken).

*expression1, ..., expressionN*

Expressions describing filters.

*keystroke1, ..., keystrokeN*

Keyboard shortcuts that, when pressed by the user, will select a corresponding filter in the group. Each keyboard shortcut is defined with a string literal and the definition method is similar to that for a parameter in the Java class method [Keystroke.getKeystroke(String)](http://docs.oracle.com/javase/7/docs/api/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)).

*DEFAULT*

A keyword specifying that the filter being added must be selected automatically when the form is added. Can be specified for one filter in the group only.

  

### Examples


<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=regularfilters"/>

  

### Order block {#sort}

### Syntax

    ORDER formPropertyName1 [DESC] 
          ...
          formPropertyNameN [DESC]

### Description

An order block adds orderings to the form that will be automatically applied when any data are read on it. One block can list an arbitrary number of properties on the form separated by a comma in any sequence. These properties must be added to the form in advance.

### Parameters

*formPropertyName1, ..., formPropertyNameN*

Names of properties or form actions specifying the order.

*DESC*

Keyword. Specifies reverse order. By default, ascending order is used.

### Examples


<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=sort"/>
