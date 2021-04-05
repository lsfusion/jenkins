---
title: 'Оператор преобразования типа'
---

Оператор преобразования типа - создание [свойства](Properties.md), реализующего [преобразование типа](Type_conversion.md).

### Синтаксис

    typeName(expression) 

### Описание

Оператор создаёт свойство, которое преобразует значение некоторого выражения в значение указанного [встроенного класса](Built-in_classes.md). Если преобразование типа невозможно, значением свойства будет **NULL**.

### Параметры

*typeName*

Имя* *[встроенного класса](Built-in_classes.md), в который будут преобразовываться значения.

*expression*

[Выражение](Expression.md), значение которого будет преобразовано к значению указанного встроенного класса.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=explicitcast"/>

**  
**
