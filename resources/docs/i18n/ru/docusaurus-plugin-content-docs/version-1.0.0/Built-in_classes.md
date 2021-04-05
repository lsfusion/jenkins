---
title: 'Встроенные классы'
---

*Встроенные классы* - это [классы](Classes.md), экземплярами которых являются объекты примитивных типов данных, такие как целые числа, строки, и т.п. 

|Имя класса|Описание|Литералы lsFusion|
|---|---|---|
|<strong>INTEGER</strong>|Четырехбайтное целое число|5, 23, 1000000000|
|<strong>LONG</strong>|Восьмибайтное целое число|5l, 23L, 10000000000000L|
|<strong>DOUBLE</strong>|Восьмибайтное число с плавающей точкой|5.0d, 2.35D|
|<strong>NUMERIC[ , ]</strong>|Число с фиксированной разрядностью и точностью|5.0, 2.35|
|<strong>BOOLEAN</strong>|Логический тип данных|TRUE, NULL|
|<strong>DATE</strong>|Дата|13_07_1982|
|<strong>DATETIME</strong>|Дата и время|13_07_1982_18:00|
|<strong>TIME</strong>|Время|18:00|
|<strong>YEAR</strong>|Год|<br /><br/>|
|<strong>STRING, STRING[ ]</strong>|Строковый тип данных, при необходимости с максимальной длиной, зависимый от регистра|<br /><br/>|
|<strong>ISTRING, ISTRING[ ]</strong>|Строковый тип данных, при необходимости с максимальной длиной, независимый от регистра|<br /><br/>|
|<strong>BPSTRING[]</strong>|Строковый тип данных с максимальной длиной, зависимый от регистра, с пробелами в конце|'text', 'text with\nbreak'|
|<strong>BPISTRING[]</strong>|Строковый тип данных с максимальной длиной, независимый от регистра, с пробелами в конце|<br /><br/>|
|<strong>TEXT</strong>|Строковый тип данных произвольной длины, зависимый от регистра|<br /><br/>|
|<strong>RICHTEXT</strong>|Строковый тип данных произвольной длины с форматированием|<br /><br/>|
|<strong>COLOR</strong>|Цвет|#00ссff, #AA55CC, RGB(0, 255, 0)|
|<strong>FILE</strong>|Файл динамического типа (содержимое файла вместе с его расширением)|<br /><br/>|
|<p><strong>RAWFILE, WORDFILE, IMAGEFILE, PDFFILE,</strong> <strong>EXCELFILE, CSVFILE, HTMLFILE, JSONFILE, XMLFILE, TABLEFILE</strong></p>|Файлы конкретного типа (<strong>RAWFILE</strong> - файл без расширения / с неизвестным расширением)|<br /><br/>|
|<strong>LINK</strong>|Символьный идентификатор-ссылка на файл (URI)|<br /><br/>|
|<strong>RAWLINK, WORDLINK, IMAGELINK, PDFLINK, EXCELLINK<strong>, CSVLINK, HTMLLINK, JSONLINK, XMLLINK, TABLELINK</strong></strong>|Символьный идентификатор-ссылка на файл конкретного типа (<strong>RAWLINK</strong> - ссылка на файл без расширения / с неизвестным расширением)|<br /><br/>|

### Наследование {#inheritance}

Среди всех встроенных классов можно выделить четыре *семейства* классов (будем считать, что каждый из остальных классов образует свое семейство классов)

|Имя класса|Описание|
|---|---|
|<strong>Числа</strong>|<strong>INTEGER, <strong>LONG, <strong>DOUBLE, <strong>NUMERIC[ , ]</strong></strong></strong></strong>|
|<strong>Строки</strong>|<strong>STRING, STRING[ ], ISTRING, ISTRING[], <strong>BPSTRING[ ], BP<strong>ISTRING[ ], <strong>TEXT</strong></strong></strong></strong>|
|<strong>Файлы конкретного типа</strong>|<strong>RAWFILE,</strong> <strong>WORDFILE, IMAGEFILE, PDFFILE,</strong> <strong>EXCELFILE<strong>, CSVFILE, HTMLFILE, JSONFILE, XMLFILE, TABLEFILE</strong></strong>|
|<strong>Ссылки на файлы конкретного типа</strong>|<strong>RAWLINK, WORDLINK, IMAGELINK, PDFLINK, EXCELLINK<strong><strong>, CSVLINK, HTMLLINK, JSONLINK, XMLLINK, TABLELINK</strong></strong></strong>|

Встроенные классы наследуют друг друга только в рамках одного семейства и не могут наследовать / наследоваться от пользовательских классов. Наследование в рамках одного семейства строится по принципу: более узкий класс наследуется от более широкого.

### **Общий предок** {#commonparentclass}

В соответствии с описанным механизмом наследования, общий предок двух встроенных классов (например для операции [выбора](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md)) определяется следующим образом:

*Строки*:

    result = STRING[blankPadded = s1.blankPadded OR s2.blankPadded, caseInsensitive = s1.caseInsensitive OR s2.caseInsensitive, length = MAX(s1.length, s2.length)]

