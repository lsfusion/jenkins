---
title: 'Создание сессий (NEWSESSION, NESTEDSESSION)'
---

Оператор создания новой [сессии](Change_sessions.md) позволяет выполнить действие в другой, отличной от текущей, сессии. 

Как и для других операторов управления сессиями, для оператора создания сессии можно явно указать [вложенные локальные свойства](Session_management.md#nested).

### Вложенные сессии {#nested}

Также в платформе существует возможность создать новую *вложенную* сессию. В этом случае все изменения, произошедшие в текущей сессии, копируются в создаваемую вложенную сессию (это же происходит и при [отмене изменений](Cancel_changes_CANCEL_.md) во вложенной сессии). В то же время, при [применении изменений](Apply_changes_APPLY_.md) в создаваемой вложенной сессии все изменения копируются обратно в текущую сессию (при этом в базу данных они не сохраняются). 

### Язык

Для создания действия, выполняющего другое действие в новой сессии, используется [оператор **NEWSESSION**](NEWSESSION_operator.md) (для вложенных сессий используется [оператор **NESTEDSESSION**](NESTEDSESSION_operator.md)).

### Примеры

import {CodeSample} from './CodeSample.mdx'

<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=newsession"/>


<CodeSample url="https://ru-documentation.lsfusion.org/sample?file=ActionSample&block=nestedsession"/>
