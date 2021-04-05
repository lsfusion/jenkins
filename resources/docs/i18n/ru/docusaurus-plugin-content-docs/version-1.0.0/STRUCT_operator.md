---
title: 'Оператор STRUCT'
---

Оператор **STRUCT** - создание [свойства](Properties.md), которое создает [структуру](Structure_operations_STRUCT_.md).

### Синтаксис

    STRUCT(expr1, ..., exprN)   

### Описание

Оператор **STRUCT** создает свойство, значением которого будет структура, созданная из переданных объектов. 

### Параметры

*expr1, ..., exprN*

Список [выражений](Expression.md), значения которых будут являться элементами структуры. Список не может быть пустым.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=struct"/>

**  
**
