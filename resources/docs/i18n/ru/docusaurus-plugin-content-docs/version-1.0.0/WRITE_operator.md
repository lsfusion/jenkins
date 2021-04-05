---
title: 'Оператор WRITE'
---

Оператор **WRITE** - создание [действия](Actions.md), [записывающего файл](Write_file_WRITE_.md) из свойства во внешний ресурс. 

### Синтаксис

    WRITE [CLIENT [DIALOG]] fileExpr TO urlExpr [APPEND]

### Описание

Оператор **WRITE** создает действие, записывающее файл из свойства, во внешний ресурс по заданному URL'у.

Поддерживаются следующие типы URL:

    [file://]path_to_file
    [s]ftp://username:password[;charset]@host:port[/path_to_file][?passivemode=true|false]

Предполагается, что в URL расширение файла не задается (то есть точка (.) также считается частью имени файла). Это расширение определяется автоматически, в зависимости от класса записываемого файла, следующим образом:

|<p>Расширение</p>|<p>Класс</p>|
|---|---|
|читается из переданного объекта|<strong>FILE</strong>|
|json|<strong>JSONFILE</strong>|
|xml|<strong>XMLFILE</strong>|
|csv|<strong>CSVFILE</strong>|
|xls или xlsx в зависимости от содержимого файла|<strong>EXCELFILE</strong>|
|dbf|<strong>DBFFILE</strong>|
|table|<strong>TABLEFILE</strong>|
|html|<strong>HTMLFILE</strong>|
|doc или docx в зависимости от содержимого файла|<strong>WORDFILE</strong>|
|jpg|<strong>IMAGEFILE</strong>|
|pdf|<strong>PDFFILE</strong>|

На клиенте текущим директорием считается директорий Downloads внутри пользовательского директория.

### Параметры

*CLIENT*

Ключевое слово. Если указывается, то действие будет выполнено на клиенте. По умолчанию действие выполняется на сервере.

*DIALOG*

Ключевое слово. Если указывается, то перед записью файла, будет показан диалог, в котором пользователь может изменить заданный URL****.**** Можно использовать только при записи на диск (тип URL - file) .**** ****По умолчанию диалог не показывается. 

****ulrExpr***  
*

[Выражение](Expression.md), значением которого является URL.

### *fileExpr*

[Выражение](Expression.md), значением которого является файл, который который будет записан во внешний ресурс. 

**APPEND**

Ключевое слово. Если указывается, то происходит дозапись файла из fileExpr в файл по указанному urlExpr . Для расширения csv происходит дозапись в конец файла. Для расширений xls и xlsx происходит копирование всех листов из файла fileExpr в файл по указанному urlExpr. Для остальных расширений не поддерживается. По умолчанию происходит перезапись файла.

**  
**

### **Примеры**


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=write"/>

