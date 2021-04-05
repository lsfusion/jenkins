---
title: 'Оператор EXEC'
---

Оператор **EXEC** - создание [действия](Actions.md), [выполняющего](Call_EXEC_.md) другое действие.

### Синтаксис

    [EXEC] actionId(expression1, ..., expressionN)

### Описание

Оператор **EXEC** создает действие, которое выполняет другое действие, передавая ему значения [выражений](Expression.md) в качестве параметров.

### Параметры

*actionId*

[Идентификатор действия](IDs.md#propertyid-broken). 

*expression1, ..., expressionN*

Список выражений, значения которых будут передаваться выполняемому действию в качестве аргументов. Количество выражений должно соответствовать количеству параметров выполняемого действия.

*operator*

Оператор, создающий выполняемое действие.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=exec"/>

  