где *blankPadded*, *caseInsensitive* и *length*, в свою очередь, определяются как:

|Имя класса|blankPadded|caseInsensitive|length|
|---|---|---|---|
|<strong>STRING[n]</strong>|false|false|n|
|<strong>ISTRING[n]</strong>|false|true|n|
|<strong>BPSTRING[n]</strong>|true|false|n|
|<strong>BPISTRING[n]</strong>|true|true|n|
|<strong>TEXT</strong>|false|false|INFINITE|

*Числа*:

    IF p1.integerPart >= p2.integerPart AND p1.precision >= p2.precision
        result = p1 
    ELSE IF p1.integerPart >= p2.integerPart AND p1.precision >= p2.precision
        result = p2 
    ELSE IF p1.integerPart > p2.integerPart  
        result = NUMERIC[p1.integerPart+p2.precision, p2.precision]
    ELSE  
        result = NUMERIC[p2.integerPart+p1.precision, p1.precision]

где integerPart и precision, в свою очередь, определяются как:

|Имя класса|integerPart|precision|
|---|---|---|
|<strong>INTEGER</strong>|10|0|
|<strong>DOUBLE</strong>|99999|99999|
|<strong>LONG</strong>|20|0|
|<strong>NUMERIC[l,p]</strong>|length-precision|precision|

*Файлы конкретного типа*:

    IF p1 = p2
        result = p1
    ELSE
        result = RAWFILE

*Ссылки на файлы конкретного типа*:

    IF p1 = p2
        result = p1
    ELSE
        result = RAWLINK

Отметим, что иногда в программировании определение общего родительского класса принято ассоциировать с *неявным приведением типов*.

### Значение по умолчанию {#defaultvalue}

В некоторых случаях для встроенного класса необходимо использовать некоторое значение, которое будет заведомо отличаться от **NULL** (например, в условии импорта при [импорте данных](Data_import_IMPORT_.md)). Это значение будем называть *значением по умолчанию*, и определяется оно следующим образом:

|Имя класса|Значение по умолчанию|
|---|---|
|<strong>Числовые классы</strong>|0|
|<strong>Строки</strong>|Пустая строка|
|<strong>DATE, TIME, DATETIME</strong>|Текущие дата, время, дата / время|
|<strong>BOOLEAN</strong>|<strong>TRUE</strong>|
|<strong>COLOR</strong>|Белый цвет|
|<strong>Файлы конкретного типа</strong>|Пустой файл|
|<strong>FILE</strong>|Пустой файл с пустым расширением|

### Расширения файлов конкретного типа {#extension}

При преобразовании файлов конкретного типа (**JSONFILE**, **XMLFILE**, ...) к файлу динамического типа (**FILE**), как явном, так и неявном (например при [импорте данных](Data_import_IMPORT_.md) без указании формата или при [взаимодействии с внешними системами](Access_to_an_external_system_EXTERNAL_.md)) расширение результирующего файла определяется следующим образом:

|Имя класса|Расширение|
|---|---|
|<strong>RAWFILE</strong>|Пустая строка|
|<strong>JSONFILE</strong>|json|
|<strong>XMLFILE</strong>|xml|
|<strong>CSVFILE</strong>|csv|
|<strong>WORDFILE</strong>|doc|
|<strong>EXCELFILE</strong>|xls|
|<strong>HTMLFILE</strong>|html|
|<strong>PDFFILE</strong>|pdf|
|<strong>IMAGEFILE</strong>|jpg|
|<strong>TABLEFILE</strong>|table|

### Порядок определения результирующего свойства при [обращении из внешней системы](Access_from_an_external_system.md#httpresult-broken)

|Имя класса|Имя свойства|
|---|---|
|<p><strong>FILE, RAWFILE, WORDFILE, IMAGEFILE, PDFFILE,</strong> <strong>EXCELFILE, CSVFILE, HTMLFILE, JSONFILE, XMLFILE, TABLEFILE</strong></p>|exportFile, exportRawFile, exportWordFile, exportImageFile, exportPdfFile, exportExcelFile, exportCsvFile, exportHtmlFile, exportJsonFile, exportXmlFile|
|<strong><strong>TEXT, STRING, BPSTRING</strong><br /><br/></strong>|exportText, exportString, exportBPString|
|<strong><strong>NUMERIC,</strong> LONG, INTEGER, DOUBLE</strong>|exportNumeric, exportLong, exportInteger, exportDouble|
|<strong><strong>DATETIME,</strong> DATE, TIME, YEAR</strong>|exportDateTime, exportDate, exportTime, exportYear|
|<strong><strong>LINK,</strong> RAWLINK, WORDLINK, IMAGELINK, PDFLINK, EXCELLINK<strong>, CSVLINK, HTMLLINK, JSONLINK, XMLLINK, TABLELINK</strong></strong>|exportFile, exportRawFile, exportWordFile, exportImageFile, exportPdfFile, exportExcelFile, exportCsvFile, exportHtmlFile, exportJsonFile, exportXmlFile|
|<strong>BOOLEAN, COLOR</strong>|exportBoolean, exportColor|
|[Пользовательские классы](Static_objects.md)|exportObject|
