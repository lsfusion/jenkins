---
title: 'Инструкция CLASS'
---

Инструкция **CLASS - **создание нового [пользовательского класса](Static_objects.md).

### Синтаксис

    CLASS ABSTRACT name [caption] [: parent1, ..., parentN];
     
    CLASS name [caption] 
    [{
        objectName1 [objectCaption1],
        ...
        objectNameM [objectCaptionM]
    }] 
    [: parent1, ..., parentN];

### Описание

Инструкция **CLASS** объявляет новый класс и добавляет его в текущий [модуль](Modules.md). 

Инструкция бывает двух видов: **CLASS ABSTRACT** для объявления [абстрактного класса](Static_objects.md#abstract) и просто **CLASS** для объявления обычного класса. Во втором случае при объявлении класса можно объявить [статические объекты](User_classes.md) этого класса, имена и заголовки которых указываются в ограниченном фигурными скобками блоке.   

### Параметры

*name *

Имя класса. [Простой идентификатор](IDs.md#id-broken). Имя должно быть уникально в пределах текущего [пространства имен](Naming.md#namespace).

*caption*

Заголовок класса. [Строковый литерал](Literals.md#strliteral-broken). Если заголовок не задан, то заголовком класса будет являться его имя.  

*objectName1, ..., objectNameM*

Имена [статических](User_classes.md) объектов данного класса. Каждое имя задается простым идентификатором. Значения имен хранятся в системном свойстве **System.staticName**.

*objectCaption1, ..., objectCaptionM*

Заголовки статических объектов данного класса. Каждый заголовок является строковым литералом. Если заголовок не задан, то заголовком статического объекта будет являться его имя. Значения заголовков хранятся в системном свойстве **System.staticCaption**.

*parent1, ..., parentN*

Список имен родительских классов. Каждое имя задается [составным идентификатором](IDs.md#cid-broken). Если список родительских классов не задан, то класс наследуется от класса **System.Object**.  

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=class"/>

**  
**
