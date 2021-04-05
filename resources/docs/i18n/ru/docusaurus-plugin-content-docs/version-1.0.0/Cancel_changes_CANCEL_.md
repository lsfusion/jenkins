---
title: 'Отмена изменений (CANCEL)'
---

Оператор *отмены изменений* полностью очищает текущую [сессию изменений](Change_sessions.md).

Как и для других операторов управления сессиями, для оператора отмены изменений можно явно указать [вложенные локальные свойства](Session_management.md#nested).

:::caution
Этот оператор работает по другому если выполняется внутри обработки [событий](Events.md#change) : в этом случае он отменяет [применение изменений](Apply_changes_APPLY_.md), которое привело к выполнению этой обработки
:::

### Язык

Для объявления действия, реализующего отмену изменений, используется [оператор **CANCEL**](CANCEL_operator.md).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=cancel"/>
