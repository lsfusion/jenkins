---
title: 'Инструкция EXTEND CLASS'
---

Инструкция **EXTEND CLASS -** [расширение](Class_extension.md) существующего класса.

### Синтаксис

    EXTEND CLASS name 
    [{
        objectName1 [objectCaption1],
        ...
        objectNameM [objectCaptionM]
    }] 
    [: parent1, ..., parentN];

### Описание

Инструкция **EXTEND CLASS** расширяет существующий [пользовательский класс](Static_objects.md) дополнительными родительскими классами и новыми [статическими объектами](User_classes.md). Расширять можно в том числе и [абстрактные классы](Static_objects.md#abstract), добавляя им родительские классы.

### Параметры

*name *

Имя класса. [Составной идентификатор](IDs.md#cid-broken). 

*objectName1, ..., objectNameM*

Имена новых статических объектов указанного класса. Каждое имя задается [простым идентификатором](IDs.md#id-broken). Значения имен хранятся в системном свойстве **System.staticName**.

*objectCaption1, ..., objectCaptionM*

Заголовки новых статических объектов указанного класса. Каждый заголовок является [строковым литералом](IDs.md#strliteral-broken). Если заголовок не задан, то заголовком статического объекта будет являться его имя. Значения заголовков хранятся в системном свойстве **System.staticCaption**.

*parent1, ..., parentN*

Список имен новых родительских классов. Каждое имя задается составным идентификатором. 

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=extendclass"/>

  
