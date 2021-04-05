---
title: 'Оператор EMAIL'
---

Оператор **EMAIL** - создание [действия](Actions.md), [отправляющего почту](Send_mail_EMAIL_.md).

### Синтаксис

    EMAIL [FROM fromExpr] 
    [SUBJECT subjExpr]
    recipientType1 recipientExpr1
    ...
    recipientTypeN recipientExprN
    [BODY bodyExpr]
    ATTACH attachFileExpr1 [NAME attachNameExpr1]
    ...
    ATTACH attachFileExprM [NAME attachNameExprM]

### Описание

Оператор **EMAIL** создает действие, которое отправляет письма по электронной почте. 

Предполагается, что в имени вложения расширение файла не задается (то есть точка (.) также считается частью имени файла). Это расширение определяется автоматически по аналогии с [оператором **WRITE**](WRITE_operator.md#extension-broken).

### Параметры

*fromExpr*

[Выражение](Expression.md), значение которого задает адрес отправителя письма. 

*subjExpr*

Выражение, значение которого задает тему письма.

*recipientType1 ... recipientTypeN*

Типы получателей. N>=1. Каждый из них задается одним из ключевых слов:

-   **TO -** получатель письма
-   **СС** - вторичный получатель письма, которому направляется копия
-   **BCC** - получатель письма, чей адрес не показывается другим получателям

*recipientExpr1 ... recipientExprN*

Выражения, значения которых задают адреса получателей письма.

*bodyExpr*

Выражение, значением которого является текст тела письма. Может быть как строкового, так и файлового типа.

*attachFileExpr1 .... *attachFileExprM**

Выражения, значения которых являются файлами, прикрепляемыми к письму как вложение.

*attachNameExpr1 ... *attachNameExprM**

Выражение, значения которых задают имена вложений.

### Пример


import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=email"/>
