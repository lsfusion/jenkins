---
title: 'Инструкция =>'
---

Инструкция **=>** - создание [следствия](Simple_constraints.md).

### Синтаксис

    leftPropertyId(param1, ..., paramN) => eventClause rightExpr [RESOLVE resolveType];

### Описание

Инструкция **=> **создает следствие. Этот оператор** **может объявлять свои локальные параметры при задании свойства посылки следствия. Затем эти параметры могут быть использованы в выражении следствия.

При создании следствия будет создано [ограничение](Constraints.md), которое во многом будет эквивалентно следующей инструкции

    CONSTRAINT eventClause leftPropertyId(param1, ..., paramN) AND NOT rightExpr MESSAGE 'Нарушено следствие';

но при этом позволяет автоматически разрешать ситуации нарушения этого ограничения. Так тип разрешения **RESOLVE LEFT** будет эквивалентен созданию [простого события](Simple_event.md):

    WHEN eventClause SET(leftPropertyId(param1, ..., paramN)) DO 
        SETACTION(rightExpr);

А **RESOLVE RIGHT** соответственно:

    WHEN eventClause DROPPED(rightExpr) DO
        DROPACTION(leftPropertyId(param1, ..., paramN));

### Параметры

*leftPropertyId*

[Идентификатор свойства](IDs.md#propertyid-broken), задающего посылку следствия.

*param1, ..., paramN*

Список [параметров](IDs.md#paramid-broken) свойства, задающего посылку следствия. Количество этих параметров должно совпадать с количеством параметров свойства.

*rightExpr*

[Выражение](Expression.md), значение которого определяет следствие.

*resolveType*

Тип [автоматического разрешения](Simple_event.md) при нарушении следствия. Задается одним из следующих вариантов:

-   **LEFT** - если посылка (левая часть инструкции) изменяется на не **NULL**, то следствие изменяется на не **NULL**.** **
-   **RIGHT** -  если следствие (правая часть инструкции) изменяется на **NULL**, то посылка изменяется на **NULL**.
-   **LEFT RIGHT** - аналогично **LEFT** и **RIGHT** вместе. 

*eventClause*

[Блок описания события](Event_description_block.md). Описывает [событие](Events.md), при наступлении которого будет проверяться создаваемое следствие и выполняться операции автоматического разрешения.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=means"/>

**  
**
