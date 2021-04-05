---
title: 'Операторы IS, AS'
---

Операторы **IS**, **AS** - создание [свойства](Properties.md), реализующего [классификацию](Classification_IS_AS_.md).

### Синтаксис

    expression IS className
    expression AS className

### Описание

Оператор **IS** создает свойство, которое возвращает **TRUE**, если значение [выражения](Expression.md) принадлежит указанному классу.

Оператор **AS** создает свойство, которое возвращает значение выражения, если это значение принадлежит указанному классу.

### Параметры

*expression*

Выражение, значение которого проверяется на принадлежность классу.

*className*

Имя класса. [Идентификатор класса](IDs.md#classid-broken).

### Примеры 


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=isas"/>

**  
**
