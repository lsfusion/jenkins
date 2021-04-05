---
title: 'Инструкция GROUP'
---

Инструкция **GROUP - **создание новой [группы свойств и действий](Groups_of_properties_and_actions.md).

### Синтаксис

    GROUP name [caption] [EXTID extID] [: parentName];

### Описание

Инструкция **GROUP **объявляет новую группу свойств и действий и добавляет ее в текущий [модуль](Modules.md).  

### Параметры

*name *

Имя группы. [Простой идентификатор](IDs.md). Имя должно быть уникально в пределах текущего [пространства имен](Naming.md#namespace).

*caption*

Заголовок группы. [Строковый литерал](Literals.md#strliteral-broken). Если заголовок не задан, то заголовком группы будет являться ее имя.  

*EXTID extID*

Указание имени, которое будет использоваться для [экспорта / импорта](Structured_view.md#extid) этой группы свойств. Используется только в [структурированном](Structured_view.md) представлении.

*extId*

Строковый литерал.

*parentName*

Имя родительской группы. [Составной идентификатор](IDs.md). Если имя родительской группы не задано, то родительской группой становится группа **System.private**.  

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=group"/>

**  
**
