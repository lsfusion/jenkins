---
title: 'Оператор SHOW'
---

Оператор **SHOW **- создание [действия](Actions.md), [открывающего форму](In_an_interactive_view_SHOW_DIALOG_.md) в интерактивном представлении. 

### Синтаксис

    SHOW name 
    [OBJECTS objName1 = expr1 [NULL], ..., objNameN = exprN [NULL]]
    [formActionOptions] 

При открытии формы выбора / редактирования синтаксис немного отличается:

    SHOW classFormType className
    = expr [NULL]
    [formActionOptions] 

    formActionOptions - это опции этого действия. Они могут указываться друг за другом в произвольном порядке:

    syncType
    windowType
    MANAGESESSION | NOMANAGESESSION
    NEWSESSION | NESTEDSESSION
    CANCEL | NOCANCEL
    READONLY

### Описание

Оператор **SHOW** создает действие, которое открывает указанную форму. При открытии формы в блоке **OBJECTS** можно задать [о](Form_structure.md)бъектам формы [начальные значения](Open_form.md#params).

### Параметры

*name*

Имя формы. [Составной идентификатор](IDs.md#cid-broken).

*classFormType*

Ключевое слово. Определяет какую именно форму необходимо открыть:

-   **LIST** - выбора
-   **EDIT** - редактирования

*className*

Имя пользовательского класса, форму выбора / редактирования которого необходимо открыть. [Составной идентификатор](IDs.md#cid-broken)

*objName1 ... objNameN*

Имена объектов формы, для которых задаются начальные значения. [Простые идентификаторы](IDs.md#id-broken).

*expr, expr1 ... exprN*

[Выражения](Expression.md), значения которых определяют начальные значения для объектов формы.

*NULL*

Указывает, что передаваемые значения могут быть **NULL**.

### *Опции оператора SHOW*

*syncType*

Определяет, в каком режиме [управления потоком](In_an_interactive_view_SHOW_DIALOG_.md#flow) будет работать оператор:

-   **WAIT** - синхронном. Используется по умолчанию.
-   **NOWAIT** - асинхронном.

*windowType*

Способ [расположения формы](In_an_interactive_view_SHOW_DIALOG_.md#location):

-   **DOCKED** - как закладка. Используется по умолчанию в асинхронном режиме.
-   **FLOAT** -  как окно. Используется по умолчанию в синхронном режиме.

*MANAGESESSION* | *NOMANAGESESSION*

Ключевое слово. Определяет, считается создаваемая форма [собственником сессии](Interactive_view.md) или нет (если считается, то в интерактивном режиме на форме будут показаны соответствующие кнопки управления сессией). По умолчанию, платформа пытается [самостоятельно](Interactive_view.md#sysactions) в зависимости от контекста определить какой режим использовать.

*CANCEL* | *NOCANCEL*

Ключевое слово. Определяет, отображать системное действие Отменить (**System.formCancel**)** **на форме или нет. По умолчанию, платформа пытается [самостоятельно](Interactive_view.md#sysactions) в зависимости от контекста определить какой режим использовать.

*NEWSESSION | NESTEDSESSION*

        Ключевое слово. Определяет, что форма будет открываться в новой (вложенной) сессии. По умолчанию форма открывается в текущей сессии.

*READONLY*

Ключевое слово. Если указывается, то форма открывается в режиме [только для чтения](In_an_interactive_view_SHOW_DIALOG_.md#extra).

### Примеры


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=show"/>
