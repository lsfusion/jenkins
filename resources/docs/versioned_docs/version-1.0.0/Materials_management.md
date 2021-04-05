---
title: 'Materials management'
---

## Description of the "Materials management" task

The information system being created using the **lsFusion**  platform must support very basic supply chain execution capabilities.

For simplicity, let's define one type of document in our system that increases the stock balance — a receipt from the supplier; and one type of document that does the opposite — a shipment for a wholesale to a customer.

## Defining domain logic

The information system will consist of a set of [modules](Modules.md), each implementing a logically isolated piece of functionality. Each module can use the functionality of other modules, which involves special syntax constructions for defining module dependencies.

Based on our task, let's define the list of modules to be implemented: stock module, item module, legal entity module, receipt module, shipment module, current balance module. We will separately define the main module that will be executed and will basically be a compound solution. The composition of modules can be different and is determined by the developer depending on the need to re-use the functionality elsewhere.

### Defining a stock

Let's create a module where we will define a stock instance and its attributes.

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Stock&original=1&block=module"/>

Let's define the concept of a stock and its attributes: name, address.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Stock&original=1&block=class"/>

### Defining an item

Let's create a module in which we'll define the concept of an item and its attributes.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Item&original=1&block=module"/>

Let's define the concept of an item and its attributes: name, barcode.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Item&original=1&block=class"/>

Let's set the wholesale price for an item.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Item&original=1&block=price"/>

### Defining a legal entity

Let's create a module where we will define a legal entity instance and its attributes. In the system, legal entities will act as suppliers and customers.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/LegalEntity&original=1&block=module"/>

Let's define the concept of a legal entity and its attributes: name, legal address, Tax ID.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/LegalEntity&original=1&block=class"/>

We define the uniqueness of the Tax ID for the legal entity.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/LegalEntity&original=1&block=inn"/>

The legalEntityINN property binds an organization and a Tax ID one-to-one and allows to find a legal entity by a Tax ID. The expression of the property can be interpreted as follows: when grouping legal entities by Tax ID (innLegalEntity property), each group must contain a non-repeating legal entity.

### Defining a receipt

Let's create a module where we'll define all instances and attributes required for defining the logic of a supplier receipt.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=module"/>

Let's define the use of functionality from other modules in the Receipt module.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=require"/>

We define the concepts that determine the logic of a supplier receipt. Let's work on the premise that all documents (both receipts and shipments) in the system consist of a header and an item specification. Accordingly, let's define the concepts of a receipt header and receipt line.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=class"/>

  

Each receipt line contains a link to the document header, so in the end, the header and the subset of lines with links to this document together define the receipt from the user perspective. The  NONULL parameter indicates that the link must be defined. The  DELETE  parameter specifies that if the main Receipt object is deleted, all ReceiptDetail lines linking to it will also be deleted. By default, when an object is deleted, all links to it are nullified. This way, without the DELETE  parameter, the system will show an error message about an undefined link.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=documentref"/>

  

Let's define the line number in a receipt.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=index"/>

The use of the name of an object class in expressions is similar to using its identification number (id) created by the system for all objects by an automatic counter. In this case, the use of the ORDER receiptDetail construct helps sort the lines of the receipt by the order of ascension of their id, i.e. basically in the order of their creation.

Here, the PARTITION instruction uses the BY block that groups objects by a certain attribute. The calculation of the expression cumulative total is performed in each group. In this case, the line number is determined only within this line's document (receipt(d) property).

We define a set of key attributes of a receipt header: number, date, supplier and its name, the stock where the product is received and its name. The name of the supplier and the stock will be needed in the future for displaying them on the form.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=headerattributes"/>

We define a set of key attributes of a receipt line: item and its name, quantity, supplier price, supplier amount (price multiplied by quantity).

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=detailattributes"/>

### Defining a shipment

Let's create a module where we will define all instances and attributes required for a wholesale shipment.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=module"/>

 We define the use of functionality from other modules in the Shipment module.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=require"/>

Similarly to the receipt, we define the shipment header and lines, as well as a link in the line to the header and its number.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=class"/>

  

We define a set of attributes for the shipment: number, date, customer and its name, the stock from which the item is shipped, and its name.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=headerattributes"/>

  

We define a set of key attributes of a shipment: item and its name, quantity, sale price, sale amount (price multiplied by quantity).

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=detailattributes"/>

  

