---
title: 'In a structured view (EXPORT, IMPORT)'
---

This operator creates an action that [opens a form](Open_form.md) in the [structured](Structured_view.md) view.

### Format {#format}

In this operator, you can define the format that all form data will be converted to:  **XML**, **JSON**, **DBF, CSV, XLS**. The generated file(s) in this format is then written to the specified property.

Form export is a general case of the  [data export operator](Data_export_EXPORT_.md).

### Form import {#importForm}

Form import is an operation that is opposite to opening the form in a structured view. The import operator accepts files in a structured format, then parses them and saves the data to the properties of the set form in such a way that when this form is exported back into the imported format, it would recreate the original file.

Since the import operator is essentially an "input operator", the following constraints apply to the form being imported:

-   All form objects must belong to  numeric or[concrete](User_classes.md#abstract) [user](User_classes.md) classes. Object groups must consist of exactly one object (this constraint is caused by the fact that all the used formats are essentially lists — that is, mappings of numbers to values).

-   Properties and [filters](Form_structure.md#filters) on the form should be  [changeable](Property_change_CHANGE_.md) by a given value (that is, as a rule, be [primary](Data_properties_DATA_.md)). Before importing, any existing changes to the imported properties in the current session are canceled.

During import, filters change to the default values of value classes of these filters.

When importing data into objects of numeric classes, 0-based numbering is used. In case of [hierarchical](Structured_view.md#hierarchy) formats, numbering is "end-to-end" (that is, when the object group being imported is encountered for the second and subsequent times, object numbering in it starts from the position that the previous one stopped at).

When importing from XLS and CSV without headers (with the **NOHEADER** option), the platform automatically attempts to convert data to the necessary type. If it fails, a **NULL** value is written to the property. Importing from other formats requires correct types. For example, if a string is required during data import from JSON, and the JSON file contains a number (without quotes), the platform will generate an error.

If a property (object group) is not found during import, it is ignored (that is, its value remains equal to **NULL**).

Form import is a general case of the [data import](Data_import_IMPORT_.md) operator.

### Language

To open the form in the structured view, use the [**EXPORT** operator](EXPORT_operator.md). To import a form, use the [**IMPORT** operator](IMPORT_operator.md).

### Examples

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=export"/>

<CodeSample url="https://documentation.lsfusion.org/sample?file=ActionSample&block=importForm"/>
