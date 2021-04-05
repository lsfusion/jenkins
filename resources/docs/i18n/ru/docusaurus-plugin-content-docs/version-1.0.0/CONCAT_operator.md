---
title: 'Оператор CONCAT'
---

Оператор **CONCAT - **создание [свойства](Properties.md), реализующего [объединение](String_operators_+_CONCAT_SUBSTRING_.md) строк.

### Синтаксис

    CONCAT separatorString, concatExpr1, ..., concatExprN

### Описание

Оператор **CONCAT** создает свойство, которое выполняет соединение значений через разделитель separatorString. При этом пустые значения пропускаются и разделитель вставляется только между непустыми значениями.

### Параметры

*separatorString*

 [Строковый литерал](Literals.md#strliteral-broken), который будет использован как разделитель.

*concatExpr1, ..., concatExprN*

[Выражения](Expression.md), значения которых будут соединены.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=concat"/>

**  
**

 
