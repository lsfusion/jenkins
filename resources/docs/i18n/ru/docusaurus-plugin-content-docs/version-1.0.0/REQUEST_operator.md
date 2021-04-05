---
title: 'Оператор REQUEST'
---

Оператор **REQUEST** - создание [действия](Actions.md), осуществляющего [запрос значения](Value_request_REQUEST_.md).

### Синтаксис

    REQUEST requestAction 
    DO doAction [ELSE elseAction]

### Описание

Оператор **REQUEST** создает действие, которое позволяет отделить запрос значения от его обработки.

### Параметры

*requestAction*

[Контекстно-зависимый оператор-действие](Action_operator.md), выполняет запрос значения.

*doAction*

[Контекстно-зависимый оператор-действие](Action_operator.md), выполняется, если ввод был успешно завершен.

*elseAction*

[Контекстно-зависимый оператор-действие](Action_operator.md), выполняется, если ввод был [отменен](Value_input.md#result).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=request"/>

**  
**
