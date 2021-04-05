---
title: 'Оператор ACTIVATE'
---

Оператор **ACTIVATE **- создание [действия](Actions.md), [активирующего](Activation_ACTIVATE_.md) указанную [форму](Forms.md), закладку, свойство или действие на форме

### Синтаксис 

    ACTIVATE FORM formName
    ACTIVATE TAB formName.componentSelector
    ACTIVATE PROPERTY formPropertyId

### Описание

Оператор **ACTIVATE** создает действие, которое активизирует форму, закладку, свойство или действие на форме.

### Параметры

*formName*

Имя формы. [Составной идентификатор](IDs.md#cid-broken).

*componentSelector*

[Селектор](DESIGN_instruction.md#selector-broken) компонента дизайна. Компонент должен быть закладкой на панели вкладок.

*formPropertyId*

Глобальный [идентификатор свойства или действия на форме](IDs.md#formpropertyid-broken), на которое должен перейти фокус.

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=activate"/>

  
