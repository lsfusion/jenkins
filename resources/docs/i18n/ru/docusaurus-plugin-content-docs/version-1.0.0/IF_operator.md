---
title: 'Оператор IF'
---

Оператор **IF** - создание [свойства](Properties.md), реализующего [выбор](Selection_CASE_IF_MULTI_OVERRIDE_EXCLUSIVE_.md) значения по условию (одиночная форма). 

### Синтаксис

    result IF condition 

### Описание

Оператор **IF **создает свойство, которое возвращает заданное значение при выполнении некоторого условия. Если условие не выполняется, свойство возвращает **NULL**.

### Параметры

*result*

[Выражение](Expression.md), значение которого определяет результат.

*condition*

Выражение, значение которого определяет условие.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=if"/>

**  
**