We implement the auto filling of the item sale price in the shipment with the value of the wholesale price defined by the user for item (salePrice property). Auto filling for the shipment line should work when the item is changed (WHEN CHANGED instruction).

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=priceset"/>

  

### Defining current item balance

The current item balance is defined as a difference between all item receipts and all its shipments.

Let's create a separate module.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockItem&original=1&block=module"/>

  

 We define the use of functionality from other modules in the StockItem module.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockItem&original=1&block=require"/>

  

Let's define the calculated property of the current item balance at the stock in quantitative terms.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockItem&original=1&block=properties"/>

  

Let's prohibit the negative item balance. The prohibition will work for any user action resulting in a negative balance. In this case, the user will see a specified message on the screen.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockItem&original=1&block=constraint"/>

  

## Defining view logic

In order to be able to work with the created solution, let's add directory forms and a current balances form, and also a set of paired forms for working with documents: a form for listing receipts and a form for editing them, a form for listing shipments and a form for editing them.

First, let's create directory forms.

In the Stock module, we add a form that provides the user with the functionality of creating and deleting stocks, as well as changing their attributes.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Stock&original=1&block=form"/>

In a similar manner, we'll create an item form in the Item module, and a legal entity form in the LegalEntity module.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Item&original=1&block=class"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/LegalEntity&original=1&block=class"/>

Let's create edit forms for a receipt and a shipment. These forms will be used for creating new documents or editing existing ones. The layout of the forms will be similar: two vertical blocks, the top one containing a panel with the header attributes of the document being created/edited, and the lower one containing the document lines in a grid view and their attributes.

In the Receipt module, we should create a receipt edit form. For the form we are building, we specify that it will be used as a default form for creating/editing receipts (the EDIT instruction).

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=formedit"/>

Line filtering for the current receipt is performed with the help of the FILTERS receipt(d) == r expression. The FILTERS construct displays an object of a corresponding class on the form if the filter expression returns a value different from NULL. In this case, the receipt line will be displayed on the form if the header of the document to which the line is linked ("receipt" property) equals to the current object of the top block. In other words, only the lines of the created/edited document will be displayed.

In addition, if a filter is specified for objects of this class on the form, then when the user presses the NEW button, the property of the newly added object will be automatically filled in a way that will make this object meet the filter conditions. In this case, when a new receipt line is created, the "receipt" property of this line will be automatically filled with a link to the current header of the receipt.

Let's create an edit form for the shipment in the Shipment module. For the form we are creating, we specify that it will be used as the default form for creating/editing shipments (EDIT instruction).

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=formedit"/>

Visually, supplier receipt and shipment forms will look almost identical and consist of two vertical blocks: one with a table for document headers and one with a table of document lines. Document lines should support visual filtering by documents and their subsets displayed on the form will change when navigating in the top block.

Let's create a receipt form. On this form, we will display all the properties defined above for document headers and their lines. Additionally, we will place automatically defined buttons for creating/editing a receipt using the edit form created above. All properties of document headers and their lines, except the buttons for creating/editing a receipt, should be read-only for editing directly on the form (READONLY operator).

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt&original=1&block=formlist"/>

Let's create the shipment form in a similar manner.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment&original=1&block=formlist"/>

Next, in the StockItem module, let's create a form for displaying current balances. A form should be a table whose lines contain information about the item (its name and barcode), the name of the stock, and the current balance for this item at this stock. The count of lines on the form will be equal to the count of items entered into the system multiplied by the count of entered stocks. To display only relevant data (i.e. only those items and stocks, for whose intersection the current balance is not NULL), let's add a filter to the form.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockItem&original=1&block=form"/>

The OBJECTS si = (s = Stock, i = Item) construct adds an object group with the name si, which is a Cartesian product of Stock and Item class objects.

Finally, let's declare the head module and specify what functionality from other modules will be used in it.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockAccounting&original=1&block=module"/>

In the StockAccounting module, we compose the system menu. Directories should be added to the predefined **masterData** folder of the navigator that we show immediately after the directories. We place the current balance form to the main menu (horizontal window **root**). Links to directory and document forms will be shown on the vertical **toolbar** when the user selects a corresponding **root** folder.

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockAccounting&original=1&block=navigator"/>

The process of creating an information system is completed.

## The complete source code (on [Github](https://github.com/lsfusion/samples/tree/master/mm))

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Stock"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Item"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/LegalEntity"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Receipt"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/Shipment"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockItem"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=mm/StockAccounting"/>
