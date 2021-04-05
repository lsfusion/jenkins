---
title: 'Built-in classes'
---

*Built-in classes* are [classes](Classes.md) whose instances are objects belonging to primitive data types such as integers, strings, etc. 

|Class name|Description|lsFusion literals|
|---|---|---|
|<strong>INTEGER</strong>|32-bit integer|5, 23, 1000000000|
|<strong>LONG</strong>|64-bit integer|5l, 23L, 10000000000000L|
|<strong>DOUBLE</strong>|64-bit floating point number|5.0d, 2.35D|
|<strong>NUMERIC[ , ]</strong>|Number with fixed width and precision|5.0, 2.35|
|<strong>BOOLEAN</strong>|The logical data type|TRUE, NULL|
|<strong>DATE</strong>|Date|13_07_1982|
|<strong>DATETIME</strong>|Date and time|13_07_1982_18:00|
|<strong>TIME</strong>|Time|18:00|
|<strong>YEAR</strong>|Year|<br /><br/>|
|<strong>STRING, STRING[ ]</strong>|String data type with optional maximum length, case-sensitive|<br /><br/>|
|<strong>ISTRING, ISTRING[ ]</strong>|String data type with optional maximum length, case-insensitive|<br /><br/>|
|<strong>BPSTRING[]</strong>|String data type with maximum length, case-sensitive, padded at the end with spaces|'text', 'text with\\nbreak'|
|<strong>BPISTRING[]</strong>|String data type with maximum length, case-insensitive, padded at the end with spaces|<br /><br/>|
|<strong>TEXT</strong>|String data type of arbitrary length, case-sensitive|<br /><br/>|
|<strong>RICHTEXT</strong>|String data type of arbitrary length with formatting|<br /><br/>|
|<strong>COLOR</strong>|Color|#00ccff, #AA55CC, RGB(0, 255, 0)|
|<strong>FILE</strong>|File of dynamic type (file content together with extension)|<br /><br/>|
|<p><strong>RAWFILE, WORDFILE, IMAGEFILE, PDFFILE,</strong> <strong>EXCELFILE, CSVFILE, HTMLFILE, JSONFILE, XMLFILE, TABLEFILE</strong></p>|Files of specific type (<strong>RAWFILE</strong>: file with no extension or with unknown extension)|<br /><br/>|
|<strong>LINK</strong>|Link to a file (URI)|<br /><br/>|
|<strong>RAWLINK, WORDLINK, IMAGELINK, PDFLINK, EXCELLINK<strong>, CSVLINK, HTMLLINK, JSONLINK, XMLLINK, TABLELINK</strong></strong>|Link to a file of a specific type (<strong>RAWLINK</strong>: link to a file with no extension or an unknown extension)|<br /><br/>|

### Inheritance {#inheritance}

The builtin classes can be divided into four class *families* (assuming that each of the remaining classes forms its own class family)

|Class name|Description|
|---|---|
|<strong>Numbers</strong>|<strong>INTEGER, <strong>LONG, <strong>DOUBLE, <strong>NUMERIC[ , ]</strong></strong></strong></strong>|
|<strong>Strings</strong>|<strong>STRING, STRING[ ], ISTRING, ISTRING[], <strong>BPSTRING[ ], BP<strong>ISTRING[ ], <strong>TEXT</strong></strong></strong></strong>|
|<strong>Files of a specific type</strong>|<strong>RAWFILE,</strong> <strong>WORDFILE, IMAGEFILE, PDFFILE,</strong> <strong>EXCELFILE<strong>, CSVFILE, HTMLFILE, JSONFILE, XMLFILE, TABLEFILE</strong></strong>|
|<strong>Links to files of a specific type</strong>|<strong>RAWLINK, WORDLINK, IMAGELINK, PDFLINK, EXCELLINK<strong><strong>, CSVLINK, HTMLLINK, JSONLINK, XMLLINK, TABLELINK</strong></strong></strong>|

The builtin classes inherit only from one another within a single family, and cannot inherit from or be inherited by user classes. Inheritance within each family works on the principle that the narrower class inherits from the broader one.

### **Common ancestor** {#commonparentclass}

According to this inheritance mechanism, the common ancestor of two builtin classes (e.g. for the [selection](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md) operation) is determined as follows:

*Strings*:

    result = STRING[blankPadded = s1.blankPadded OR s2.blankPadded, caseInsensitive = s1.caseInsensitive OR s2.caseInsensitive, length = MAX(s1.length, s2.length)]

where *blankPadded*, *caseInsensitive* and *length* are in turn determined as:

