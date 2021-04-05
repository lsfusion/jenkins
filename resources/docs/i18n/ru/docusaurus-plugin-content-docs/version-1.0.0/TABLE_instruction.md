---
title: 'Инструкция TABLE'
---

Инструкция **TABLE -** создание новой [таблицы](Tables.md).

### Синтаксис

    TABLE name(className1, ..., classNameN) [FULL | NODEFAULT];

### Описание

Инструкция **TABLE **объявляет новую таблицу и добавляет ее в текущий [модуль](Modules.md). 

 

### Параметры

*name*

Имя таблицы. [Простой идентификатор](IDs.md#id-broken). Имя должно быть уникально в пределах текущего [пространства имен](Naming.md#namespace).

*className1, ..., classNameN*

Список имен классов. Каждое имя является [идентификатором класса](IDs.md#classname-broken). Определяет классы для ключевых полей создаваемой таблицы. Не может быть пустым.

*FULL*

Ключевое слово, при указании которого таблица будет помечена как [полная](Tables.md#full) (то есть в ней будут находиться все объекты, принадлежащие классам ключевых полей таблицы).  

*NODEFAULT*

Ключевое слово, при указании которого таблица не будет участвовать в автоматическом [определении таблиц](Tables.md#property-broken) свойств.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=table"/>

  
