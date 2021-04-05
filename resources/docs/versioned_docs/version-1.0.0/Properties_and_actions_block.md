---
title: 'Properties and actions block'
---

The property and action [block of the FORM instruction](FORM_instruction.md) adds [properties and actions](Form_structure.md#properties) to the [form structure](Form_structure.md). 

### Syntax

    PROPERTIES [(cparam1, ..., cparamN)] [formPropertyOptions] formPropertyDecl1, ..., formPropertyDeclM

Each *formPropertyDecl* describes the property or action being added to the form structure and has the following syntax:

If the common parameters of the **PROPERTIES** *(cparam1, ..., cparamN)* block have been specified:

    [[alias1] [caption1] =] [ACTION] formPropertyId1 [formPropertyOptions1], ..., [[aliasK] [captionK] =] [ACTION] formPropertyIdK [formPropertyOptionsK] 

If the common parameters of the block have not been specified, then:

    [[alias1] [caption1] =] formMappedProperty1 [formPropertyOptions1], ..., [[aliasK] [captionK] =] formMappedPropertyK [formPropertyOptionsK] 

Each formMappedProperty describes the implementation of a property or action being added to the form and having one of the following syntaxes

    [ACTION] formPropertyId(param1, ..., paramN1) 
    formPropertyExpression 

    { formActionOperator }

Each *formPropertyOptions* specifies options for the property or action being added to the form structure. They can be added one by one in arbitrary order:

    changeType
    SHOWIF propertyExpression
    READONLYIF propertyExpression
    BACKGROUND propertyExpression
    FOREGROUND propertyExpression
    HEADER propertyExpression
    FOOTER propertyExpression
    COLUMNS [groupid] (groupName1, ..., groupNameM)
    viewType
    NEWSESSION | NESTEDSESSION
    DRAW groupObjectName 
    BEFORE formPropertyName
    AFTER formPropertyName
    QUICKFILTER formPropertyName
    ON eventType actionId(param1, ..., paramZ) | { actionOperator }
    ATTR
    EXTID extID
    IN propertyGroup

### Description

One **PROPERTIES** block adds properties and actions to the form structure. To add a property (action), specify its ID and the [form objects](Form_structure.md#objects) whose values will be passed to this property (action) as arguments. Sometimes it’s convenient to list these form objects for the entire **PROPERTIES** block. To do this, specify the block of *common parameters*. In this case, when adding a property (action), you only need to specify its ID, while the common parameters will be passed as arguments.

Each property (action) being added to the form structure must have a *unique name within the form*. This name may be specified either explicitly or based on the name of the property and the passed parameters, i. e. names of objects separated by commas. 

    FORM order
    OBJECTS o=Order 
    PROPERTIES(o) d=date, Order.number;

In the given example, the names of the added properties **date** and **Order.number** will be **d** and **number(o)** respectively.

When adding a property (action) to the form structure, you may specify a set of its options. You may also specify common options for the entire **PROPERTIES** block. If any of the options are specified for both the entire block and a specific declaration, the option value for the declaration will be used.

In all [expressions](Expression.md) and [context-dependent action operators](Action_operator.md#contextdependent), you can use the names of the objects already declared on the form as parameters.

### Parameters

*cparam1, ..., cparamN*

List of common parameters of the block. Each parameter is specified with the name of the object on the form. If this list is defined, then adding a property (action) will require that you specify only its ID without parameters. The object name is specified with [simple ID](IDs.md#id-broken).

*alias*

The name of the property or action being added to the form. [Simple ID](IDs.md#id-broken). If the name is not specified, then the name of the property (action) on the form will be the same as the property's name itself (but without the namespace and signature) with parameters, i. e. names of the objects separated by commas and enclosed in brackets. 

*caption*

The caption of the property or action being added to the form. [String literal](Literals.md#strliteral-broken). If the caption is not specified, then the caption of the property (action) itself will be used on the form.


:::note
In the current platform implementation, if the name and caption are not specified together, then **=** is required when using the expressions and action operators (i. e. f(a,b), but =a\*b+5)
:::

*formPropertyId*

[ID of the property or action](IDs.md#propertyid-broken) being added to the form structure.

Alternatively, you may use [object operators](Interactive_view.md#objectoperators) instead of the property/action IDs:

**VALUE** displays the object value (or the object ID for custom classes).

**NEW** creates a new object.

**EDIT** edits the object.

**DELETE** deletes the object.

**NEWEDIT** creates and edits a new object.

In addition, you may use brackets to explicitly specify a class whose object will be added/edited (e. g. **NEW\[A\]**) for operators **NEW**, **EDIT**, and **NEWEDIT** or for property/action IDs.

*ACTION*

Keyword. When specified, it is considered that the action is specified in formPropertyId. When not specified, it is initially considered that a property is specified in formPropertyId; otherwise, if property is not found, it is considered that an action is specified in formPropertyId.

*param1, ..., paramNk*

List of parameters of the added properties or actions. Each parameter is specified with the name of the object on the form. The number of specified parameters must match the number of parameters for the property or action being added. Not specified if the common parameters block is specified. The object name, in turn, is specified with a [simple ID](IDs.md#id-broken).

*formPropertyExpression*

[Expression](Expression.md) being added to the form structure.

*formActionOperator*

[Context-dependent action operator](Action_operator.md#contextdependent) being added to the form structure

### *Options*** ***for the property or action*

*changeType*

Specifying [standard handlers](Form_events.md#predefined) for property/action change events. It is specified with one of the keywords:

-   **CHANGEABLE** calls the corresponding handler of the triggered event. Default value. It makes sense to use it explicitly only when another modifier is defined for the entire block but should not be applied to a specific property.
-   **READONLY** will either turn on the filtering mechanism or simply ignore the event when the user tries to change the property.
-   **SELECTOR** shows a dialog for changing the current value of the object (not the property value) when the user tries to change the property. Applicable only for properties with one parameter.

*SHOWIF propertyExpression*

Specifies a property that determines visibility of the property or action being added to the form. If the value of this property is **NULL**, then the property (action) being added will not be displayed. Cannot be used together with the **HEADER** option.

*propertyExpression*

[Expression](Expression.md).

*READONLYIF propertyExpression*

Specifies a property that allows or prohibits changing the property being added (or execution of the action being added). If the value of this property is **NULL**, then the property being added will not be changeable, and the action being added will not be executable. The behavior is similar to the **READONLY option**, but with an additional data-dependant condition.

*propertyExpression*

[Expression](Expression.md).

*BACKGROUND propertyExpression*

Specifying a property that determines the background color for the value cell of the property (action) being added. The property may have the **COLOR** type (in this case, its value will be used) or any other type. In this case, if the value is not equal **NULL**, then either the default color stored in the global property **System.defaultBackgroundColor** (set in settings) or yellow color (if the default color is not set) will be used.

*propertyExpression*

[Expression](Expression.md).

*FOREGROUND propertyExpression*

Specifying a property that determines the text color for the value cell of the property (action) being added. The property may have the **COLOR** type (in this case, its value will be used) or any other type. In this case, if the value is not equal to **NULL**, then either the default text color stored in the global property **System.defaultForegroundColor** (set in settings) or red color (if the default color is not set) will be used.

*propertyExpression*

[Expression](Expression.md).

*HEADER propertyExpression*

Specifying a property that determines the header for the column of the property (action) being added. The return value of this property will be used as the header. If the return value is **NULL**, then the added property (action) will be automatically hidden. 

*propertyExpression*

[Expression](Expression.md).

*FOOTER propertyExpression*

Specifying a property that determines the footer of the property (action) being added. Used only in the [print view](Print_view.md) of the form.

*propertyExpression*

[Expression](Expression.md).

*COLUMNS \[groupid\] (groupName1, ..., groupNameM)*

Specifies the upper [object groups](Form_structure.md#objects) whose values will define a set of [columns](Form_structure.md#groupcolumns-broken) to display the added property (action). The **COLUMNS** option is often used with the **HEADER** option which defines headers for these columns.

*groupid*

Column group ID. [String literal](Literals.md#strliteral-broken). If the **COLUMNS** option is specified for several properties (actions) being added with the same set of object groups, then by default the columns of the first property (action) will be added, followed by the columns of the second property (action), and so on. To group columns of different properties (actions) by values of the upper objects, you can set for them the same string ID of the group of columns. In this case, the columns for different properties (actions) will alternate. 

*groupName1, ..., groupNameM*

List of the names of the upper object groups. Each name is defined [by a simple ID](IDs.md#id-broken).

*DRAW groupObjectName*

Specifying the name of the object group on the form where the added property or action will be displayed in the table of the specified object group.

*groupObjectName*

Name of an object group. [Simple ID](IDs.md#id-broken).

*viewType*

Specifying the [view type](Interactive_view.md#property) of the property or action being added:

-   **GRID** — table column
-   **TOOLBAR** — toolbar
-   **PANEL** — panel

If not specified, the corresponding option from the [property options](Property_options.md) is used. If it is also not specified, then the [default view type](Interactive_view.md#defaultPropertyView-broken) is used for the display group of this property or action.

*NEWSESSION | NESTEDSESSION*

Modifiers specifying that object operators (**NEW**, **EDIT**, **DELETE**, **NEWEDIT**) must be executed in a new (nested) session.

*BEFORE formPropertyName*

*AFTER  formPropertyName*

Specifying that a property or an action should be added to the form structure before (keyword **BEFORE**) or after (keyword **AFTER**) the previously added property or action. Typically used in the [form extension](Form_extension.md) concept.

*formPropertyName*

[Property/action name on the form](#name-broken).

*QUICKFILTER formPropertyName*

Specifying the property to be used in [default handlers](Form_events.md#default) for quick filtering.

*formPropertyName*

[Property name/actions on the form](#name-broken).

*ON eventType actionId(param1, ..., paramZ) | { actionOperator }*

Specifying the action that will be executed when the specified [form event](Form_events.md) occurs.

*eventType*

Type of form event. It is specified by one of the following keywords:

-   **CHANGE** — the user tries to change the value of the property being added (call the added action). 
-   **CHANGEWYS** — the user tries to change the displayed value of the property. It is triggered when the user inserts a text into the added property (by pressing Ctrl + V or similarly). 
-   **GROUPCHANGE** — the user tries to change the property value for all objects in the table (group change).
-   **EDIT** — the user tries to edit the object that represents the property value.**  
    **
-   **CONTEXTMENU** \[caption\] is the event type which adds a menu item executing the specified action to the context menu of the property (action) on the form. You can also specify the caption for this menu item (as string literal). If it is not specified, then, by default, it will be the same as the action caption.

*caption*

Caption of the item in the context menu. [String literal](Literals.md#strliteral-broken). If the caption is not specified explicitly, then it will be the same as the caption of the action called upon the event.

*actionId*

[Action ID](IDs.md#propertyid-broken).

*param1, ..., paramZ*

List of action parameters. Each parameter is specified with the object name on the form. The object name, in turn, is specified with a simple ID.

*actionOperator*

[Context-dependent action operator](Action_operator.md#contextdependent).

*ATTR*

Keyword. Used only in the [hierarchical](Structured_view.md#hierarchy) view. Indicates that:

-   When importing from XML, the data is imported from the tag attributes instead of the child tag.
-   When exporting to XML, the data is exported to the tag attributes instead of the child tag.

*EXTID extID*

Specifying the name to be used for [export/import](Structured_view.md#extid) operations of this property. Used only in the [structured](Structured_view.md) view.

*extId*

String literal.

*IN** **propertyGroup*

Specifying a [property/action group](Groups_of_properties_and_actions.md) which the property (action) on the form belongs to. If this option is not specified, then the property group of the property itself will be used as the property (action) group on the form. 

**propertyGroup* – *the property group name. [Composite ID](IDs.md#cid-broken).

### Examples


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=FormSample&block=properties"/>
