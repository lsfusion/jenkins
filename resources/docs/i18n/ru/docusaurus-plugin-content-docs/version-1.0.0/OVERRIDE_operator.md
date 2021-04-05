---
title: 'Оператор OVERRIDE'
---

Оператор **OVERRIDE **- создание [свойства](Properties.md), реализующего [выбор](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md#exclusive) одного из значений (полиморфная форма).

### Синтаксис

    OVERRIDE expr1, ..., exprN

### Описание

Оператор **OVERRIDE **создает свойство, значением которого будет значение одного из указанных в операторе свойств. Выбор осуществляется среди свойств значение которых не равны **NULL**. Если несколько свойств не равны **NULL**, то выбирается значение первого из этих свойств.

### Параметры

*expr1, ..., exprN*

Список [выражений](Expression.md), значения которых будут определять значение свойства.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=override"/>

**  
**
