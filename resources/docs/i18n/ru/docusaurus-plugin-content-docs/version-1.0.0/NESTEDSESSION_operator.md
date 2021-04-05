---
title: 'Оператор NESTEDSESSION'
---

Оператор **NESTEDSESSION **- создание [действия](Actions.md), которое выполняет другое действие во [вложенной сессии](New_session_NEWSESSION_NESTEDSESSION_.md#nested).

### Синтаксис

    NESTEDSESSION action 

### Описание

Оператор **NESTEDSESSION **создает действие, которое выполняет другое действие во вложенной сессии. При этом все изменения, уже произошедшие в текущей сессии, попадают в создаваемую вложенную сессию. Также все изменения, которые будут произведены во вложенной сессии, попадут в текущую сессию при [применении изменений](Apply_changes_APPLY_.md) во вложенной сессии.

### Параметры

*action *

[Контекстно-зависимый оператор-действие](Action_operator.md#contextdependent), описывающий действие, которое должно быть выполнено во вложенной сессии.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=nestedsession"/>

  
