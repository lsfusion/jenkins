---
title: 'Оператор ACTIVE TAB'
---

Оператор **ACTIVE TAB **- создание [свойства](Properties.md), реализующего проверку [активности](Activity_ACTIVE_.md) закладки.

### Синтаксис 

    ACTIVE TAB formName.componentSelector

### Описание

Оператор **ACTIVE TAB** создает свойство, которое возвращает **TRUE**, если указанная закладка активна на [форме](Forms.md). 

### Параметры

*formName*

Имя формы. [Составной идентификатор](IDs.md#cid-broken).

**componentSelector*  
*

[Селектор](DESIGN_instruction.md) компонента дизайна. Компонент должен быть закладкой на панели вкладок.

### Примеры

  

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=activetab"/>
