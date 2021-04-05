---
title: 'EXTEND FORM instruction'
---

The **EXTEND FORM** instruction [extends](Form_extension.md) an existing [form](Forms.md).

### Syntax

    EXTEND FORM formName 
        formBlock1
        ...
        formBlockN
    ;

### Description

The **EXTEND FORM** instruction allows you to extend an existing form with additional [form blocks](FORM_instruction.md#blocks-broken).

### Parameters

*formName*

The name of the form being extended. [Composite ID](IDs.md#cid-broken) .

*formBlock1 ... formBlockN *

Form blocks.

### Example


```lsf
CLASS ItemGroup;
name = DATA ISTRING[100] (ItemGroup);

itemGroup = DATA ItemGroup (Item);

EXTEND FORM items
    PROPERTIES(i) NEWSESSION DELETE // adding a delete button to the form

    OBJECTS g = ItemGroup BEFORE i // adding a product group object to the form before the product
    PROPERTIES(g) READONLY name
    FILTERS itemGroup(i) == g // if the object was added after the object with products, then filtering would go by the group of products, and not by products
;
```

  
