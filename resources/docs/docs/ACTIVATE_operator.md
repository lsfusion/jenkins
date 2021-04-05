---
title: 'ACTIVATE operator'
---

The **ACTIVATE** operator creates an [action](Actions.md) that [activates](Activation_ACTIVATE_.md) a specified [form](Forms.md), tab, property, or action on a form

### Syntax 

    ACTIVATE FORM formName
    ACTIVATE TAB formName.componentSelector
    ACTIVATE PROPERTY formPropertyId

### Description

The **ACTIVATE** operator creates an action that activates a form, a tab, a property or an action on a form. 

### Parameters

*formName*

Form name. [Composite ID](IDs.md#cid-broken).

*componentSelector*

Design component [selector](DESIGN_instruction.md#selector-broken). The component must be a tab in the tab panel.

*formPropertyId*

The global [ID of a property or action on a form](IDs.md#formpropertyid-broken) which should get focus.

### Examples

```lsf
//Form with two tabs
FORM myForm 'My form'
    OBJECTS u = CustomUser
    PROPERTIES(u) name

    OBJECTS c = Chat
    PROPERTIES(c) name
;

DESIGN myForm {
    NEW tabbedPane FIRST {
        type = TABBED;
        NEW contacts {
            caption = 'Contacts';
            MOVE BOX(u);
        }
        NEW recent {
            caption = 'Recent';
            MOVE BOX(c);
        }
    }
}

testAction()  {
    ACTIVATE FORM myForm;
    ACTIVATE TAB myForm.recent;
}

CLASS ReceiptDetail;
barcode = DATA STRING[30] (ReceiptDetail);
quantity = DATA STRING[30] (ReceiptDetail);

FORM POS
    OBJECTS d = ReceiptDetail
    PROPERTIES(d) barcode, quantityGrid = quantity
;

createReceiptDetail 'Add sales line'(STRING[30] barcode)  {
    NEW d = ReceiptDetail {
        barcode(d) <- barcode;
        ACTIVATE PROPERTY POS.quantityGrid;
    }
}
```

  
