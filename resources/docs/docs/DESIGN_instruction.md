---
title: 'DESIGN instruction'
---

The **DESIGN** instruction changes [form design](Form_design.md).

### Syntax

The syntax consists of nested blocks of *design instructions.* The outer block, beginning with the keyword **DESIGN**, defines a [form](Forms.md) whose design will change: 

    DESIGN formName [caption] [CUSTOM] {
        designStatement1
        ...
        designStatementN
    }

Each  *designStatement * describes one design instruction. Design instructions are of the following types: 

    NEW name [insertPos] [{...}];
    MOVE selector [insertPos] [{...}];  
    selector [{...}];   
    REMOVE selector;
    propertyName = value;

The first three instructions – *create* (**NEW**), *move* (**MOVE**), and *modify *– may in turn contain nested blocks of design instructions. The design instructions *remove* (**REMOVE**) and *change property value* (**=**) are simple single instructions. Each navigator instruction must end with a semicolon if it does not contain a nested instruction block.

Each *selector* can be one of the following types:

    componentName
    PROPERTY(formPropertyName)
    FILTERGROUP(filterGroupName)
    PARENT(selector)
    GROUP([propertyGroupSelector][,groupObjectTreeSelector])
    noGroupObjectTreeContainerType
    groupObjectTreeContainerType(groupObjectTreeSelector)

In turn, *groupObjectTreeSelector* can be one of two types:

    groupObjectSelector
    TREE treeSelector

### Description

