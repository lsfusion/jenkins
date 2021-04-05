---
title: 'Оператор NEWEXECUTOR'
---

Оператор **NEWEXECUTOR **- создание [действия](Actions.md), позволяющего выполнение других действий в [новом пуле потоков](New_threads_NEWTHREAD_NEWEXECUTOR_.md).

### Синтаксис

    NEWEXECUTOR action THREADS threadExpr

### Описание

Оператор **NEWEXECUTOR **создает действие, которое создает новый пул потоков и выполняет заданное действие таким образом, что любое действие внутри этого действия, созданное с помощью [оператора **NEWTHREAD**](NEWTHREAD_operator.md), будет выполняться в одном из потоков созданного пула. 

### Параметры

*action *

[Контекстно-зависимый оператор-действие](Action_operator.md#contextdependent), описывающий действие которое будет выполнено.

*threadExpr*

[Выражение](Expression.md), значение которого определяет количество потоков в пуле. Должно возвращать значение класса **INTEGER**. 

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=newexecutor"/>

  