|Class name|blankPadded|caseInsensitive|length|
|---|---|---|---|
|<strong>STRING[n]</strong>|false|false|n|
|<strong>ISTRING[n]</strong>|false|true|n|
|<strong>BPSTRING[n]</strong>|true|false|n|
|<strong>BPISTRING[n]</strong>|true|true|n|
|<strong>TEXT</strong>|false|false|INFINITE|

*Numbers*:

    IF p1.integerPart >= p2.integerPart AND p1.precision >= p2.precision
        result = p1 
    ELSE IF p1.integerPart >= p2.integerPart AND p1.precision >= p2.precision
        result = p2 
    ELSE IF p1.integerPart > p2.integerPart  
        result = NUMERIC[p1.integerPart+p2.precision, p2.precision]
    ELSE  
        result = NUMERIC[p2.integerPart+p1.precision, p1.precision]

where integerPart and precision, in turn, are determined as:

|Class name|integerPart|precision|
|---|---|---|
|<strong>INTEGER</strong>|10|0|
|<strong>DOUBLE</strong>|99999|99999|
|<strong>LONG</strong>|20|0|
|<strong>NUMERIC[l,p]</strong>|length-precision|precision|

*Files of a specific type*:

    IF p1 = p2
        result = p1
    ELSE
        result = RAWFILE

*Links to files of a specific type*:

    IF p1 = p2
        result = p1
    ELSE
        result = RAWLINK

Note that sometimes in programming the definition of a common parent class is associated with *implicit typecasting*.

### Default value {#defaultvalue}

It is sometimes necessary to use some value for a built-in class which will differ from **NULL** (for example, in an import condition with [data import](Data_import_IMPORT_.md)). Let's call this value the *default value*. It is defined as follows:

|Class name|Default value|
|---|---|
|<strong>Numerical classes</strong>|0|
|<strong>Strings</strong>|The empty string|
|<strong>DATE, TIME, DATETIME</strong>|The current date / time / date and time|
|<strong>BOOLEAN</strong>|<strong>TRUE</strong>|
|<strong>COLOR</strong>|White|
|<strong>Files of a specific type</strong>|Empty file|
|<strong>FILE</strong>|Empty file with empty extension|

### Extensions of specific type files {#extension}

When files of a specific type (**JSONFILE**, **XMLFILE**, ...) are cast into a file of dynamic type (**FILE**), whether explicitly or implicitly (e.g. with [data import](Data_import_IMPORT_.md) without specifying a format or when [working with external systems](Access_to_an_external_system_EXTERNAL_.md)), the extension of the result file is determined as follows:

|Class name|Extension|
|---|---|
|<strong>RAWFILE</strong>|The empty string|
|<strong>JSONFILE</strong>|json|
|<strong>XMLFILE</strong>|xml|
|<strong>CSVFILE</strong>|csv|
|<strong>WORDFILE</strong>|doc|
|<strong>EXCELFILE</strong>|xls|
|<strong>HTMLFILE</strong>|html|
|<strong>PDFFILE</strong>|pdf|
|<strong>IMAGEFILE</strong>|jpg|
|<strong>TABLEFILE</strong>|table|

### The order of determining the result property when [accessing from an external system](Access_from_an_external_system.md#httpresult-broken)

|Class name|Property name|
|---|---|
|<p><strong>FILE, RAWFILE, WORDFILE, IMAGEFILE, PDFFILE,</strong> <strong>EXCELFILE, CSVFILE, HTMLFILE, JSONFILE, XMLFILE, TABLEFILE</strong></p>|exportFile, exportRawFile, exportWordFile, exportImageFile, exportPdfFile, exportExcelFile, exportCsvFile, exportHtmlFile, exportJsonFile, exportXmlFile|
|<strong><strong>TEXT, STRING, BPSTRING</strong><br /><br/></strong>|exportText, exportString, exportBPString|
|<strong><strong>NUMERIC,</strong> LONG, INTEGER, DOUBLE</strong>|exportNumeric, exportLong, exportInteger, exportDouble|
|<strong><strong>DATETIME,</strong> DATE, TIME, YEAR</strong>|exportDateTime, exportDate, exportTime, exportYear|
|<strong><strong>LINK,</strong> RAWLINK, WORDLINK, IMAGELINK, PDFLINK, EXCELLINK<strong>, CSVLINK, HTMLLINK, JSONLINK, XMLLINK, TABLELINK</strong></strong>|exportFile, exportRawFile, exportWordFile, exportImageFile, exportPdfFile, exportExcelFile, exportCsvFile, exportHtmlFile, exportJsonFile, exportXmlFile|
|<strong>BOOLEAN, COLOR</strong>|exportBoolean, exportColor|
|[User classes](User_classes.md)|exportObject|