Using the **DESIGN** instruction the developer can manage the [design](Form_design.md) [of the](Interactive_view.md) interactive form view by creating, moving, and deleting containers and components, as well as changing their certain properties. By default, a [default design](Form_design.md#defaultDesign)  is created for each form, along with appropriate containers. If necessary, you can recreate the design without the default containers and previously configured settings. This is done using the keyword **CUSTOM**.  

Each block of design instructions enclosed in braces alows to modify a particular component and its descendants. Let's call this component the *current component* or the *current container* if we know that the component should be a container in our case. In the external block following the  **DESIGN **keyword, the **main** container is the current component. There are the following design instructions:

-   The *create instruction* (**NEW**) allows to create a new container, making it a descendant of the current one. The newly-created container will be the current component in the design instructions block contained in this instruction.
-   The *move instruction* (**MOVE**)  allows to make an existing component a direct descendant of the current container. This component is first removed from the previous parent container. The component being moved becomes the current component in the design instructions block contained in this instruction. 
-   The *modify* instruction allows to modify the specified component which must be a descendant (not necessarily a child) of the current container. The specified element will be the current component in the design instructions block contained in this instruction.
-   The *remove instruction*(**REMOVE**) allows to remove a specified component from the component hierarchy. The component to be removed has to be a descendant of the current container. 
-   The *change property value instruction*(**=**) allows to change the value of the specified property of the current component.

The component hierarchy described in this instruction can have an arbitrary number of nesting levels and describe any number of components and their properties at each level.

To access design components, you can use their names or address property components on the form (**PROPERTY**), the parent component (**PARENT**), property group components (**GROUP**), and other base components/default design components.

### Parameters

### *Common parameters*

*formName*

The name of the form being changed. [Composite ID](IDs.md#cid-broken).* *

*caption*

The new form caption in the interactive view mode. [String literal](Literals.md#strliteral-broken). The form caption doesn't change in the [navigator](Navigator.md).

*name*

The name of the container being created. [Simple ID](IDs.md#id-broken).

*insertPos*

Component insertion or moving position. Specified with one of the following options:*  
*

**BEFORE** selector

**AFTER** selector 

Specifies that the component should be added or moved before (**BEFORE**) or after (**AFTER**) the specified components. The specified component must be a child of the current container. 

**FIRST**

A keyword specifying that the component should be added or moved to the first position in the list of the current container's children. 

*propertyName*

The name of the component property. The list of existing properties is provided in the tables below.

*value*

The value assigned to the corresponding container property. Acceptable value types are provided in the tables below.

### *Component properties*

|Property name|Description|Value type|Default value|Examples|
|---|---|---|---|---|
|size|The base component size in pixels (a value of -1 means that the size is undefined)|A pair [of integer literals](Literals.md#intliteral-broken) (width, height)|(-1, -1)|(100, 20)|
|height|The base component height in pixels.|Integer literal|-1|50|
|width|The base component width in pixels.|Integer literal|-1|20|
|<p>background</p>|<p>The color to be used for the component background</p>|[Literal of class <strong>COLOR</strong>](Literals.md#colorliteral-broken)|#FFFFFF|#FFFFCC, RGB(255, 0, 0)|
|foreground|The color to be used for the component text|Color |<strong>NULL</strong>|#FFFFCC, RGB(255, 0, 0)|
|font|<p>The font to be used for displaying the component text — for example, property value, action caption, table text</p>|[String literal](Literals.md#strliteral-broken)|depends on the component|'Tahoma bold 16', 'Times 12'|
|fontSize|The size of the font to be used for displaying the component text|Numeric literal|depends on the component|10|
|fontStyle|The style of the font to be used for the component text May contain the words 'bold' and/or 'italic', or an empty string|String literal|''|'bold', 'bold italic'|
|defaultComponent|<p>Specifying that this component should get the focus when the form is initialized. Can only be set for one component on the entire form</p>|Extended [Boolean literal](Literals.md#booleanliteral-broken)|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|align, alignment|Component alignment inside the container. Acceptable values: <strong>START</strong> (at the beginning), <strong>CENTER</strong> (in the center), <strong>END</strong> (at the end), <strong>STRETCH</strong> (stretched).|Alignment type|<strong>START</strong>|<strong>STRETCH</strong>|
|flex|Extension coefficient. Value of a property similar to the [CSS flex-grow](http://www.w3schools.com/cssref/css3_pr_flex-grow.asp) property. Defines how much the component should grow in size relative to other components.|[<strong>NUMERIC</strong> type literal](Literals.md#numericliteral-broken)|0|0.25|
|fill|Similar to the *flex* property, the only difference being that if a zero value is set, the *align* property is set to <strong>START</strong>, otherwise *align* is set to <strong>STRETCH</strong>|<strong>NUMERIC</strong> type literal|0|<p>1.5</p>|
|noSort |No sorting|[Logical literal](Literals.md#booleanliteral-broken)|<strong>FALSE</strong>|<p><strong>TRUE</strong>, <strong>FALSE</strong></p>|
|defaultCompare|Default filter. Acceptable values: <strong>EQUALS</strong>, <strong>GREATER</strong>, <strong></strong> <strong>LESS</strong>, <strong></strong> <strong>GREATER_EQUALS</strong>, <strong></strong> <strong>LESS_EQUALS</strong>, <strong>NOT_EQUALS</strong>, <strong>START_WITH</strong>, <strong>CONTAINS</strong>, <strong></strong> <strong>ENDS_WITH</strong>, <strong>LIKE.</strong>|String literal|<strong>CONTAINS</strong>|<p><strong>START_WITH</strong></p>|
|marginTop|Top margin|Integer literal|0|3|
|marginRight|Right margin|Integer literal|0|1|
|marginBottom|Bottom margin|Integer literal|0|4|
|marginLeft|Left margin|Integer literal|0|1|
|margin|Margin. Sets the same value to the following properties: *marginTop*, *marginRight*, *marginBottom*, *marginLeft*|Integer literal|0|5|

### *Container properties*

|Property name|Description|Value type|Default value|Examples|
|---|---|---|---|---|
|caption|Container header|String literal|<strong>NULL</strong>|'Caption'|
|type|<p>Container type. Acceptable values:</p><br/><p><strong>CONTAINERV</strong> - vertical container</p><br/><p><strong>CONTAINERH</strong> - horizontal container</p><br/><p><strong>COLUMNS</strong> - column container</p><br/><p><strong>SPLITV</strong> - vertical splitter</p><br/><p><strong>SPLITH</strong> - horizontal splitter</p><br/><p><strong>TABBED</strong> - tabbed panel</p><br/><p><strong>SCROLL</strong>– a scrollable container, cannot have more than one child</p>|Container type|<strong>CONTAINERV</strong>|<strong>CONTAINERH, TABBED</strong>|
|childrenAlignment|Alignment of child components inside a container. Acceptable values: <strong>START</strong>, <strong>CENTER</strong>, <strong>END</strong>|Alignment type|<strong>START</strong>|<strong>CENTER</strong>|
|columns|Number of columns in a <strong>COLUMNS</strong> type container|Integer literal|4|3|
|columnLabelsWidth|Width of component captions in a <strong>COLUMNS</strong> type container.|Integer literal|0|50|
|showIf|Specifies a condition under which the container will be displayed.|[Expression](Expression.md)|<strong>NULL</strong>|isLeapYear(date), hasComplexity(a, b)|

### *Properties of actions and properties on the form*

|Property name|Description|Value type|Default value|Examples|
|---|---|---|---|---|
|caption|Caption of a property or action|String literal|caption of a property or action|'Caption'|
|askConfirm|Specifies that an attempt to change the property (execute an action) will show a confirmation request|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|askConfirmMessage|Text of the confirmation request shown when an attempt to change the property (execute the action) is made|String literal|default message|'Are you sure you want to modify this property?'|
|autoSize|Automatic component size option. Applies to text components only|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|valueWidth|Width of the property value cell in pixels|Integer literal|depends on the property|100|
|charWidth|Width of the property value cell in characters|Integer literal|depends on the property|10|
|charHeight|Height of the property value cell in characters (rows).|Integer literal|depends on the property|2, 3|
|clearText|Specifying that the current text should be reset when input starts|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|echoSymbols|Specifying that a set of '*' characters will be displayed instead of the property value. Used for passwords, for example|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|changeKey|<p>The key that will trigger the property change event. The definition principle is similar to specifying a parameter in [Keystroke.getKeystroke(String)](https://docs.oracle.com/javase/8/docs/api/javax/swing/KeyStroke.html#getKeyStroke-java.lang.String-)</p>|String literal|<strong>NULL</strong>|'ctrl F6', 'BACK_SPACE', 'alt shift X'|
|showChangeKey|Specifying that the property caption will include that name of the key shortcut that will trigger the change event|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|editOnSingleClick|Specifying that change event should be triggered after the property component is clicked once|Extended Boolean literal|depends on the property|<strong>TRUE</strong>, <strong>FALSE</strong>|
|focusable|Specifying that the property (action) component or a table column can get focus|Extended Boolean literal|changeKey = <strong>NULL</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|captionFont|The font that will be used to display the property caption|String literal|depends on the component|'Tahoma bold italic 16', 'Times 12'|
|hide|Specifying that the property (action) component should be always hidden|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|imagePath|The path to the file with the image to be displayed as an action icon. The path is specified relative to the <strong>images</strong> folder|String literal|<strong>NULL</strong>|'image.png', 'pring.png' |
|maxValue|The maximum numerical value that the property component can have|Integer literal|<strong>NULL</strong>|1000000, 5000000000L|
|notNull|Specifies that in case of a <strong>NULL</strong> property value, the component of this property should be highlighted|Extended Boolean literal|depends on the property|<strong>TRUE</strong>, <strong>FALSE</strong>|
|panelCaptionAbove|<p>Indicates that the captions of property or action components should be drawn above the value on the panel</p><br/><p>removed in 5.0, use panelCaptionVertical instead</p>|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|panelCaptionVertical|Indicates that the captions of property or action components should be drawn above the value on the panel|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|<p>panelCaptionAfter</p>|Indicates that the value should be drawn on the panel prior to thee property caption|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|regexp|The regular expression that the property value must match during input|String literal|<strong>NULL</strong>|'^((8\|\\+7)[\\- ]?)?(\\(?\\d\{3\}\\)?[\\- ]?)?[\\d\\- ]\{7,10\}$'|
|regexpMessage|The message to be shown to the user if they enter a value that does not match the regular expression|String literal|default message|'Incorrect phone number format'|
|<p>toolTip</p>|The tip to be shown when the cursor hovers over the caption of a property or action|String literal|Default toolTip|'Tip'|
|pattern|Property value formatting template. The syntax of template definition is similar to the [DecimalFormat](https://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormat.html) or [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html) syntax, depending on the value type|String literal|<strong>NULL</strong>|<pre><code>&#39;#,##0.00&#39;</code></pre>|

### *Toolbar properties*

|Property name|Description|Value type|Default value|Examples|
|---|---|---|---|---|
|showCalculateSum|Show the column sum calculation button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|showCountQuantity|Show the row quantity calculation button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|showGroupChange|Show the group adjustment button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|showGroup|Show the grouping report button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|showPrintGroup|Show the table printing button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|showPrintGroupXls|Show the XLS export button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|showSettings|Show the table setting button|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|

### *Other properties*

|Property name|Applies to|Description|Value type|Default value|Examples|
|---|---|---|---|---|---|
|tabVertical|table|Specifying that focus will be moved from top to bottom (not from left to right)|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|quickSearch|table|Specifying that the table will support quick element search|Extended Boolean literal|<strong>FALSE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|
|visible|custom filter, class tree|Specifying the visibility of the component for setting custom filters (class tree)|Extended Boolean literal|<strong>TRUE</strong>|<strong>TRUE</strong>, <strong>FALSE</strong>|

### *Selector parameters*

*componentName*

Name of a design component. [Simple ID](IDs.md#id-broken).

*formPropertyName *

[Property/action name on the form](Properties_and_actions_block.md#name-broken)*.*

*filterGroupName*

The name of [a filter group](Filters_and_sortings_block.md#filterName-broken). [Simple ID](IDs.md#id-broken).

*propertyGroupSelector*

The name of a [property group](Groups_of_properties_and_actions.md). [Simple ID](IDs.md#id-broken).

*groupObjectSelector*

The name of an [object group on the form](Object_blocks.md#groupName-broken). [Simple ID](IDs.md#id-broken).

*treeSelector*

The name of [an object tree on the form](Object_blocks.md#treeName-broken). [Simple ID](IDs.md#id-broken).

*noGroupObjectTreeContainerType*

Type of form container:**  
**

-   **BOX** – a common form container
-   **PANEL** – contains components of properties that are displayed in PANEL view and display group of which is undefined.
-   **TOOLBARBOX** – a common toolbar container with property components that are displayed in the panel, marked for placement on the **TOOLBAR**,** **and for which no object group is defined.
-   **TOOLBARLEFT **- the left part of the toolbar
-   **TOOLBARRIGHT** - the right part of the toolbar
-   **TOOLBAR** contains components of properties that are displayed in TOOLBAR view and display group of which is undefined.

*groupObjectTreeContainerType*

The type of an object group / tree container.

-   All types of containers of the *noGroupObjectTreeContainerType* form (identical semantics)
-   **GRIDBOX **- a table container
-   **GRID **- a table component
-   **TOOLBARSYSTEM** - a system toolbar (number of records, group adjustment, etc.).
-   **FILTERGROUPS **- contains filter group components
-   **USERFILTER **- a component that displays custom filters

 **Examples**


```lsf
DESIGN order { // customizing the design of the form, starting with the default design
    // marking that all changes to the hierarchy will occur for the topmost container
    NEW orderPane FIRST { // creating a new container as the very first one before the system buttons, in which we put two containers - header and specifications
        fill = 1; // specifying that the container should occupy all the space available to it
        type = SPLITV; // specifying that the container will be a vertical splitter
        MOVE BOX(o) { // moving everything related to the object o to the new container
            PANEL(o) { // configuring how properties are displayed in the object o panel
                type = CONTAINERV; // making all descendants go from top to bottom
                NEW headerRow1 { // creating a container - the first row
                    type = CONTAINERH;
                    MOVE PROPERTY(date(o)) { // moving the order date property
                        caption = 'Date of the edited order'; // "override" the property caption in the form design (instead of the standard one)
                        toolTip = 'Input here the date the order was made'; //setting a hint for the order date property
                        background = #00FFFF; // making the background red
                    }
                    MOVE PROPERTY(time(o)) { // moving the order time property
                        foreground = #FF00FF; // making the color green
                    }
                    MOVE PROPERTY(number(o)) { // moving the order number property
                        charWidth = 5; // setting that the user should preferably be shown 5 characters
                    }
                    MOVE PROPERTY(series(o)); // moving the order series property
                }
                NEW headerRow2 {
                    type = CONTAINERV; // descendants - from top to bottom
                }
                MOVE PROPERTY(note(o));
            }

            size = (400, 300); //specifying that the container o.box should have a base size of 400x300 pixels
        }
        NEW detailPane { // creating a container that will store various specifications for the order
            type = TABBED; // marking that this container should be a tab panel, where its descendats are tabs
            MOVE BOX(d) { // adding a container with order lines as one of the tabs in the top panel
                caption = 'Lines'; // setting the caption of the tab panel
                PROPERTY(index(d)) { focusable = FALSE; } // making the row number column never have focus
                GRID(d) {
                    defaultComponent = TRUE; // making sure that by default the focus when opening the form is set to the row table
                }
            }
            MOVE BOX(s) { // adding a container with sku totals as one of the detailPane tabs
                caption = 'Selection';
            }
        }
    }
}

// splitting the form definition into two instructions (the second instruction can be transferred to another module)
DESIGN order {
    REMOVE TOOLBARLEFT; // removing from the hierarchy the container with the print and export buttons to xls, thereby making them invisible
}
```

The output is the following form:

![](attachments/4718609/43648025.png)
