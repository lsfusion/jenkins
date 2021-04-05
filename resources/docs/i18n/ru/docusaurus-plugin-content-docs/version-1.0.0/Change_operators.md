---
title: 'Операторы изменений'
---

Операторы изменений - набор операторов реализующих определение различных типов [изменений значений свойств](Change_operators_SET_CHANGED_..._.md). 

### Синтаксис

    typeChange(propExpr)

### Описание

Операторы изменений создают [свойства](Properties.md), которые определяют, произошли ли для некоторого свойства в текущей сессии те или иные виды изменений.

### Параметры

*typeChange*

Тип оператора изменений. Задается одним из ключевых слов:

-   **SET**
-   **CHANGED**
-   **DROPPED**
-   **SETCHANGED**
-   **DROPCHANGED**
-   **SETDROPPED**

*propExpr*

[Выражения](Expression.md), значение которого определяет свойство, для которого необходимо определить наличие изменения.

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=OperatorPropertySample&block=changed"/>

**  
**
