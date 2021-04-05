---
title: 'NAVIGATOR instruction'
---

The **NAVIGATOR**  instruction is used to modify the [navigator](Navigator.md).

### Syntax

The syntax consists of nested blocks of *navigator instructions.* The outer instruction block is described after the keyword **NAVIGATOR**:

    NAVIGATOR {
        navigatorStatement1 
        ...
        navigatorStatementN
    }

Each *navigatorStatement* describes a single navigator instruction, and at the end it may contain a nested block of additional navigator instructions. There are three types of navigator instructions:  

    NEW elementDescription [options] [{ ... }];
    MOVE name [caption] [options] [{ ... }];
    name [caption] [options] [{ ... }];

where *elementDescription* in the **NEW** instruction describes the type of element to be created, and can be in one of three types:

    FOLDER name [caption] 
    FORM [name [caption] =] formName
    ACTION [name [caption] =] actionName
    [name [caption] =] formElseActionName

A navigator instruction must end with a semicolon if it does not contain a nested block of additional instructions.

The options for the *options* navigator element can be listed one after the other in arbitrary order. The following set of options is supported:

    WINDOW windowName
    BEFORE elementName
    AFTER elementName
    FIRST 
    IMAGE fileName

### Description

The **NAVIGATOR** instruction allows to modify the navigator. Each navigator instruction block enclosed in braces allows to change the descendants of a particular [navigator element](Navigator.md), which we will call the *current* element. In the outer block that follows the **NAVIGATOR** keyword, the current element is the root system folder **System.root**. There are three types of navigator instructions:

-   The *create instruction* (**NEW**) allows to create a new navigator element, making it a child object of the current element. The created navigator elements can be of three types: folder, form element, and action element. The type of element to be created is specified by the keywords **FOLDER**, **FORM, and ACTION**. When creating a form element, the keyword **FORM** is optional. The navigator instruction block contained in this instruction (if any) describes the descendants of the element being created.
-   The *move instruction* (**MOVE**)** **allows to move an existing element to the current navigator element, making it a child element. Prior to this, the navigator element being moved is deleted from its previous location. The navigator instruction block contained in this instruction describes the descendants of the element being added. 
-   The *modify instruction* allows to change the specified navigator element, which must be a descendant (not necessarily a child) of the current element. The navigator instruction block contained in this instruction describes the descendants of the specified element.

The move and modify instructions allow to change the caption and options of a navigator item.

The hierarchy described within a single **NAVIGATOR** instruction can have an arbitrary nesting level and describe any number of elements at each level.

### Parameters

*name*

Navigator element name. In the create instruction, it is [a simple ID](IDs.md#id-broken) and must be unique within the current [namespace](Naming.md#namespace), while in the rest of the instructions it is a [composite ID](IDs.md#cid-broken). The name does not have to be specified when creating a form element or action element. Here the form name or action name is used as the element name.

*caption*

Navigator element caption. [String literal](Literals.md#strliteral-broken). In the create instruction, if the caption is not specified, the caption will be the name of the created element. In the rest of the instructions it modifies the existing caption.

*actionName*

The [action ID](IDs.md#propertyid-broken) for which the navigator element will be created. The action must not take any parameters.

*formName*

The [form ID](IDs.md#propertyid-broken) for which the navigator element will be created.

*formElseActionName*

The [form or action ID](IDs.md#propertyid-broken) for which the navigator element will be created. It is initially assumed that in *formElseActionName *a form is specified, and that only if it is not found an action is specified in*formElseActionName * The action must not take any parameters.

***Element options **(options)*

WINDOW windowName

Specifying a [window](Navigator_design.md) in which the element and its descendants will be displayed (unless another window is specified for them). 

*windowName*

Window name. Composite ID.

BEFORE elementName

AFTER elementName 

Specifying that the element must be added or moved before (keyword **BEFORE**) or after (keyword **AFTER**) the specified navigator element. The specified element must be a child of the current element. If the option is specified in the modify instruction, then the element itself must also be a child of the current element. Otherwise, the **MOVE** instruction should be used.

*elementName*

Navigator element name. Composite ID. 

FIRST

A keyword that specifies that the element must be added or moved to first place in the child list of the current element. If the option is specified in the modify instruction, then the element itself must also be a child of the current element. Otherwise, the **MOVE** instruction should be used.

IMAGE fileName

Specifying the relative path to the file with the image to be displayed as the icon for the navigator element. 

*fileName*

Path to the file. String literal. The path is relative to the  **images** directory.

### Examples


```lsf
FORM items;
FORM stocks;
FORM legalEntities;
FORM shipments;
hello()  { MESSAGE 'Hello world'; }
hi()  { MESSAGE 'Hi'; }

NAVIGATOR {
    NEW FOLDER catalogs 'Directories' WINDOW toolbar { // creating a new navigator folder and making all its descendants appear in a window with a vertical toolbar
        NEW items; // creating a form element for the items form in the folder, the default element name is the form name
    }
    catalogs {  // navigator element editing instruction
        NEW FORM stocksNavigator 'Warehouses' = stocks; // creating a stocksNavigator form element for the stocks form and adding the last element to the catalogs folder
        NEW legalEntities AFTER items; // creating a form element named legalEntities in the catalogs folder right after the items element
        NEW shipments;
    }
    NEW FOLDER documents 'Documents' WINDOW toolbar { // creating another folder, the elements of which will also be displayed in a window with a vertical toolbar
                                                      // the folders themselves will be displayed in the root window, and when the user selects one of them in a window with a vertical
                                                      // toolbar the descendants of this particular folder will be shown
        NEW ACTION hi;   // creating an action element
        NEW ACTION h=hello;   // creating an action element
        MOVE shipments BEFORE h; // an instruction to move the shipments element from the catalogs folder to the document folder before the hello element
    }
}
```


