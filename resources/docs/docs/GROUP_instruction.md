---
title: 'GROUP instruction'
---

The **GROUP** instruction creates a new [property group](Groups_of_properties_and_actions.md).

### Syntax

    GROUP name [caption] [EXTID extID] [: parentName];

### Description

The  **GROUP **instruction declares a new property group and adds it to the current [module](Modules.md).  

### Parameters

*name *

Group name. [Simple ID](IDs.md#id-broken). The name must be unique within the current [namespace](Naming.md#namespace).

*caption*

Group caption. [String literal](Literals.md#strliteral-broken). If the caption is not defined, the name of the group will be its caption.  

*EXTID extID*

Specifying the name to be used to [export/import](Structured_view.md#extid) this property group. Used only in the [structured](Structured_view.md) view.

*extId*

String literal.

*parentName*

Name of the parent group. [Сomposite ID](IDs.md#cid-broken). If the name of the parent group is not defined, the **System.private** group becomes the parent.  

### Examples

```lsf
GROUP base : root; // The caption of this group will be 'base'
GROUP local 'Local properties'; // The parent group of local will be System.private
```

