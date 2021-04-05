---
title: 'Инструкция AFTER'
---

Инструкция **AFTER** - вызов [действия](Actions.md) после вызова другого действия. 

### Синтаксис

    AFTER action(param1, ..., paramN) DO aspectAction;

### Описание

Инструкция **AFTER** задает действие (будем называть его *аспектом*), которое будет вызываться после вызова указанного действия.

### Параметры

*action*

[Идентификатор действия](IDs.md#propertyid-broken), после которого будет вызываться аспект.

*param1, ..., paramN*

Список имен параметров действия. Каждое имя задается [простым идентификатором](IDs.md#id-broken). К этим параметрам можно обращаться при задании аспекта.

*aspectAction*

[Контекстно-зависимый оператор-действие](Action_operator.md#contextdependent), описывающий аспект.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=InstructionSample&block=after"/>

  
